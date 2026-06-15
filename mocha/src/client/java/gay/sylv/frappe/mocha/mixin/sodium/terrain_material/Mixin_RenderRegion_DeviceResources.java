/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import java.util.Map;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import gay.sylv.frappe.api.ext.render_pipeline.value.BlockAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;
import gay.sylv.frappe.mocha.impl.sodium.vertex.format.ComplexChunkVertex;

@Mixin(RenderRegion.DeviceResources.class)
public abstract class Mixin_RenderRegion_DeviceResources {
	@Unique
	private @Nullable Map<VertexAttribute<?>, GpuTextureView> vertexAttributeTextureViews;
	@Unique
	private @Nullable Map<VertexAttribute<?>, GpuSampler> vertexAttributeSamplers;
	@Unique
	private @Nullable Map<QuadAttribute<?>, GpuTextureView> quadAttributeTextureViews;
	@Unique
	private @Nullable Map<QuadAttribute<?>, GpuSampler> quadAttributeSamplers;
	@Unique
	private @Nullable Map<BlockAttribute<?>, GpuTextureView> blockAttributeTextureViews;
	@Unique
	private @Nullable Map<BlockAttribute<?>, GpuSampler> blockAttributeSamplers;

	@Definition(
			id = "COMPACT",
			field = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkMeshFormats;COMPACT:Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexType;"
	)
	@Expression("COMPACT")
	@ModifyExpressionValue(method = "<init>", at = @At("MIXINEXTRAS:EXPRESSION"))
	private ChunkVertexType useExtendedFormat(ChunkVertexType original) {
		return ComplexChunkVertex.INSTANCE;
	}
}
