/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import static gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline.DataType.FLOAT;
import static gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline.DataType.VEC2;
import static gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline.DataType.VEC3;
import static gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline.DataType.VEC4;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.textures.GpuSampler;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat2v;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat3v;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat4v;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.DefaultShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.util.FogParameters;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.GameRenderState;

import gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline;
import gay.sylv.frappe.api.ext.render_pipeline.FrappeRenderPipeline.DataType;

@Mixin(DefaultShaderInterface.class)
public abstract class Mixin_DefaultShaderInterface {
	@Unique
	private @Nullable GlUniformFloat2v uniformTextureSize;
	@Unique
	private @Nullable GlUniformFloat uniformLevelTime;
	@Unique
	private final Map<String, GlUniformFloat> floatUniforms = new HashMap<>();
	@Unique
	private final Map<String, GlUniformFloat2v> vec2Uniforms = new HashMap<>();
	@Unique
	private final Map<String, GlUniformFloat3v> vec3Uniforms = new HashMap<>();
	@Unique
	private final Map<String, GlUniformFloat4v> vec4Uniforms = new HashMap<>();
	@Unique
	private final Map<String, FrappeRenderPipeline.UniformGetter<?>> uniformGetters = new HashMap<>();

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(
			ShaderBindingContext context,
			ChunkShaderOptions options,
			CallbackInfo ci
	) {
		this.uniformTextureSize = context.bindUniformOptional("u_FrappeCompatTextureSize", GlUniformFloat2v::new);
		this.uniformLevelTime = context.bindUniformOptional("u_FrappeCompatLevelTime", GlUniformFloat::new);

		for (FrappeRenderPipeline pipeline : FrappeRenderPipeline.getAllPipelines()) {
			for (Map.Entry<String, DataType<?>> entry : pipeline.uniformDataTypes().entrySet()) {
				String identifier = entry.getKey();
				DataType<?> type = entry.getValue();

				if (type.equals(FLOAT)) {
					GlUniformFloat uniformFloat = context.bindUniformOptional(identifier, GlUniformFloat::new);

					if (uniformFloat == null) {
						continue;
					}

					this.floatUniforms.put(identifier, uniformFloat);
				} else if (type.equals(VEC2)) {
					GlUniformFloat2v uniformFloat = context.bindUniformOptional(identifier, GlUniformFloat2v::new);

					if (uniformFloat == null) {
						continue;
					}

					this.vec2Uniforms.put(identifier, uniformFloat);
				} else if (type.equals(VEC3)) {
					GlUniformFloat3v uniformFloat = context.bindUniformOptional(identifier, GlUniformFloat3v::new);

					if (uniformFloat == null) {
						continue;
					}

					this.vec3Uniforms.put(identifier, uniformFloat);
				} else if (type.equals(VEC4)) {
					GlUniformFloat4v uniformFloat = context.bindUniformOptional(identifier, GlUniformFloat4v::new);

					if (uniformFloat == null) {
						continue;
					}

					this.vec4Uniforms.put(identifier, uniformFloat);
				}

				this.uniformGetters.put(identifier, pipeline.uniformGetters().get(identifier));
			}
		}
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

		for (Map.Entry<String, GlUniformFloat> entry : this.floatUniforms.entrySet()) {
			this.floatUniforms.get(entry.getKey()).set((Float) this.uniformGetters.get(entry.getKey()).getValue(gameRenderState));
		}

		for (Map.Entry<String, GlUniformFloat2v> entry : this.vec2Uniforms.entrySet()) {
			Vector2fc value = (Vector2fc) this.uniformGetters.get(entry.getKey()).getValue(gameRenderState);
			this.vec2Uniforms.get(entry.getKey()).set(value.x(), value.y());
		}

		for (Map.Entry<String, GlUniformFloat3v> entry : this.vec3Uniforms.entrySet()) {
			Vector3fc value = (Vector3fc) this.uniformGetters.get(entry.getKey()).getValue(gameRenderState);
			this.vec3Uniforms.get(entry.getKey()).set(value.x(), value.y(), value.z());
		}

		for (Map.Entry<String, GlUniformFloat4v> entry : this.vec4Uniforms.entrySet()) {
			Vector4fc value = (Vector4fc) this.uniformGetters.get(entry.getKey()).getValue(gameRenderState);
			this.vec4Uniforms.get(entry.getKey()).set(value.x(), value.y(), value.z(), value.w());
		}

		if (this.uniformLevelTime != null) {
			long gameTime = gameRenderState.levelRenderState.gameTime;
			DeltaTracker deltaTracker = Minecraft.getInstance().getDeltaTracker();
			this.uniformLevelTime.set(((float) (gameTime % 24000L) + deltaTracker.getGameTimeDeltaPartialTick(false)) / 24000.0F);
		}
	}
}
