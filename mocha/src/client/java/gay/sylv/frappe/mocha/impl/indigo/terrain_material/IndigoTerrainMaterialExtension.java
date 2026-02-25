/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.Mocha.modId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialExtension;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;

public final class IndigoTerrainMaterialExtension implements TerrainMaterialExtension, IndigoRendererExtension {
	public static Map<RenderPipeline, RenderPipeline> VANILLA_2_MOCHA_TERRAIN_PIPELINES = Map.of();
	private static final Logger LOGGER = LoggerFactory.getLogger("Mocha/Indigo/frappe-ext-terrain-material");
	public static String mochaFragmentShader = "";
	// this VF is 32 bytes
	public static final VertexFormat MATERIAL_BLOCK = VertexFormat.builder()
			.add("Position", VertexFormatElement.POSITION)
			.add("Color", VertexFormatElement.COLOR)
			.add("UV0", VertexFormatElement.UV0)
			.add("UV2", VertexFormatElement.UV2)
			.add("UV1", VertexFormatElement.UV1) // use an extra 4 bytes
			.build();

	@Override
	public TerrainMaterial createChunkLayer(
			Identifier shaderId,
			String label
	) {
		return new IndigoTerrainMaterial(shaderId, label);
	}

	@Override
	public void registerMaterialImpl(TerrainMaterial material) {
		if (!VANILLA_2_MOCHA_TERRAIN_PIPELINES.isEmpty()) {
			throw new IllegalStateException("TerrainMaterial registration must occur in ClientModInitializer.");
		}

		int index = MochaIndigoEncodingFormat.terrainMaterialCount;
		MochaIndigoEncodingFormat.TERRAIN_MATERIAL_2_INDEX.put(material, index);
		MochaIndigoEncodingFormat.TERRAIN_MATERIALS[index] = material;
		MochaIndigoEncodingFormat.terrainMaterialCount++;
	}

	// This is cursed as fuck, but it lets us do cool things:tm:
	// https://regexlicensing.com
	public static void resolveMaterials() {
		if (!VANILLA_2_MOCHA_TERRAIN_PIPELINES.isEmpty()) {
			return;
		}

		RenderPipeline.Builder solid = RenderPipeline.builder(RenderPipelines.TERRAIN_SNIPPET)
				.withLocation("pipeline/solid_terrain");
		RenderPipeline.Builder cutout = RenderPipeline.builder(RenderPipelines.TERRAIN_SNIPPET)
				.withLocation("pipeline/cutout_terrain")
				.withShaderDefine("ALPHA_CUTOUT", 0.5f);
		RenderPipeline.Builder translucent = RenderPipeline.builder(RenderPipelines.TERRAIN_SNIPPET)
				.withLocation("pipeline/translucent_terrain")
				.withBlend(BlendFunction.TRANSLUCENT)
				.withShaderDefine("ALPHA_CUTOUT", 0.01f);
		List<RenderPipeline.Builder> builders = List.of(solid, cutout, translucent);

		// Load Mocha's fragment shader so we can modify it.
		try {
			mochaFragmentShader = Files.readString(getShaderPath(modId("include/fragment"), "glsl").orElseThrow());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String preFragmentTemplate = findFunction("frappe_pre_fragment");
		boolean preFragment = false;
		StringBuilder preFragmentFunctions = new StringBuilder();
		StringBuilder preFragmentBuilder = new StringBuilder();

		for (int i = 0; i < MochaIndigoEncodingFormat.terrainMaterialCount; i++) {
			TerrainMaterial material = MochaIndigoEncodingFormat.TERRAIN_MATERIALS[i];
			Identifier shaderId = material.shaderId();
			Optional<Path> fragmentShaderPath = getShaderPath(shaderId, "fsh");

			try {
				if (fragmentShaderPath.isPresent()) {
					String shader = Files.readString(fragmentShaderPath.get());
					shader = shader
							.replaceFirst("#version [0-9]{3}", "")
							.replaceFirst("(?<=vec4 )frappe_pre_fragment(?=\\()", "_frappe_pre_fragment_" + i);

					boolean frappePreFragment = shader.contains("_frappe_pre_fragment_");

					if (frappePreFragment) {
						preFragment = true;
						preFragmentBuilder.append(preFragmentTemplate
								.replaceAll("_FRAPPE_MATERIAL_ID", Integer.toString(i)));
						preFragmentFunctions.append(shader);
					}

					for (RenderPipeline.Builder builder : builders) {
						builder.withShaderDefine("_FRAPPE_FRAGMENT");

						if (frappePreFragment) {
							builder.withShaderDefine("_FRAPPE_PRE_FRAGMENT");
						}
					}
				}
			} catch (IOException e) {
				LOGGER.error("An error occurred while parsing terrain material shaders", e);
			}
		}

		if (preFragment) {
			mochaFragmentShader = mochaFragmentShader
					.replaceFirst("(?<=#ifdef _FRAPPE_PRE_FRAGMENT\n)_FRAPPE_FUNCTION_DEFS(?=\n#endif)", preFragmentFunctions.toString())
					.replaceFirst("(?<=#ifdef _FRAPPE_PRE_FRAGMENT[\\n\\t]{1,3})(?!._FRAPPE)color = _frappe_pre_fragment.*\\(.*\\);", preFragmentBuilder.toString());
		}

		// Defaults
		for (RenderPipeline.Builder builder : builders) {
			builder.withVertexFormat(MATERIAL_BLOCK, VertexFormat.Mode.QUADS);
		}

		VANILLA_2_MOCHA_TERRAIN_PIPELINES = Map.of(
				RenderPipelines.SOLID_TERRAIN, solid.build(),
				RenderPipelines.CUTOUT_TERRAIN, cutout.build(),
				RenderPipelines.TRANSLUCENT_TERRAIN, translucent.build()
		);
	}

	private static String findFunction(String functionName) {
		String fun = Pattern.quote("_" + functionName);
		String def = Pattern.quote("_" + functionName.toUpperCase(Locale.ROOT));
		Pattern pattern = Pattern.compile("(?<=#ifdef " + def + ")(?!._FRAPPE_)(.*" + fun + ".*)(?=#endif)", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(mochaFragmentShader);

		if (!matcher.find()) {
			throw new NullPointerException("Mocha failed to find " + functionName + " in its template shader");
		}

		return matcher.group();
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
