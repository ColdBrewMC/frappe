/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import com.mojang.blaze3d.textures.GpuSampler;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat2v;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat3v;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.DefaultShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.util.FogParameters;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.OptionsRenderState;

@Mixin(DefaultShaderInterface.class)
public abstract class Mixin_DefaultShaderInterface {
	@Unique
	private @Nullable GlUniformFloat2v uniformTextureSize;
	@Unique
	private @Nullable GlUniformFloat3v uniformCameraOffset;
	@Unique
	private @Nullable GlUniformFloat uniformGlintAlpha;
	@Unique
	private @Nullable GlUniformFloat uniformGlintSpeed;
	@Unique
	private @Nullable GlUniformFloat uniformLevelTime;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(
			ShaderBindingContext context,
			ChunkShaderOptions options,
			CallbackInfo ci
	) {
		this.uniformTextureSize = context.bindUniformOptional("u_FrappeCompatTextureSize", GlUniformFloat2v::new);
		this.uniformGlintAlpha = context.bindUniformOptional("u_FrappeCompatGlintAlpha", GlUniformFloat::new);
		this.uniformGlintSpeed = context.bindUniformOptional("u_FrappeCompatGlintSpeed", GlUniformFloat::new);
		this.uniformLevelTime = context.bindUniformOptional("u_FrappeCompatLevelTime", GlUniformFloat::new);
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

		GameRenderState gameRenderState = Minecraft.getInstance().gameRenderer.getGameRenderState();
		OptionsRenderState optionsRenderState = gameRenderState.optionsRenderState;

		if (this.uniformGlintAlpha != null) {
			this.uniformGlintAlpha.set((float) optionsRenderState.glintStrength);
		}

		if (this.uniformGlintSpeed != null) {
			this.uniformGlintSpeed.set((float) optionsRenderState.glintSpeed);
		}

		if (this.uniformLevelTime != null) {
			long gameTime = gameRenderState.levelRenderState.gameTime;
			DeltaTracker deltaTracker = Minecraft.getInstance().getDeltaTracker();
			this.uniformLevelTime.set(((float) (gameTime % 24000L) + deltaTracker.getGameTimeDeltaPartialTick(false)) / 24000.0F);
		}
	}
}
