/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.Mocha.modId;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.CUTOUT_LAYERS;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_CUTOUT;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_CUTOUT_SNIPPET;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_CUTOUT_WIREFRAME;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_LAYERS;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_SOLID;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_SOLID_SNIPPET;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_SOLID_WIREFRAME;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.SOLID_LAYERS;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.VANILLA_2_MOCHA_TERRAIN_PIPELINES;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.VANILLA_2_SIMPLE_MOCHA_TERRAIN_PIPELINES;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.PolygonMode;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.parameters.AlphaCutoffParameter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;
import gay.sylv.frappe.mocha.impl.indigo.terrain_material.Ext_ChunkSectionLayer;
import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;
import gay.sylv.frappe.mocha.impl.sodium.MochaSodiumMaterials;

// It is impossible to make this compatible, so we enum extend.
// For simplicity's sake, we assume Mocha is special.
// Mocha's mixins can be disabled, so this should be fine.
// If you are reading this and need to enum extend, you are
// likely doing something wrong.
@Mixin(ChunkSectionLayer.class)
public abstract class Mixin_ChunkSectionLayer implements Ext_ChunkSectionLayer {
	@Unique
	private @Nullable TerrainMaterial associatedMaterial;

	@SuppressWarnings("NotNullFieldNotInitialized") // always initialized
	@Unique
	private RenderPipeline wireframePipeline;

	@Unique
	private static int ordinalOffset;

	@Unique
	private boolean fromMocha = false;

	@Mutable
	@Shadow
	@Final
	private static ChunkSectionLayer[] $VALUES;

	@Shadow
	@Final
	public static ChunkSectionLayer SOLID;

	@Shadow
	@Final
	public static ChunkSectionLayer CUTOUT;

	@Override
	public @Nullable TerrainMaterial mocha$getAssociatedMaterial() {
		return associatedMaterial;
	}

	@Override
	public void mocha$setAssociatedMaterial(TerrainMaterial material) {
		associatedMaterial = material;
	}

	@Override
	public RenderPipeline mocha$getWireframePipeline() {
		return this.wireframePipeline;
	}

	@Override
	public void mocha$setWireframePipeline(RenderPipeline pipeline) {
		this.wireframePipeline = pipeline;
	}

	@Override
	public boolean mocha$isFromMocha() {
		return this.fromMocha;
	}

	@Override
	public void mocha$setFromMocha() {
		this.fromMocha = true;
	}

