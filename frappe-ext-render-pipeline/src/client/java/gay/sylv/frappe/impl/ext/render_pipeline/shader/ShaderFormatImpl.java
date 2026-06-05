/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.render_pipeline.shader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.github.douira.glsl_transformer.ast.data.ChildNodeList;
import io.github.douira.glsl_transformer.ast.node.Identifier;
import io.github.douira.glsl_transformer.ast.node.TranslationUnit;
import io.github.douira.glsl_transformer.ast.node.declaration.DeclarationMember;
import io.github.douira.glsl_transformer.ast.node.declaration.FunctionDeclaration;
import io.github.douira.glsl_transformer.ast.node.declaration.InterfaceBlockDeclaration;
import io.github.douira.glsl_transformer.ast.node.declaration.TypeAndInitDeclaration;
import io.github.douira.glsl_transformer.ast.node.expression.Expression;
import io.github.douira.glsl_transformer.ast.node.expression.ReferenceExpression;
import io.github.douira.glsl_transformer.ast.node.expression.binary.AssignmentExpression;
import io.github.douira.glsl_transformer.ast.node.expression.unary.FunctionCallExpression;
import io.github.douira.glsl_transformer.ast.node.external_declaration.DeclarationExternalDeclaration;
import io.github.douira.glsl_transformer.ast.node.external_declaration.FunctionDefinition;
import io.github.douira.glsl_transformer.ast.node.statement.CompoundStatement;
import io.github.douira.glsl_transformer.ast.node.statement.Statement;
import io.github.douira.glsl_transformer.ast.node.statement.terminal.ExpressionStatement;
import io.github.douira.glsl_transformer.ast.node.type.FullySpecifiedType;
import io.github.douira.glsl_transformer.ast.node.type.qualifier.InterpolationQualifier;
import io.github.douira.glsl_transformer.ast.node.type.qualifier.LayoutQualifier;
import io.github.douira.glsl_transformer.ast.node.type.qualifier.NamedLayoutQualifierPart;
import io.github.douira.glsl_transformer.ast.node.type.qualifier.StorageQualifier;
import io.github.douira.glsl_transformer.ast.node.type.qualifier.TypeQualifier;
import io.github.douira.glsl_transformer.ast.node.type.qualifier.TypeQualifierPart;
import io.github.douira.glsl_transformer.ast.node.type.specifier.ArraySpecifier;
import io.github.douira.glsl_transformer.ast.node.type.specifier.BuiltinFixedTypeSpecifier;
import io.github.douira.glsl_transformer.ast.node.type.specifier.BuiltinNumericTypeSpecifier;
import io.github.douira.glsl_transformer.ast.node.type.specifier.FunctionPrototype;
import io.github.douira.glsl_transformer.ast.node.type.specifier.TypeSpecifier;
import io.github.douira.glsl_transformer.ast.node.type.struct.StructBody;
import io.github.douira.glsl_transformer.ast.node.type.struct.StructDeclarator;
import io.github.douira.glsl_transformer.ast.node.type.struct.StructMember;
import io.github.douira.glsl_transformer.ast.query.Root;
import io.github.douira.glsl_transformer.ast.transform.ASTInjectionPoint;
import io.github.douira.glsl_transformer.ast.transform.GroupedASTTransformer;
import io.github.douira.glsl_transformer.ast.transform.JobParameters;
import io.github.douira.glsl_transformer.ast.transform.SingleASTTransformer;
import io.github.douira.glsl_transformer.ast.transform.Template;
import io.github.douira.glsl_transformer.util.Type;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline;
import gay.sylv.frappe.api.ext.render_pipeline.shader.PipelineStage;
import gay.sylv.frappe.api.ext.render_pipeline.shader.PipelineStageFormat;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderEvent;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderFormat;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderGlobal;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderItem;
import gay.sylv.frappe.api.ext.render_pipeline.shader.TransformOptions;
import gay.sylv.frappe.mixin.ext.render_pipeline.Accessor_ManyStatement;

