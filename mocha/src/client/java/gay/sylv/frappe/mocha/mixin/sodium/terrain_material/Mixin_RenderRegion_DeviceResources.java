/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import gay.sylv.frappe.mocha.impl.sodium.vertex.format.ComplexChunkVertex;

@Mixin(RenderRegion.DeviceResources.class)
public abstract class Mixin_RenderRegion_DeviceResources {
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