	@SuppressWarnings({"NameDoesntMatchTargetClass", "LocalMayUseName"}) // Local name in <init> is autogenerated
	@Definition(id = "pipeline", local = @Local(type = RenderPipeline.class, argsOnly = true))
	@Expression("pipeline")
	@ModifyExpressionValue(method = "<init>", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private static RenderPipeline overridePipeline(RenderPipeline original, @Local(argsOnly = true) String name) {
		if (name.equals("SOLID") || name.equals("CUTOUT") || name.equals("TRANSLUCENT")) {
			IndigoTerrainMaterialExtension.resolveMaterials();
			RenderPipeline mochaPipeline = VANILLA_2_SIMPLE_MOCHA_TERRAIN_PIPELINES.get(original);
			return mochaPipeline != null ? mochaPipeline : original;
		} else {
			return original;
		}
	}

	@Definition(id = "ordinal", local = @Local(type = int.class, argsOnly = true, ordinal = 0))
	@Expression("ordinal")
	@ModifyExpressionValue(method = "<init>", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private static int countOrdinals(int ordinal) {
		ordinalOffset++;
		return ordinal;
	}

	//CHECKSTYLE.OFF: MatchXpath
	@SuppressWarnings("CheckStyle") // it thinks we need @Unique here LOL
	@Invoker(value = "<init>")
	private static ChunkSectionLayer init(
			String name,
			int ordinal,
			RenderPipeline pipeline,
			int bufferSize,
			boolean translucent
	) {
		throw new UnsupportedOperationException("@Invoker in Mixin");
	}

	//CHECKSTYLE.ON: MatchXpath

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void onClinit(CallbackInfo ci) {
		clinit();
	}

	@Unique
	private static void clinit() {
		IndigoTerrainMaterialExtension.resolveMaterials();
		RenderPipeline solid = VANILLA_2_MOCHA_TERRAIN_PIPELINES.get(RenderPipelines.SOLID_TERRAIN);
		RenderPipeline cutout = VANILLA_2_MOCHA_TERRAIN_PIPELINES.get(RenderPipelines.CUTOUT_TERRAIN);
		List<ChunkSectionLayer> layers = new ArrayList<>();
		MOCHA_SOLID = init(
				"MOCHA_SOLID",
				ordinalOffset,
				solid,
				SOLID.bufferSize(),
				SOLID.translucent()
		);
		MOCHA_CUTOUT = init(
				"MOCHA_CUTOUT",
				ordinalOffset,
				cutout,
				CUTOUT.bufferSize(),
				CUTOUT.translucent()
		);
		MOCHA_SOLID.mocha$setWireframePipeline(MOCHA_SOLID_WIREFRAME);
		MOCHA_CUTOUT.mocha$setWireframePipeline(MOCHA_CUTOUT_WIREFRAME);
		MOCHA_SOLID.mocha$setFromMocha();
		MOCHA_CUTOUT.mocha$setFromMocha();
		SOLID_LAYERS.add(MOCHA_SOLID);
		CUTOUT_LAYERS.add(MOCHA_CUTOUT);
		layers.add(MOCHA_SOLID);
		layers.add(MOCHA_CUTOUT);

		// Isolate terrain materials may have their own ChunkSectionLayers.
		// Let's compute those now.
		for (int i = 0; i < MochaIndigoEncodingFormat.terrainMaterialCount; i++) {
			IndigoTerrainMaterial material =
					(IndigoTerrainMaterial) MochaIndigoEncodingFormat.TERRAIN_MATERIALS[i];

			if (!material.complexity().equals(TerrainMaterial.Complexity.ISOLATE)) {
				continue;
			}

			if (material.renderPipelineModifier() != null) {
				String random = RandomStringUtils.secure().nextAlphabetic(8).toLowerCase(Locale.ROOT);
				String materialName = random + "_" + material.label().replaceAll("[^a-z0-9/._-]", "_");
				RenderPipeline.Builder solidBuilder = RenderPipeline.builder(MOCHA_SOLID_SNIPPET);
				RenderPipeline.Builder cutoutBuilder = RenderPipeline.builder(MOCHA_CUTOUT_SNIPPET);
				solidBuilder.withShaderDefine("_FRAPPE_ISOLATE_MATERIAL");
				cutoutBuilder.withShaderDefine("_FRAPPE_ISOLATE_MATERIAL");
				solidBuilder = material.renderPipelineModifier().apply(solidBuilder);
				cutoutBuilder = material.renderPipelineModifier().apply(cutoutBuilder);
				ChunkSectionLayer solidLayer = init(
						"MOCHA_SOLID_" + materialName,
						ordinalOffset,
						solidBuilder
								.withLocation(modId("solid_" + materialName))
								.build(),
						MOCHA_SOLID.bufferSize(),
						false
				);
				ChunkSectionLayer cutoutLayer = init(
						"MOCHA_CUTOUT_" + materialName,
						ordinalOffset,
						cutoutBuilder
								.withLocation(modId("cutout_" + materialName))
								.build(),
						MOCHA_CUTOUT.bufferSize(),
						false
				);
				RenderPipeline.Builder wireframePipeline = RenderPipeline.builder(MOCHA_SOLID_SNIPPET)
						.withPolygonMode(PolygonMode.WIREFRAME);
				RenderPipeline.Builder wireframeCutoutPipeline = RenderPipeline.builder(MOCHA_CUTOUT_SNIPPET)
						.withPolygonMode(PolygonMode.WIREFRAME);
				wireframePipeline.withShaderDefine("_FRAPPE_ISOLATE_MATERIAL");
				wireframeCutoutPipeline.withShaderDefine("_FRAPPE_ISOLATE_MATERIAL");
				wireframePipeline = material.renderPipelineModifier().apply(wireframePipeline);
				wireframeCutoutPipeline = material.renderPipelineModifier().apply(wireframeCutoutPipeline);
				solidLayer.mocha$setWireframePipeline(wireframePipeline
								.withLocation(modId("wireframe_solid_" + materialName))
								.build());
				cutoutLayer.mocha$setWireframePipeline(wireframeCutoutPipeline
						.withLocation(modId("wireframe_cutout_" + materialName))
						.build());
				solidLayer.mocha$setAssociatedMaterial(material);
				cutoutLayer.mocha$setAssociatedMaterial(material);
				solidLayer.mocha$setFromMocha();
				cutoutLayer.mocha$setFromMocha();
				SOLID_LAYERS.add(solidLayer);
				CUTOUT_LAYERS.add(cutoutLayer);
				layers.add(solidLayer);
				layers.add(cutoutLayer);
			}
		}

		$VALUES = ArrayUtils.addAll($VALUES, layers.toArray(ChunkSectionLayer[]::new));

		MOCHA_LAYERS.addAll(SOLID_LAYERS);
		MOCHA_LAYERS.addAll(CUTOUT_LAYERS);

		if (FabricLoader.getInstance().isModLoaded("sodium")) {
			for (ChunkSectionLayer layer : MOCHA_LAYERS) {
				boolean hasAlphaCutout = layer.pipeline().getShaderDefines().values().containsKey("ALPHA_CUTOUT");
				AlphaCutoffParameter alphaCutoffParameter = layer.translucent() ? AlphaCutoffParameter.TINY : hasAlphaCutout ? AlphaCutoffParameter.HALF : AlphaCutoffParameter.ZERO;
				TerrainRenderPass pass = new TerrainRenderPass(layer, layer.translucent(), hasAlphaCutout);
				MochaSodiumMaterials.RENDER_PASSES.add(pass);
				MochaSodiumMaterials.MOCHA_MATERIALS.put(layer, new Material(pass, alphaCutoffParameter, true));
			}
		}
	}
}
