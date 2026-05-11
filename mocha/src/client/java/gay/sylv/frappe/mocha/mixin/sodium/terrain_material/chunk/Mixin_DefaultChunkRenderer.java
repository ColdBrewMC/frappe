/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.textures.GpuSampler;
import net.caffeinemc.mods.sodium.client.gl.device.CommandList;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.lists.ChunkRenderListIterable;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.viewport.CameraTransform;
import net.caffeinemc.mods.sodium.client.util.FogParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gay.sylv.frappe.mocha.impl.sodium.Ext_ChunkShaderInterface;
import gay.sylv.frappe.mocha.impl.sodium.Ext_DeviceResources;

@Mixin(DefaultChunkRenderer.class)
public abstract class Mixin_DefaultChunkRenderer {
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/DefaultChunkRenderer;setModelMatrixUniforms(Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/ChunkShaderInterface;Lnet/caffeinemc/mods/sodium/client/render/chunk/region/RenderRegion;Lnet/caffeinemc/mods/sodium/client/render/viewport/CameraTransform;Lnet/caffeinemc/mods/sodium/client/gl/buffer/GlBuffer;)V"))
	private void setMochaData(
			ChunkRenderMatrices matrices,
			CommandList commandList,
			ChunkRenderListIterable renderLists,
			TerrainRenderPass renderPass,
			CameraTransform camera,
			FogParameters parameters,
			boolean indexedRenderingEnabled,
			GpuSampler terrainSampler,
			CallbackInfo ci,
			@Local(name = "shader") ChunkShaderInterface shader,
			@Local(name = "region") RenderRegion region
	) {
		Ext_DeviceResources resources = ((Ext_DeviceResources) region.getResources()).mocha$prepareMeshMaterials(commandList);
		((Ext_ChunkShaderInterface) shader).mocha$bindMeshMaterials(resources.mocha$getSamplerMaterialInfo(), resources.mocha$getTextureMaterialInfo());
	}
}
