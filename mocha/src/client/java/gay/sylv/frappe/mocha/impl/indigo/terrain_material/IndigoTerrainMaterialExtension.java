/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import static gay.sylv.frappe.impl.base.FrappeInitializer.frappeId;
import static gay.sylv.frappe.mocha.impl.Mocha.modId;
import static net.minecraft.client.renderer.RenderPipelines.CUTOUT_TERRAIN;
import static net.minecraft.client.renderer.RenderPipelines.SOLID_TERRAIN;
import static net.minecraft.client.renderer.RenderPipelines.TRANSLUCENT_TERRAIN;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.PolygonMode;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.apache.commons.lang3.RandomStringUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
import net.minecraft.resources.Identifier;

import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialExtension;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialRegistryEntrypoint;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;
import gay.sylv.frappe.mocha.impl.indigo.vertex.format.MochaVertexFormats;

public final class IndigoTerrainMaterialExtension implements TerrainMaterialExtension, IndigoRendererExtension {
	public static Map<RenderPipeline, RenderPipeline> VANILLA_2_MOCHA_TERRAIN_PIPELINES = Map.of();
	public static Map<ChunkSectionLayer, RenderPipeline> CSL_2_MOCHA_TERRAIN_PIPELINES = Map.of();
	public static Map<RenderPipeline, RenderPipeline> VANILLA_2_SIMPLE_MOCHA_TERRAIN_PIPELINES = Map.of();
	private static final Logger LOGGER = LoggerFactory.getLogger("Mocha/Indigo/frappe-ext-terrain-material");
	public static String mochaFragmentShader = "";
	public static String mochaVertexShader = "";
	public static Runnable chunkSectionLayerInit;
	public static Runnable chunkSectionLayerGroupInit;
	public static ChunkSectionLayerGroup MOCHA_OPAQUE_SOLID;
	public static ChunkSectionLayerGroup MOCHA_OPAQUE_CUTOUT;
	public static ChunkSectionLayer MOCHA_SOLID;
	public static ChunkSectionLayer MOCHA_CUTOUT;
	public static RenderPipeline MOCHA_SOLID_WIREFRAME;
	public static RenderPipeline MOCHA_CUTOUT_WIREFRAME;
	public static RenderPipeline.Snippet MOCHA_SOLID_SNIPPET;
	public static RenderPipeline.Snippet MOCHA_CUTOUT_SNIPPET;
	public static List<ChunkSectionLayer> SOLID_LAYERS = new ArrayList<>();
	public static List<ChunkSectionLayer> CUTOUT_LAYERS = new ArrayList<>();

	@Override
	public TerrainMaterial createChunkLayer(
			Identifier shaderId,
			String label,
			TerrainMaterial.Complexity complexity,
			@Nullable Function<RenderPipeline.Builder, RenderPipeline.Builder> renderPipelineModifier,
			@Nullable Runnable preRenderPassState,
			@Nullable Runnable postRenderPassState,
			@Nullable Consumer<RenderPass> renderPassSetup,
			@Nullable Consumer<RenderPass> renderPassCleanup
	) {
		return new IndigoTerrainMaterial(
				shaderId,
				label,
				complexity,
				renderPipelineModifier,
				preRenderPassState,
				postRenderPassState,
				renderPassSetup,
				renderPassCleanup
		);
	}

	@Override
	public void registerMaterialImpl(TerrainMaterial material) {
		if (!VANILLA_2_MOCHA_TERRAIN_PIPELINES.isEmpty()) {
			throw new IllegalStateException("TerrainMaterial registration must occur during the frappe-ext-terrain-material:registry entrypoint.");
		}

		int index = MochaIndigoEncodingFormat.terrainMaterialCount;
		MochaIndigoEncodingFormat.TERRAIN_MATERIAL_2_INDEX.put(material, index);
		MochaIndigoEncodingFormat.TERRAIN_MATERIALS[index] = material;
		MochaIndigoEncodingFormat.terrainMaterialCount++;
	}

