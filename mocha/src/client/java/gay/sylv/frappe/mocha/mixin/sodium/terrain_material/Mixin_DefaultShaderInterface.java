/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import java.util.Map;

import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat2v;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat3v;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformInt;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderTextureSlot;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.DefaultShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.util.FogParameters;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.phys.Vec3;

import gay.sylv.frappe.mocha.impl.sodium.Ext_ChunkShaderInterface;

@Mixin(DefaultShaderInterface.class)
public abstract class Mixin_DefaultShaderInterface implements Ext_ChunkShaderInterface {
	@Shadow
	@Final
	private Map<ChunkShaderTextureSlot, GlUniformInt> uniformTextures;

	@Shadow
	@Deprecated(forRemoval = true)
	protected abstract void bindTexture(
			ChunkShaderTextureSlot slot,
			GpuTextureView textureView,
			GpuSampler sampler
	);

	@Unique
	private @Nullable GlUniformFloat2v uniformTextureSize;
	@Unique
	private @Nullable GpuSampler samplerMaterialInfo;
	@Unique
	private @Nullable GpuTextureView textureMaterialInfo;
	@Unique
	private @Nullable GlUniformFloat3v uniformCameraOffset;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(
			ShaderBindingContext context,
			ChunkShaderOptions options,
			CallbackInfo ci
	) {
		ChunkShaderTextureSlot slot = ChunkShaderTextureSlot.valueOf("MOCHA_MATERIAL_INFO");
		this.uniformTextures.put(slot, context.bindUniform("u_MochaTex", GlUniformInt::new));
		this.uniformTextureSize = context.bindUniformOptional("u_FrappeCompatTextureSize", GlUniformFloat2v::new);
		this.uniformCameraOffset = context.bindUniformOptional("u_MochaCameraOffset", GlUniformFloat3v::new);
	}

	@Inject(method = "setupState", at = @At("RETURN"))
	private void setupMochaState(
			TerrainRenderPass pass,
			FogParameters parameters,
			GpuSampler terrainSampler,
			CallbackInfo ci
	) {
		if (this.uniformTextureSize != null) {
			Accessor_GpuTexture atlas = (Accessor_GpuTexture) pass.getAtlas().texture();
			this.uniformTextureSize.set(atlas.mocha$getWidth(), atlas.mocha$getHeight());
		}

		if (this.uniformCameraOffset != null) {
			GameRenderState gameRenderState = Minecraft.getInstance().gameRenderer.getGameRenderState();
			LevelRenderState levelRenderState = gameRenderState.levelRenderState;
			Vec3 position = levelRenderState.cameraRenderState.pos;
			this.uniformCameraOffset.set((float) position.x(), (float) position.y(), (float) position.z());
		}
	}

	@Override
	public void mocha$bindMeshMaterials(GpuSampler samplerMaterialInfo, GpuTextureView textureMaterialInfo) {
		if (samplerMaterialInfo.equals(this.samplerMaterialInfo) && textureMaterialInfo.equals(this.textureMaterialInfo)) {
			return;
		}

		this.bindTexture(ChunkShaderTextureSlot.valueOf("MOCHA_MATERIAL_INFO"), textureMaterialInfo, samplerMaterialInfo);
		this.samplerMaterialInfo = samplerMaterialInfo;
		this.textureMaterialInfo = textureMaterialInfo;
	}
}
