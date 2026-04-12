/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import java.util.Objects;

import com.mojang.blaze3d.textures.GpuSampler;
import net.caffeinemc.mods.sodium.client.gl.buffer.GlBuffer;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformBlock;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat2v;
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

import gay.sylv.frappe.mocha.impl.sodium.Ext_ChunkShaderInterface;

@Mixin(DefaultShaderInterface.class)
public abstract class Mixin_DefaultShaderInterface implements Ext_ChunkShaderInterface {
	@Unique
	private @Nullable GlUniformFloat2v uniformTextureSize;
	@Unique
	private @Nullable GlUniformBlock uniformMochaData;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/ShaderBindingContext;bindUniformBlock(Ljava/lang/String;I)Lnet/caffeinemc/mods/sodium/client/gl/shader/uniform/GlUniformBlock;"))
	private void onInit(
			ShaderBindingContext context,
			ChunkShaderOptions options,
			CallbackInfo ci
	) {
		this.uniformTextureSize = context.bindUniformOptional("u_FrappeCompatTextureSize", GlUniformFloat2v::new);
		this.uniformMochaData = context.bindUniformBlock("MochaData", 1);
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
	}

	@Override
	public void mocha$setMeshMaterials(GlBuffer buffer) {
		Objects.requireNonNull(this.uniformMochaData, "Mocha data couldn't be bound to Sodium shader").bindBuffer(buffer);
	}
}