	public static void resolveMaterials() {
		resolveMaterials(false);
	}

	// TODO: support resource reloading
	// This is cursed as fuck, but it lets us do cool things:tm:
	// https://regexlicensing.com
	public static void resolveMaterials(boolean reload) {
		if (!VANILLA_2_MOCHA_TERRAIN_PIPELINES.isEmpty() && !reload) {
			return;
		}

		if (!reload) {
			FabricLoader.getInstance().invokeEntrypoints(
					"frappe-ext-terrain-material:registry",
					TerrainMaterialRegistryEntrypoint.class,
					TerrainMaterialRegistryEntrypoint::onTerrainMaterialRegistry
			);
		}

		RenderPipeline.Builder solid = RenderPipeline.builder(RenderPipelines.TERRAIN_SNIPPET)
				.withLocation("pipeline/solid_terrain");
		RenderPipeline.Builder cutout = RenderPipeline.builder(RenderPipelines.TERRAIN_SNIPPET)
				.withLocation("pipeline/cutout_terrain")
				.withShaderDefine("ALPHA_CUTOUT", 0.5f);
		RenderPipeline.Builder translucent = RenderPipeline.builder(RenderPipelines.TERRAIN_SNIPPET)
				.withBlend(BlendFunction.TRANSLUCENT)
				.withShaderDefine("ALPHA_CUTOUT", 0.01f);
		List<RenderPipeline.Builder> builders = List.of(solid, cutout, translucent);

		// Load Mocha's fragment/vertex shader so we can modify it.
		try {
			mochaFragmentShader = Files.readString(getShaderPath(modId("include/fragment"), "glsl").orElseThrow());
			mochaVertexShader = Files.readString(getShaderPath(modId("include/vertex"), "glsl").orElseThrow());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String preFragmentSimpleTemplate = findFunction(mochaFragmentShader, "simple_pre_fragment");
		StringBuilder preFragmentSimpleFunctions = new StringBuilder();
		StringBuilder preFragmentSimpleBuilder = new StringBuilder();
		String preFragmentTemplate = findFunction(mochaFragmentShader, "pre_fragment");
		StringBuilder preFragmentFunctions = new StringBuilder();
		StringBuilder preFragmentBuilder = new StringBuilder();
		String frappeUvTemplate = findFunction(mochaVertexShader, "modify_uv");
		StringBuilder frappeUvFunctions = new StringBuilder();
		StringBuilder frappeUvBuilder = new StringBuilder();

		for (int i = 0; i < MochaIndigoEncodingFormat.terrainMaterialCount; i++) {
			TerrainMaterial material = MochaIndigoEncodingFormat.TERRAIN_MATERIALS[i];
			Identifier shaderId = material.shaderId();
			Optional<Path> fragmentShaderPath = getShaderPath(shaderId, "fsh");
			Optional<Path> vertexShaderPath = getShaderPath(shaderId, "vsh");

			try {
				if (fragmentShaderPath.isPresent()) {
					String shader = Files.readString(fragmentShaderPath.get());

					if (material.simple()) {
						shader = shader
								.replaceFirst("#version [0-9]{3}", "")
								.replaceFirst("(?<=vec4 )frappe_simple_pre_fragment(?=\\()", "_frappe_simple_pre_fragment_" + i)
								.replaceFirst("(?<=vec4 )frappe_pre_fragment(?=\\()", RandomStringUtils.secure().nextAlphabetic(24));

						if (shader.contains("_frappe_simple_pre_fragment_")) {
							preFragmentSimpleBuilder.append(preFragmentSimpleTemplate
									.replaceAll("_FRAPPE_MATERIAL_ID", Integer.toString(i)));
							preFragmentSimpleFunctions.append(shader);
						}
					} else {
						shader = shader
								.replaceFirst("#version [0-9]{3}", "")
								.replaceFirst("(?<=vec4 )frappe_pre_fragment(?=\\()", "_frappe_pre_fragment_" + i)
								.replaceFirst("(?<=vec4 )frappe_simple_pre_fragment(?=\\()", RandomStringUtils.secure().nextAlphabetic(24));

						if (shader.contains("_frappe_pre_fragment_")) {
							preFragmentBuilder.append(preFragmentTemplate
									.replaceAll("_FRAPPE_MATERIAL_ID", Integer.toString(i)));
							preFragmentFunctions.append(shader);
						}
					}

					for (RenderPipeline.Builder builder : builders) {
						builder.withShaderDefine("_FRAPPE_FRAGMENT");

						if (shader.contains("_frappe_simple_pre_fragment_")) {
							builder.withShaderDefine("_FRAPPE_SIMPLE_PRE_FRAGMENT");
						} else if (shader.contains("_frappe_pre_fragment_")) {
							builder.withShaderDefine("_FRAPPE_PRE_FRAGMENT");
						}
					}
				}

				if (vertexShaderPath.isPresent()) {
					String shader = Files.readString(vertexShaderPath.get());

					shader = shader
							.replaceFirst("#version [0-9]{3}", "")
							.replaceFirst("(?<=vec2 )frappe_modify_uv(?=\\()", "_frappe_modify_uv_" + i);

					if (shader.contains("_frappe_modify_uv_")) {
						frappeUvBuilder.append(frappeUvTemplate
								.replaceAll("_FRAPPE_MATERIAL_ID", Integer.toString(i)));
						frappeUvFunctions.append(shader);
					}

					for (RenderPipeline.Builder builder : builders) {
						builder.withShaderDefine("_FRAPPE_VERTEX");

						if (shader.contains("_frappe_modify_uv_")) {
							builder.withShaderDefine("_FRAPPE_MODIFY_UV");
						}
					}
				}
			} catch (IOException e) {
				LOGGER.error("An error occurred while parsing terrain material shaders", e);
			}
		}

		mochaFragmentShader = substituteFunction(mochaFragmentShader, "simple_pre_fragment", preFragmentSimpleFunctions.toString(), preFragmentSimpleBuilder.toString(), "color");
		mochaFragmentShader = substituteFunction(mochaFragmentShader, "pre_fragment", preFragmentFunctions.toString(), preFragmentBuilder.toString(), "color");
		mochaVertexShader = substituteFunction(mochaVertexShader, "modify_uv", frappeUvFunctions.toString(), frappeUvBuilder.toString(), "uv");

		RenderPipeline.Snippet solidSnippet = solid.buildSnippet();
		RenderPipeline.Snippet cutoutSnippet = cutout.buildSnippet();
		RenderPipeline.Snippet translucentSnippet = translucent.buildSnippet();

		RenderPipeline.Builder complexSolid = RenderPipeline.builder(solidSnippet)
				.withLocation(frappeId("pipeline/solid_terrain"));
		RenderPipeline.Builder complexCutout = RenderPipeline.builder(cutoutSnippet)
				.withLocation(frappeId("pipeline/cutout_terrain"));
		List<RenderPipeline.Builder> complexBuilders = List.of(complexSolid, complexCutout);

		for (RenderPipeline.Builder builder : complexBuilders) {
			builder.withVertexFormat(MochaVertexFormats.COMPLEX_TERRAIN, VertexFormat.Mode.QUADS);
			builder.withShaderDefine("_FRAPPE_COMPLEX_MATERIAL");
		}

		MOCHA_SOLID_SNIPPET = complexSolid.buildSnippet();
		MOCHA_CUTOUT_SNIPPET = complexCutout.buildSnippet();

		RenderPipeline.Builder simpleSolid = RenderPipeline.builder(solidSnippet)
				.withLocation("pipeline/solid_terrain");
		RenderPipeline.Builder simpleCutout = RenderPipeline.builder(cutoutSnippet)
				.withLocation("pipeline/cutout_terrain");
		RenderPipeline.Builder simpleTranslucent = RenderPipeline.builder(translucentSnippet)
				.withLocation("pipeline/translucent_terrain");
		List<RenderPipeline.Builder> simpleBuilders = List.of(
				simpleSolid,
				simpleCutout,
				simpleTranslucent
		);

		for (RenderPipeline.Builder builder : simpleBuilders) {
			builder.withVertexFormat(MochaVertexFormats.SIMPLE_TERRAIN, VertexFormat.Mode.QUADS);
			builder.withShaderDefine("_FRAPPE_SIMPLE_MATERIAL");
		}

		MOCHA_SOLID_WIREFRAME = RenderPipeline.builder(complexSolid.buildSnippet())
				.withLocation(modId("pipeline/wireframe_solid_terrain"))
				.withPolygonMode(PolygonMode.WIREFRAME)
				.build();
		MOCHA_CUTOUT_WIREFRAME = RenderPipeline.builder(complexCutout.buildSnippet())
				.withLocation(modId("pipeline/wireframe_cutout_terrain"))
				.withPolygonMode(PolygonMode.WIREFRAME)
				.build();

		VANILLA_2_MOCHA_TERRAIN_PIPELINES = Map.of(
				SOLID_TERRAIN, complexSolid.build(),
				CUTOUT_TERRAIN, complexCutout.build(),
				TRANSLUCENT_TERRAIN, simpleTranslucent.build()
		);

		VANILLA_2_SIMPLE_MOCHA_TERRAIN_PIPELINES = Map.of(
				SOLID_TERRAIN, simpleSolid.build(),
				CUTOUT_TERRAIN, simpleCutout.build(),
				TRANSLUCENT_TERRAIN, simpleTranslucent.build()
		);
	}

	public static void postResolveMaterials() {
		CSL_2_MOCHA_TERRAIN_PIPELINES = Map.of(
				ChunkSectionLayer.SOLID, VANILLA_2_MOCHA_TERRAIN_PIPELINES.get(SOLID_TERRAIN),
				ChunkSectionLayer.CUTOUT, VANILLA_2_MOCHA_TERRAIN_PIPELINES.get(CUTOUT_TERRAIN),
				ChunkSectionLayer.TRANSLUCENT, VANILLA_2_MOCHA_TERRAIN_PIPELINES.get(TRANSLUCENT_TERRAIN)
		);
	}

	private static String findFunction(String shader, String functionName) {
		String fun = Pattern.quote("_frappe_" + functionName);
		String def = Pattern.quote("_FRAPPE_" + functionName.toUpperCase(Locale.ROOT));
		Pattern pattern = Pattern.compile("(?<=#ifdef " + def + ")(?!._FRAPPE_)(.*" + fun + "[\\w\\t\\n(),=+-; ]+)(?=#endif)", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(shader);

		if (!matcher.find()) {
			throw new NullPointerException("Mocha failed to find " + functionName + " in its template shader");
		}

		return matcher.group();
	}

	private static String substituteFunction(String shader, String functionName, String functions, String calls, String varName) {
		String fun = Pattern.quote("_frappe_" + functionName);
		String def = Pattern.quote("_FRAPPE_" + functionName.toUpperCase(Locale.ROOT));
		String varNameQ = Pattern.quote(varName);
		return shader
				.replaceFirst("(?<=#ifdef " + def + "\n)" + def + "_FUNCTION_DEFS(?=\n#endif)", functions)
				.replaceFirst("(?<=#ifdef " + def + "[\\n\\t]{1,3})(?!._FRAPPE_)" + varNameQ + " = " + fun + ".*\\(.*\\);", calls);
	}

	private static Optional<Path> getShaderPath(Identifier shaderId, String extension) {
		String path = "assets/" + shaderId.getNamespace() + "/shaders/" + shaderId.getPath() + "." + extension;

		try {
			return FabricLoader.getInstance()
					.getModContainer(shaderId.getNamespace())
					.orElseThrow()
					.findPath(path);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException(path, e);
		}
	}
}
