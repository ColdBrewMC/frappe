/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.textures.GpuSampler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.util.profiling.ProfilerFiller;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;

@Mixin(LevelRenderer.class)
public abstract class Mixin_LevelRenderer {
	@Shadow
	private @Nullable GpuSampler chunkLayerSampler;

	@Shadow
	private @Nullable SectionRenderDispatcher sectionRenderDispatcher;

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	@Final
	private ObjectArrayList<SectionRenderDispatcher.RenderSection> visibleSections;

	@Definition(id = "chunkSectionsToRender", local = @Local(type = ChunkSectionsToRender.class, argsOnly = true))
	@Definition(
			id = "renderGroup",
			method = "Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;renderGroup(Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;Lcom/mojang/blaze3d/textures/GpuSampler;)V"
	)
	@Definition(
			id = "OPAQUE",
			field = "Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;OPAQUE:Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;"
	)
	@Definition(
			id = "chunkLayerSampler",
			field = "Lnet/minecraft/client/renderer/LevelRenderer;chunkLayerSampler:Lcom/mojang/blaze3d/textures/GpuSampler;"
	)
	@Expression("chunkSectionsToRender.renderGroup(OPAQUE, this.chunkLayerSampler)")
	@Inject(method = "lambda$addMainPass$0", at = @At("MIXINEXTRAS:EXPRESSION"))
	private void addMochaOpaquePass(
			GpuBufferSlice terrainFog,
			LevelRenderState levelRenderState,
			ProfilerFiller profiler,
			ChunkSectionsToRender chunkSectionsToRender,
			ResourceHandle<RenderTarget> entityOutlineTarget,
			ResourceHandle<RenderTarget> translucentTarget,
			ResourceHandle<RenderTarget> mainTarget,
			ResourceHandle<RenderTarget> itemEntityTarget,
			ResourceHandle<RenderTarget> particleTarget,
			boolean renderOutline,
			Matrix4f modelViewMatrix,
			CallbackInfo ci
	) {
		//noinspection DataFlowIssue // No issue at Mixin target
		chunkSectionsToRender.renderGroup(IndigoTerrainMaterialExtension.MOCHA_OPAQUE_SOLID, this.chunkLayerSampler);
	}

	@Definition(id = "chunkSectionsToRender", local = @Local(type = ChunkSectionsToRender.class, argsOnly = true))
	@Definition(
			id = "renderGroup",
			method = "Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;renderGroup(Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;Lcom/mojang/blaze3d/textures/GpuSampler;)V"
	)
	@Definition(
			id = "OPAQUE",
			field = "Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;OPAQUE:Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;"
	)
	@Definition(
			id = "chunkLayerSampler",
			field = "Lnet/minecraft/client/renderer/LevelRenderer;chunkLayerSampler:Lcom/mojang/blaze3d/textures/GpuSampler;"
	)
	@Expression("chunkSectionsToRender.renderGroup(OPAQUE, this.chunkLayerSampler)")
	@Inject(method = "lambda$addMainPass$0", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
	private void addMochaOpaqueCutoutPass(
			GpuBufferSlice terrainFog,
			LevelRenderState levelRenderState,
			ProfilerFiller profiler,
			ChunkSectionsToRender chunkSectionsToRender,
			ResourceHandle<RenderTarget> entityOutlineTarget,
			ResourceHandle<RenderTarget> translucentTarget,
			ResourceHandle<RenderTarget> mainTarget,
			ResourceHandle<RenderTarget> itemEntityTarget,
			ResourceHandle<RenderTarget> particleTarget,
			boolean renderOutline,
			Matrix4f modelViewMatrix,
			CallbackInfo ci
	) {
		//noinspection DataFlowIssue // No issue at Mixin target
		chunkSectionsToRender.renderGroup(IndigoTerrainMaterialExtension.MOCHA_OPAQUE_CUTOUT, this.chunkLayerSampler);
	}
}
