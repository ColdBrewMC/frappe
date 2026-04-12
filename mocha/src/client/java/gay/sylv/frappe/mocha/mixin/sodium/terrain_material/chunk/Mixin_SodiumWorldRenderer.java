/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import com.mojang.blaze3d.textures.GpuSampler;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.util.FogParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;

import gay.sylv.frappe.mocha.impl.sodium.MochaSodiumMaterials;

@Mixin(SodiumWorldRenderer.class)
public abstract class Mixin_SodiumWorldRenderer {
	@Shadow
	private RenderSectionManager renderSectionManager;

	@Shadow
	private FogParameters lastFogParameters;

	@Inject(method = "drawChunkLayer", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/RenderSectionManager;renderLayer(Lnet/caffeinemc/mods/sodium/client/render/chunk/ChunkRenderMatrices;Lnet/caffeinemc/mods/sodium/client/render/chunk/terrain/TerrainRenderPass;DDDLnet/caffeinemc/mods/sodium/client/util/FogParameters;Lcom/mojang/blaze3d/textures/GpuSampler;)V", ordinal = 1, shift = At.Shift.AFTER))
	private void drawMochaComplexLayers(
			ChunkSectionLayerGroup group,
			ChunkRenderMatrices matrices,
			double x,
			double y,
			double z,
			GpuSampler terrainSampler,
			CallbackInfo ci
	) {
		for (TerrainRenderPass pass : MochaSodiumMaterials.RENDER_PASSES) {
			this.renderSectionManager.renderLayer(matrices, pass, x, y, z, this.lastFogParameters, terrainSampler);
		}
	}
}
