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
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.PolygonMode;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.frappe.api.base.extension.RendererInfo;
import gay.sylv.frappe.api.ext.material.MaterialExtension;
import gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline;
import gay.sylv.frappe.api.ext.render_pipeline.RenderPipelineExtension;
import gay.sylv.frappe.api.ext.render_pipeline.shader.PipelineStage;
import gay.sylv.frappe.api.ext.render_pipeline.shader.ShaderFormat;
import gay.sylv.frappe.api.ext.render_pipeline.shader.TransformOptions;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialExtension;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialRegistryEntrypoint;
import gay.sylv.frappe.mocha.impl.indigo.IndiumRendererExtension;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;
import gay.sylv.frappe.mocha.impl.indigo.vertex.format.MochaVertexFormats;

public final class IndigoTerrainMaterialExtension implements TerrainMaterialExtension, IndiumRendererExtension {
	public static Map<RenderPipeline, RenderPipeline> VANILLA_2_MOCHA_TERRAIN_PIPELINES = Map.of();
	public static Map<ChunkSectionLayer, RenderPipeline> CSL_2_MOCHA_TERRAIN_PIPELINES = Map.of();
	public static Map<RenderPipeline, RenderPipeline> VANILLA_2_SIMPLE_MOCHA_TERRAIN_PIPELINES = Map.of();
	private static final Logger LOGGER = LoggerFactory.getLogger("Mocha/Indigo/frappe-ext-terrain-material");
	public static String mochaFragmentShader = "";
	public static String mochaVertexShader = "";
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
	public static List<ChunkSectionLayer> MOCHA_LAYERS = new ArrayList<>();

	@Override
	public TerrainMaterial createChunkLayer(
			Identifier shaderId,
			String label,
			TerrainMaterial.Complexity complexity
	) {
		return new IndigoTerrainMaterial(
				shaderId,
				label,
				complexity,
				null,
				null,
				null,
				null,
				null
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

	public static void resolveMaterials(boolean reload) {
		if (!VANILLA_2_MOCHA_TERRAIN_PIPELINES.isEmpty() && !reload) {
			return;
		}

		Renderer.get(); // Ensure Renderer is ready by this point.

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
				.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
				.withShaderDefine("ALPHA_CUTOUT", 0.01f);
		List<RenderPipeline.Builder> builders = List.of(solid, cutout, translucent);

		// Load Mocha's fragment/vertex shader so we can modify it.
		try {
			if (FabricLoader.getInstance().isModLoaded("sodium")) {
				mochaFragmentShader = Files.readString(getShaderPath(modId("blocks/block_layer_opaque"), "fsh").orElseThrow());
				mochaVertexShader = Files.readString(getShaderPath(modId("blocks/block_layer_opaque"), "vsh").orElseThrow());
			} else {
				mochaFragmentShader = Files.readString(getShaderPath(modId("core/terrain"), "fsh", "mocha").orElseThrow());
				mochaVertexShader = Files.readString(getShaderPath(modId("core/terrain"), "vsh", "mocha").orElseThrow());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		mochaFragmentShader = fuckOffWindows(mochaFragmentShader);
		mochaVertexShader = fuckOffWindows(mochaVertexShader);

		List<ShaderFormat> shaderFormats = new ArrayList<>(List.of(
				ShaderFormat.getExtensionFormats("frp-base", RenderPipelineExtension.class),
				ShaderFormat.getExtensionFormats("frp-material", MaterialExtension.class),
				ShaderFormat.getExtensionFormats("frp-terrain-material", TerrainMaterialExtension.class)
		));

		for (FrappeRenderPipeline pipeline : FrappeRenderPipeline.getAllPipelines()) {
			shaderFormats.add(pipeline.shaderFormat());
		}

		ShaderFormat shaderFormat = ShaderFormat.union(shaderFormats.toArray(ShaderFormat[]::new));

		Map<String, String> vertexShaderSources = new HashMap<>();
		Map<String, String> fragmentShaderSources = new HashMap<>();
		Map<String, Map<String, String>> perPipelineDefines = new HashMap<>();

		for (int i = 0; i < MochaIndigoEncodingFormat.terrainMaterialCount; i++) {
			Identifier shaderId = MochaIndigoEncodingFormat.TERRAIN_MATERIALS[i].shaderId();
			String id = shaderId + "_" + i;
			Optional<Path> vertexPath = getShaderPath(shaderId, "vsh");
			Optional<Path> fragmentPath = getShaderPath(shaderId, "fsh");

			perPipelineDefines.computeIfAbsent(id, _ -> new HashMap<>())
					.put("FRP_MATERIAL_ID", i + "u");

			if (vertexPath.isPresent()) {
				try {
					vertexShaderSources.put(id, Files.readString(vertexPath.get()));
				} catch (NoSuchFileException _) {
					// ignored
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			if (fragmentPath.isPresent()) {
				try {
					fragmentShaderSources.put(id, Files.readString(fragmentPath.get()));
				} catch (NoSuchFileException _) {
					// ignored
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		TransformOptions transformOptions = TransformOptions.of(RendererInfo.getModId()
				.equals("fabric-renderer-indigo"));
		mochaVertexShader = shaderFormat.transformAndCombineShaders(PipelineStage.VERTEX, mochaVertexShader, vertexShaderSources, perPipelineDefines, transformOptions);
		mochaFragmentShader = shaderFormat.transformAndCombineShaders(PipelineStage.FRAGMENT, mochaFragmentShader, fragmentShaderSources, perPipelineDefines, transformOptions);

		if (!FabricLoader.getInstance().isModLoaded("sodium")) {
			mochaVertexShader = mochaVertexShader.replace("#custom moj_import", "#moj_import");
			mochaFragmentShader = mochaFragmentShader.replace("#custom moj_import", "#moj_import").replace("ALPHA_CUTOUT;", """

#ifdef ALPHA_CUTOUT
if (frp_fragColor.a < ALPHA_CUTOUT) {
	discard;
}
#endif
""");

			IndigoPipelineUniform.INSTANCES.values().forEach(IndigoPipelineUniform::close);
			IndigoPipelineUniform.INSTANCES.clear();
		} else {
			mochaFragmentShader = mochaFragmentShader.replace("ALPHA_CUTOUT;", """

#ifdef USE_FRAGMENT_DISCARD
if (color.a < _material_alpha_cutoff(v_Material)) {
	discard;
}
#endif
""");
		}

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

	private static Optional<Path> getShaderPath(Identifier shaderId, String extension) {
		return getShaderPath(shaderId, extension, shaderId.getNamespace());
	}

	private static Optional<Path> getShaderPath(Identifier shaderId, String extension, String modId) {
		String path = "assets/" + shaderId.getNamespace() + "/shaders/" + shaderId.getPath() + "." + extension;

		try {
			return FabricLoader.getInstance()
					.getModContainer(modId)
					.orElseThrow()
					.findPath(path);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException(path, e);
		}
	}

	/// makes windows fuck off
	private static String fuckOffWindows(String infected) {
		Pattern weHateWindows = Pattern.compile("\\r\\n");
		Matcher windowsVaccine = weHateWindows.matcher(infected);
		return windowsVaccine.replaceAll("\n");
	}
}
