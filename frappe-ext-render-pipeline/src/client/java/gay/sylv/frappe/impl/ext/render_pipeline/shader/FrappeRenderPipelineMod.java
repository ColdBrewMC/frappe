/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.render_pipeline.shader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.douira.glsl_transformer.GLSLLexer;
import io.github.douira.glsl_transformer.GLSLParser;
import io.github.douira.glsl_transformer.GLSLParserBaseVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;

import gay.sylv.frappe.api.base.extension.RendererExtensionMetadata;
import gay.sylv.frappe.api.ext.render_pipeline.shader.PipelineStage;
import gay.sylv.frappe.api.ext.render_pipeline.shader.PipelineStageFormat;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderEvent;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderFormat;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderGlobal;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderItem.Qualifier;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderItem.Specifier;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderItem.Type;

// this is where the magic happens
public class FrappeRenderPipelineMod implements ModInitializer {
	private static final String SHADER_FORMAT_KEY = "frappe-ext-render-pipeline:shader-format";
	private static final Logger LOGGER = LoggerFactory.getLogger("frappe-ext-render-pipeline");
	public static Map<String, Map<String, ShaderFormat>> EXTENSION_ID_2_SHADER_FORMAT = new HashMap<>();
	public static Map<String, ShaderFormat> ID_2_SHADER_FORMAT = new HashMap<>();
	private static boolean initialize = false;

	static {
		try {
			new FrappeRenderPipelineMod().onInitialize();
		} catch (Throwable t) {
			LOGGER.error("Failure during static initialization", t);
			throw t;
		}
	}

	@Override
	public void onInitialize() {
		if (initialize) {
			return;
		}

		initialize = true;

		for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
			ModMetadata modMetadata = modContainer.getMetadata();

			if (modMetadata.containsCustomValue(SHADER_FORMAT_KEY)) {
				CustomValue.CvObject object = modMetadata.getCustomValue(SHADER_FORMAT_KEY).getAsObject();

				if (!object.containsKey("shaderFormats")) {
					continue;
				}

				for (CustomValue value : object.get("shaderFormats").getAsArray()) {
					if (value.getType().equals(CustomValue.CvType.OBJECT)) {
						continue; // skip objects since we may support them in the future
					}

					String formatPath = value.getAsString() + ".json";
					JsonElement shaderFormatElement = findAndParseJson(modContainer, formatPath);

					if (shaderFormatElement == null) {
						continue;
					}

					ShaderFormatV0 shaderFormat = decodeJson(ShaderFormatV0.CODEC, shaderFormatElement);

					if (shaderFormat == null) {
						continue;
					}

					if (shaderFormat.frappeExtension().isPresent() && !RendererExtensionMetadata.isExtensionEnabled(shaderFormat.frappeExtension().get())) {
						continue;
					}

					Map<PipelineStage, List<PipelineStageFormat>> stageFormats = new HashMap<>();

					parseFormat(modContainer, shaderFormat.vertexFormat(), PipelineStage.VERTEX, stageFormats, formatPath);
					parseFormat(modContainer, shaderFormat.fragmentFormat(), PipelineStage.FRAGMENT, stageFormats, formatPath);

					if (!stageFormats.isEmpty()) {
						String id = getFormatIdFromPath(formatPath);
						Map<String, ShaderFormat> map;

						if (shaderFormat.frappeExtension().isPresent()) {
							map = EXTENSION_ID_2_SHADER_FORMAT.computeIfAbsent(
									shaderFormat.frappeExtension().get(),
									_ -> new HashMap<>()
							);
						} else {
							map = ID_2_SHADER_FORMAT;
						}

						ShaderFormatImpl value1 = new ShaderFormatImpl(Map.copyOf(stageFormats), Set.of(id), id);
						map.put(id, value1);
					}
				}
			}
		}

