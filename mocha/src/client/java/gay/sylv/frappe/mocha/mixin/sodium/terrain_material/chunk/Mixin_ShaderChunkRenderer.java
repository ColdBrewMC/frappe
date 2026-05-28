/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk;

import java.util.function.Function;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.gl.shader.GlProgram;
import net.caffeinemc.mods.sodium.client.gl.shader.ShaderConstants;
import net.caffeinemc.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkFogMode;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import gay.sylv.frappe.mocha.impl.sodium.vertex.format.MochaChunkShaderBindingPoints;

@Mixin(ShaderChunkRenderer.class)
public abstract class Mixin_ShaderChunkRenderer {
	@WrapOperation(method = "createShaderConstants", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gl/shader/ShaderConstants;builder()Lnet/caffeinemc/mods/sodium/client/gl/shader/ShaderConstants$Builder;"))
	private static ShaderConstants.Builder appendMochaShaderConstants(
			Operation<ShaderConstants.Builder> original,
			@Local(argsOnly = true, name = "options") ChunkShaderOptions options
	) {
		ShaderConstants.Builder builder = original.call();

		for (String key : options.pass().getPipeline().getShaderDefines().flags()) {
			if (key.startsWith("_FRP_") || key.startsWith("_FRAPPE_")) {
				builder.add(key);
			}
		}

		return builder;
	}

	@WrapOperation(method = "createShader", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gl/shader/GlProgram$Builder;link(Ljava/util/function/Function;)Lnet/caffeinemc/mods/sodium/client/gl/shader/GlProgram;"))
	private <U extends ChunkShaderInterface> GlProgram<U> modifyShader(
			GlProgram.Builder instance,
			Function<ShaderBindingContext, U> factory,
			Operation<GlProgram<U>> original,
			@Local(name = "constants") ShaderConstants constants
	) {
		instance
				.bindAttribute("a_FrappeUV", MochaChunkShaderBindingPoints.FRAPPE_UV);

		return original.call(instance, factory);
	}
}