public record ShaderFormatImpl(
		@Unmodifiable Map<PipelineStage, List<PipelineStageFormat>> stageFormats,
		Set<String> id,
		@Nullable String singleId
) implements ShaderFormat {
	public static final String IMPORT_FRP = "#custom frp_imports";
	public static final ScopedValue<Integer> MATERIAL_ID = ScopedValue.newInstance();
	private static final Template<Identifier> identifierTemplate = new Template<>(new Identifier("placeholder"));
	private static @Nullable Template<FullySpecifiedType> fullySpecifiedTypeTemplate = null;
	private static @Nullable Template<DeclarationMember> declarationMemberTemplate = null;
	private static @Nullable Template<TypeAndInitDeclaration> typeAndInitDeclarationTemplate = null;
	private static @Nullable Template<DeclarationExternalDeclaration> declarationExternalDeclarationTemplate = null;
	private static @Nullable Template<FunctionDefinition> functionDefinitionTemplate = null;
	private static @Nullable Template<FunctionCallExpression> functionCallExpressionTemplate = null;
	private static @Nullable Template<CompoundStatement> compoundStatementTemplate = null;

	@Override
	public @Unmodifiable Collection<PipelineStageFormat> getStageFormats(PipelineStage pipelineStage) {
		List<PipelineStageFormat> coll = this.stageFormats.get(pipelineStage);

		if (coll == null) {
			return List.of();
		}

		return List.copyOf(coll);
	}

	@Override
	public String combineShaders(
			PipelineStage pipelineStage,
			String mainShaderSource,
			Map<String, String> shaderSources
	) {
		var transformer = new IntMapASTTransformer();

		for (PipelineStageFormat format : this.getStageFormats(pipelineStage)) {
			// TODO: use glsl-preprocessor
			// TODO: proper #line support
			for (Map.Entry<String, String> shaderSource : shaderSources.entrySet()) {
				mainShaderSource = mainShaderSource.replace(IMPORT_FRP, shaderSource.getValue().replaceAll("#version [0-9]{3}", "") + "\n\n" + IMPORT_FRP);
			}

			mainShaderSource = mainShaderSource.replace(IMPORT_FRP, "");

			HashMap<String, String> map = new HashMap<>();
			map.put("$", mainShaderSource);
			map.putAll(shaderSources);
			transformer.setTuMapSupplier(HashMap::new);
			transformer.setResultMapSupplier(HashMap::new);
			transformer.getLexer().enableCustomDirective = true;

			transformer.setTransformation(translationUnitMap -> {
				TranslationUnit mainTranslationUnit = translationUnitMap.get("$");
				Root root = mainTranslationUnit.getRoot();

				// generate event invokers
				for (ShaderEvent event : format.events()) {
					FunctionPrototype eventPrototypeToTemplate = getEventPrototype(event, transformer);
					FunctionPrototype functionPrototype = eventPrototypeToTemplate.cloneInto(root);
					List<Statement> statements = new ArrayList<>(); // callback invocations

					for (Map.Entry<String, TranslationUnit> entry : translationUnitMap.entrySet()) {
						String id = entry.getKey();

						if (id.equals("$")) {
							continue; // skip main shader file
						}

						FunctionDefinition functionDefinition = findFunctionDefinitionStartsWith(entry.getValue(), functionPrototype.getName().getName());

						if (functionDefinition == null) {
							continue;
						}

						FunctionPrototype callbackPrototype = functionDefinition.getFunctionPrototype();
						Identifier functionName = identifierTemplate.getInstanceFor(root);
						functionName.setName(callbackPrototype.getName().getName());
						FunctionCallExpression functionCallExpression = getFunctionCallExpression(event, functionName, root);

						// invoke callback
						ExpressionStatement expressionStatement = root.indexNodes(() -> new ExpressionStatement(null));
						expressionStatement.setExpression(functionCallExpression);
						statements.add(expressionStatement);
					}

					if (functionDefinitionTemplate == null) {
						FunctionDefinition functionDefinition = root.nodeIndex.getOne(FunctionDefinition.class);

						//noinspection ConstantValue
						if (functionDefinition == null) {
							// if it's null, then we don't need an invoker for it anyway
							functionPrototype.detachAndDelete();
							continue;
						}

						functionDefinitionTemplate = new Template<>(functionDefinition);
					}

					if (compoundStatementTemplate == null) {
						CompoundStatement compoundStatement = root.nodeIndex.getOne(CompoundStatement.class);

						//noinspection ConstantValue
						if (compoundStatement == null) {
							// if it's null, then we don't need an invoker for it anyway
							functionPrototype.detachAndDelete();
							continue;
						}

						compoundStatementTemplate = new Template<>(compoundStatement);
					}

					// define invoker (i.e. the event impl)
					CompoundStatement compoundStatement = compoundStatementTemplate.getInstanceFor(root);
					((Accessor_ManyStatement) compoundStatement).frappe_ext_render_pipeline$setStatements(ChildNodeList.collect(statements.stream(), compoundStatement));
					FunctionDefinition functionDefinition = functionDefinitionTemplate.getInstanceFor(root);
					functionDefinition.setFunctionPrototype(functionPrototype);
					functionDefinition.setBody(compoundStatement);
					mainTranslationUnit.injectNode(ASTInjectionPoint.BEFORE_FUNCTIONS, functionDefinition);

					// reorder callbacks
					for (Map.Entry<String, TranslationUnit> entry : translationUnitMap.entrySet()) {
						String id = entry.getKey();

						if (id.equals("$")) {
							continue;
						}

						FunctionDefinition callbackDefinition = findFunctionDefinitionStartsWith(mainTranslationUnit, getCallbackName(functionPrototype.getName().getName(), id));

						if (callbackDefinition == null) {
							continue;
						}

						CompoundStatement body = callbackDefinition.getBody().cloneInto(root);
						callbackDefinition.getBody().detachAndDelete();
						callbackDefinition.detachAndDelete();
						mainTranslationUnit.injectNode(ASTInjectionPoint.BEFORE_FUNCTIONS, root.indexNodes(() -> {
							callbackDefinition.setBody(body);
							return callbackDefinition;
						}));
					}
				}
			});

			mainShaderSource = transformer.transform(map).get("$");
		}

		return mainShaderSource;
	}

	private static FunctionCallExpression getFunctionCallExpression(ShaderEvent event, Identifier functionName, Root root) {
		if (functionCallExpressionTemplate == null) {
			FunctionCallExpression functionCallExpression = root.nodeIndex.getOne(FunctionCallExpression.class);
			functionCallExpressionTemplate = new Template<>(functionCallExpression.cloneInto(root));
		}

		FunctionCallExpression functionCallExpression;

		if (event.parameters().isEmpty()) {
			functionCallExpression = root.indexNodes(() -> new FunctionCallExpression((Identifier) null));
		} else {
			List<Expression> arguments = new ArrayList<>();

			for (ShaderEvent.Parameter parameter : event.parameters()) {
				arguments.add(new ReferenceExpression(new Identifier(parameter.name())));
			}

			functionCallExpression = root.indexNodes(() -> new FunctionCallExpression((Identifier) null, arguments.stream()));
		}

		root.registerNode(functionCallExpression, true);
		functionCallExpression.useFunctionName(functionName);

		return functionCallExpression;
	}

	private static String getCallbackName(String name, String id) {
		return name + "_" + id.replaceAll("[^\\w_0-9]", "_");
	}

	private static FunctionPrototype getEventPrototype(ShaderEvent event, IntMapASTTransformer transformer) {
		return ((FunctionDeclaration) ((DeclarationExternalDeclaration) transformer.parseSeparateExternalDeclaration(
				event.toString())).getDeclaration()).getFunctionPrototype();
	}

	@Override
	public String transformSingleShader(PipelineStage pipelineStage, String shaderSource, String id, Map<String, String> perPipelineDefines) {
		var transformer = new SingleASTTransformer<>((_, root) -> {
			for (PipelineStageFormat format : this.getStageFormats(pipelineStage)) {
				// rename event callbacks
				for (ShaderEvent event : format.events()) {
					root.process(
							root.identifierIndex.getStream(event.identifier())
									.filter(identifier -> identifier.hasAncestor(FunctionDefinition.class)),
							identifier -> identifier.setName(getCallbackName(identifier.getName(), id))
					);
				}

				// TODO: use glsl-preprocessor to substitute pipeline-specific constants (let API users pass in a Map)
			}
		});
		transformer.getLexer().enableCustomDirective = true;
		shaderSource = transformer.transform(shaderSource);

		// substitute per-pipeline defines
		for (Map.Entry<String, String> entry : perPipelineDefines.entrySet()) {
			shaderSource = shaderSource.replaceAll("\\Q" + entry.getKey() + "\\E", entry.getValue());
		}

		return shaderSource;
	}

	@Override
	public String transformCombinedShader(
			PipelineStage pipelineStage,
			String shaderSource,
			TransformOptions transformOptions
	) {
		var transformer = new SingleASTTransformer<>((translationUnit, root) -> {
			// set up templates
			if (fullySpecifiedTypeTemplate == null) {
				FunctionPrototype functionPrototype = root.nodeIndex.getOne(FunctionPrototype.class);
				fullySpecifiedTypeTemplate = new Template<>(functionPrototype.getReturnType().cloneInto(root));
			}

			if (declarationMemberTemplate == null) {
				DeclarationMember declarationMember = root.nodeIndex.getOne(DeclarationMember.class);
				declarationMemberTemplate = new Template<>(declarationMember.cloneInto(root));
			}

			if (typeAndInitDeclarationTemplate == null) {
				TypeAndInitDeclaration typeAndInitDeclaration = root.nodeIndex.getOne(TypeAndInitDeclaration.class);
				typeAndInitDeclarationTemplate = new Template<>(typeAndInitDeclaration.cloneInto(root));
			}

			if (declarationExternalDeclarationTemplate == null) {
				DeclarationExternalDeclaration declarationExternalDeclaration = root.nodeIndex.getOne(DeclarationExternalDeclaration.class);
				declarationExternalDeclarationTemplate = new Template<>(declarationExternalDeclaration.cloneInto(root));
			}

			for (PipelineStageFormat format : this.getStageFormats(pipelineStage)) {
				// declare globals
				for (ShaderGlobal global : format.globals()) {
					if (global.type().specifier().isOpaque()) {
						if (!global.type().qualifiers().contains(ShaderItem.Qualifier.CONST)) {
							throw new IllegalStateException("Error at shader global '" + global.identifier() + "': Opaque types must be const in GLSL as they can only be uniforms.");
						}

						// find true identifier
						AssignmentExpression assignmentExpression = root.identifierIndex.getStream(global.identifier())
								.filter(id -> id.hasAncestor(AssignmentExpression.class))
								.map(id -> id.getAncestor(AssignmentExpression.class))
								.findFirst()
								.orElse(null);
						Objects.requireNonNull(assignmentExpression, "Error at shader global '" + global.identifier() + "': Opaquely typed globals must be assigned to their respective uniforms.");
						assignmentExpression.getAncestor(Statement.class).detachAndDelete();
						String realId = ((ReferenceExpression) assignmentExpression.getRight())
								.getIdentifier()
								.getName();

						// find and replace opaquely typed globals
						root.identifierIndex.getStream(global.identifier())
								.filter(id -> id.getParent() instanceof ReferenceExpression)
								.map(id -> (ReferenceExpression) id.getParent())
								.forEach(referenceExpression ->
										referenceExpression.setIdentifier(new Identifier(realId)));

						continue; // opaque types can only act as aliases as they cannot be set in variables
					}

					FullySpecifiedType fullySpecifiedType = getFullySpecifiedType(root, global.type(), false);
					DeclarationMember declarationMember = declarationMemberTemplate.getInstanceFor(root);
					declarationMember.setName(new Identifier(global.identifier()));
					declarationMember.setInitializer(null);

					// FIXME: potential NPE here, what to do about arrays?
					if (global.type().array()) {
						declarationMember.setArraySpecifier(new ArraySpecifier(null));
					}

					boolean isUniform = false;
					ShaderFormat shaderFormat = ShaderFormat.getFormat(format.shaderFormatId());

					// declare it as a uniform if a uniform is assigned to this global
					if (shaderFormat != null && FrappeRenderPipeline.exists(shaderFormat)) {
						FrappeRenderPipeline renderPipeline = FrappeRenderPipeline.getOrCreate(shaderFormat);

						if (renderPipeline.uniformTypes().containsKey(global.identifier())) {
							isUniform = true;

							//CHECKSTYLE.OFF: MatchXpath
							if (!transformOptions.useUniformBlocks()) {
								fullySpecifiedType.getTypeQualifier().getParts().add(root.indexNodes(() -> new StorageQualifier(StorageQualifier.StorageType.UNIFORM)));
							}

							//CHECKSTYLE.ON: MatchXpath
						}
					}

					DeclarationExternalDeclaration declarationExternalDeclaration =
							declarationExternalDeclarationTemplate.getInstanceFor(root);

					if (!isUniform || !transformOptions.useUniformBlocks()) {
						TypeAndInitDeclaration declaration = typeAndInitDeclarationTemplate.getInstanceFor(root);
						declaration.setType(fullySpecifiedType);
						declaration.getMembers().clear();
						declaration.getMembers().add(declarationMember);
						declarationExternalDeclaration.setDeclaration(declaration);
					} else {
						InterfaceBlockDeclaration declaration = root.indexNodes(() -> new InterfaceBlockDeclaration(
								null,
								new Identifier("frp_uniformBlock_" + declarationMember.getName().getName()),
								null
						));
						declaration.setTypeQualifier(root.indexNodes(() -> new TypeQualifier(Stream.of(
								new LayoutQualifier(Stream.of(
										new NamedLayoutQualifierPart(new Identifier("std140")))),
								new StorageQualifier(StorageQualifier.StorageType.UNIFORM)
						))));
						declaration.setStructBody(root.indexNodes(() -> new StructBody(Stream.of(new StructMember(
								fullySpecifiedType,
								Stream.of(new StructDeclarator(declarationMember.getName().cloneInto(root)))
						)))));
						declarationExternalDeclaration.setDeclaration(declaration);
					}

					translationUnit.injectNode(ASTInjectionPoint.BEFORE_DECLARATIONS, declarationExternalDeclaration);
				}
			}
		});
		transformer.getLexer().enableCustomDirective = true;
		return transformer.transform(shaderSource);
	}

	private static FullySpecifiedType getFullySpecifiedType(Root root, ShaderItem.Type type, boolean addQualifiers) {
		List<TypeQualifierPart> typeQualifierParts = new ArrayList<>();

		for (ShaderItem.Qualifier qualifier : type.qualifiers()) {
			typeQualifierParts.add(switch (qualifier) {
				case CONST -> new StorageQualifier(StorageQualifier.StorageType.CONST);
				case IN -> new StorageQualifier(StorageQualifier.StorageType.IN);
				case OUT -> new StorageQualifier(StorageQualifier.StorageType.OUT);
				case FLAT -> new InterpolationQualifier(InterpolationQualifier.InterpolationType.FLAT);
			});
		}

		TypeSpecifier specifier;
		String specifierString = type.specifier().toString();

		if (specifierString.contains("sampler") || type.specifier().equals(ShaderItem.Specifier.ACCELERATION_STRUCTURE_EXT)) {
			specifier = new BuiltinFixedTypeSpecifier(BuiltinFixedTypeSpecifier.BuiltinType.valueOf(type.specifier().name()));
		} else {
			String name = type.specifier().name();

			if (name.startsWith("VEC")) {
				name = "F32" + name;
			} else if (name.startsWith("MAT")) {
				name = "F32" + name;
			}

			if (name.equals("INT")) {
				name = "INT32";
			} else if (name.startsWith("I")) {
				name = "I32" + name.replaceFirst("I", "");
			}

			if (name.equals("UINT")) {
				name = "UINT32";
			} else if (name.startsWith("U")) {
				name = "U32" + name.replaceFirst("U", "");
			}

			if (name.equals("FLOAT")) {
				name = "FLOAT32";
			}

			specifier = new BuiltinNumericTypeSpecifier(Type.valueOf(name));
		}

		if (type.array()) {
			specifier.setArraySpecifier(new ArraySpecifier(null));
		}

		TypeQualifier typeQualifier;

		if (addQualifiers) {
			typeQualifier = new TypeQualifier(typeQualifierParts.stream());
		} else {
			typeQualifier = new TypeQualifier(Stream.of());
		}

		FullySpecifiedType fullySpecifiedType = Objects.requireNonNull(fullySpecifiedTypeTemplate).getInstanceFor(root);
		fullySpecifiedType.setTypeSpecifier(specifier);
		fullySpecifiedType.setTypeQualifier(typeQualifier);
		return fullySpecifiedType;
	}

	private static class IntMapASTTransformer extends GroupedASTTransformer<JobParameters, String, Map<String, String>, Map<String, TranslationUnit>> {
		IntMapASTTransformer(
				Supplier<Map<String, TranslationUnit>> tuMapSupplier,
				Supplier<Map<String, String>> resultMapSupplier
		) {
			super(tuMapSupplier, resultMapSupplier);
		}

		IntMapASTTransformer(
				Consumer<Map<String, TranslationUnit>> transformation,
				Supplier<Map<String, TranslationUnit>> tuMapSupplier,
				Supplier<Map<String, String>> resultMapSupplier
		) {
			super(transformation, tuMapSupplier, resultMapSupplier);
		}

		IntMapASTTransformer(
				BiConsumer<Map<String, TranslationUnit>, JobParameters> transformation,
				Supplier<Map<String, TranslationUnit>> tuMapSupplier,
				Supplier<Map<String, String>> resultMapSupplier
		) {
			super(transformation, tuMapSupplier, resultMapSupplier);
		}

		IntMapASTTransformer(Consumer<Map<String, TranslationUnit>> transformation) {
			super(transformation);
		}

		IntMapASTTransformer() {
			super();
		}
	}

	private static @Nullable FunctionDefinition findFunctionDefinitionStartsWith(TranslationUnit translationUnit, String name) {
		return translationUnit.getRoot().nodeIndex.getStream(FunctionDefinition.class)
				.filter(functionDefinition -> functionDefinition.getFunctionPrototype().getName().getName().startsWith(name))
				.findFirst()
				.orElse(null);
	}
}