		EXTENSION_ID_2_SHADER_FORMAT.replaceAll((_, v) -> Map.copyOf(v));
		EXTENSION_ID_2_SHADER_FORMAT = Map.copyOf(EXTENSION_ID_2_SHADER_FORMAT);
		ID_2_SHADER_FORMAT = Map.copyOf(ID_2_SHADER_FORMAT);
	}

	private static String getFormatIdFromPath(String formatPath) {
		return Path.of(formatPath).getFileName().toString().replace(".json", "");
	}

	private static void parseFormat(
			ModContainer modContainer,
			@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // never constructed
			Optional<String> format,
			PipelineStage pipelineStage,
			Map<PipelineStage, List<PipelineStageFormat>> stageFormats,
			String formatPath
	) {
		if (format.isPresent()) {
			PipelineStageFormat stageFormat = parseStageFormat(modContainer, pipelineStage, format.get(), formatPath);

			if (stageFormat != null) {
				stageFormats.put(stageFormat.pipelineStage(), List.of(stageFormat));
			}
		}
	}

	private static @Nullable PipelineStageFormat parseStageFormat(
			ModContainer modContainer,
			PipelineStage pipelineStage,
			String pathString,
			String formatPath
	) {
		String source = readFile(modContainer, pathString);

		// hack in case it's in the same folder
		if (source == null) {
			source = readFile(modContainer, formatPath + "/../" + pathString);
		}

		if (source == null) {
			return null;
		}

		// TODO: use glsl-preprocessor to process #include directives

		GLSLLexer lexer = new GLSLLexer(CharStreams.fromString(source));
		GLSLParser parser = new GLSLParser(new CommonTokenStream(lexer));
		GLSLParserBaseVisitor<PartialStageFormat> visitor = new GLSLParserBaseVisitor<>() {
			private final Set<ShaderEvent> events = new HashSet<>();
			private final Set<ShaderGlobal> globals = new HashSet<>();
			private final PartialStageFormat partialStageFormat = new PartialStageFormat(events, globals);

			@Override
			protected PartialStageFormat defaultResult() {
				return this.partialStageFormat;
			}

			@Override
			public PartialStageFormat visitFunctionDeclaration(GLSLParser.FunctionDeclarationContext ctx) {
				GLSLParser.FunctionPrototypeContext functionPrototype = ctx.functionPrototype();
				Type result = parseType(functionPrototype.fullySpecifiedType());
				List<ShaderEvent.Parameter> parameters = new ArrayList<>();

				for (GLSLParser.ParameterDeclarationContext parameterDeclaration : functionPrototype.functionParameterList()
						.parameterDeclaration()) {
					parameters.add(new ParameterImpl(parseType(parameterDeclaration.fullySpecifiedType()), parameterDeclaration.parameterName.getText()));
				}

				this.events.add(new ShaderEventImpl(
						functionPrototype.IDENTIFIER().getText(),
						List.copyOf(parameters),
						result,
						List.of()
				));
				return super.visitFunctionDeclaration(ctx);
			}

			@Override
			public PartialStageFormat visitTypeAndInitDeclaration(GLSLParser.TypeAndInitDeclarationContext ctx) {
				Type type = parseType(ctx.fullySpecifiedType());

				for (GLSLParser.DeclarationMemberContext declarationMember : ctx.declarationMember()) {
					this.globals.add(new ShaderGlobalImpl(declarationMember.IDENTIFIER().getText(), type, List.of()));
				}

				return super.visitTypeAndInitDeclaration(ctx);
			}
		};
		PartialStageFormat partialStageFormat = visitor.visit(parser.translationUnit());

		// TODO: relicense Frappé to LGPLv3 so we can use glsl-preprocessor to enumerate the shader defines
		return new PipelineStageFormatImpl(
				getFormatIdFromPath(formatPath),
				pipelineStage,
				Set.copyOf(partialStageFormat.events()),
				Set.copyOf(partialStageFormat.globals()),
				Set.of()
		);
	}

	private static Type parseType(GLSLParser.FullySpecifiedTypeContext fullySpecifiedType) {
		GLSLParser.TypeSpecifierContext typeSpecifier = fullySpecifiedType.typeSpecifier();
		String specifierName;

		if (typeSpecifier.builtinTypeSpecifierFixed() != null) {
			specifierName = typeSpecifier.builtinTypeSpecifierFixed().getText();
		} else if (typeSpecifier.builtinTypeSpecifierParseable() != null) {
			specifierName = typeSpecifier.builtinTypeSpecifierParseable().getText();
		} else {
			throw new IllegalArgumentException("Only built-in types are supported.");
		}

		specifierName = specifierName
				.replace("F32", "")
				.replace("32", "");
		Specifier specifier = Specifier.valueOf(specifierName.toUpperCase(Locale.ROOT));
		Set<Qualifier> qualifiers = new HashSet<>();

		if (fullySpecifiedType.typeQualifier() != null) {
			GLSLParser.TypeQualifierContext typeQualifier = fullySpecifiedType.typeQualifier();
			addQualifiers(typeQualifier.storageQualifier(), qualifiers);
			addQualifiers(typeQualifier.interpolationQualifier(), qualifiers);
		}

		return new ShaderItemTypeImpl(
				typeSpecifier.arraySpecifier() != null,
				qualifiers,
				specifier
		);
	}

	private static void addQualifiers(
			List<? extends ParserRuleContext> children,
			Set<Qualifier> qualifiers
	) {
		for (ParserRuleContext context : children) {
			qualifiers.add(Qualifier.valueOf(context.getText().toUpperCase(Locale.ROOT)));
		}
	}

	private static @Nullable String readFile(ModContainer modContainer, String pathString) {
		String source = null;

		for (Path path : modContainer.getRootPaths()) {
			try {
				source = Files.readString(path.resolve(pathString));
				break;
			} catch (NoSuchFileException _) {
				// ignored
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return source;
	}

	private static @Nullable JsonElement findAndParseJson(ModContainer modContainer, String formatPath) {
		JsonElement shaderFormatElement = null;

		for (Path path : modContainer.getRootPaths()) {
			try {
				shaderFormatElement = JsonParser.parseString(Files.readString(path.resolve(formatPath)));
				break;
			} catch (NoSuchFileException _) {
				// ignored
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return shaderFormatElement;
	}

	private static <T> @Nullable T decodeJson(Codec<T> codec, JsonElement jsonElement) {
		return codec.parse(JsonOps.INSTANCE, jsonElement)
				.resultOrPartial(LOGGER::error)
				.orElse(null);
	}

	private record ShaderFormatV0(
			int schemaVersion,
			Optional<String> frappeExtension,
			Optional<String> vertexFormat,
			Optional<String> fragmentFormat,
			Optional<String> pipelineDefines
	) {
		public static final Codec<ShaderFormatV0> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("schema_version")
						.validate(schemaVersion -> {
							if (schemaVersion != 0) {
								return DataResult.error(() -> "Invalid schema version: " + schemaVersion);
							} else {
								return DataResult.success(schemaVersion);
							}
						})
						.forGetter(ShaderFormatV0::schemaVersion),
				Codec.STRING.optionalFieldOf("frappe_extension")
						.forGetter(ShaderFormatV0::frappeExtension),
				Codec.STRING.optionalFieldOf("vertex_format")
						.forGetter(ShaderFormatV0::vertexFormat),
				Codec.STRING.optionalFieldOf("fragment_format")
						.forGetter(ShaderFormatV0::fragmentFormat),
				Codec.STRING.optionalFieldOf("pipeline_defines")
						.forGetter(ShaderFormatV0::pipelineDefines)
		).apply(instance, ShaderFormatV0::new));
	}

	private record PartialStageFormat(Set<ShaderEvent> events, Set<ShaderGlobal> globals) {
	}
}
