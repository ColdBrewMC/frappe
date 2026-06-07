/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.impl.CompactChunkVertex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import gay.sylv.frappe.mocha.impl.sodium.vertex.format.ComplexChunkVertex;

// fuck you, *uncompacts your vertex format*
// sorry iGPU users. works on my machine!
// jokes aside, it does work good enough on my shittier laptop, so we'll see.
// honestly i think we should just not change this but conditionally change if this or something else is used, and then not change it for simple contexts but do it for complex and *certainly* for isolate.
// hey why don't we do some vertex smuggling x3
// nah we're going with some block pulling as I'm calling it
// TODO: put complex and isolate materials on their own custom vertex formats
@Mixin(CompactChunkVertex.class)
public abstract class Mixin_CompactChunkVertex {
	@Unique
	private static final ScopedValue<Integer> MATERIAL_ID = ScopedValue.newInstance();

	// pack lowest 4 bits of material ID
	@WrapMethod(method = "lambda$getEncoder$0")
	private static long onEncode(
			long ptr,
			int materialBits,
			ChunkVertexEncoder.Vertex[] vertices,
			int section,
			Operation<Long> original
	) {
		return ScopedValue.where(MATERIAL_ID, (materialBits >> 4) & 0xFF)
				.call(() -> original.call(
						ptr,
						(materialBits & 0xF) | ((MATERIAL_ID.get() & 0xF) << 4),
						vertices,
						section
				));
	}

	@Definition(id = "mulRGB", method = "Lnet/caffeinemc/mods/sodium/api/util/ColorARGB;mulRGB(IF)I")
	@Definition(
			id = "color",
			field = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexEncoder$Vertex;color:I"
	)
	@Definition(
			id = "ao",
			field = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexEncoder$Vertex;ao:F"
	)
	@Expression("mulRGB(?.color, ?.ao)")
	@WrapOperation(method = "lambda$getEncoder$0", at = @At("MIXINEXTRAS:EXPRESSION"))
	private static int dontMultiplyAo(int color, float factor, Operation<Integer> original) {
		return color;
	}

	@Definition(id = "ptr", local = @Local(type = long.class, name = "ptr", argsOnly = true))
	@Expression("ptr = @(?)")
	@ModifyExpressionValue(method = "lambda$getEncoder$0", at = @At("MIXINEXTRAS:EXPRESSION"))
	private static long applyStrideOffset(long original) {
		return original + ComplexChunkVertex.STRIDE_OFFSET.orElse(0);
	}

	// pack lowest 2 bits of remaining 4 bits
	@WrapMethod(method = "packPositionLo")
	private static int packTerrainMaterialLo(int x, int y, int z, Operation<Integer> original) {
		return original.call(x, y, z) | (MATERIAL_ID.get() & (0b11 << 4)) << 30;
	}

	// pack highest 2 bits of remaining 4 bits
	@WrapMethod(method = "packPositionHi")
	private static int packTerrainMaterialHi(int x, int y, int z, Operation<Integer> original) {
		return original.call(x, y, z) | (MATERIAL_ID.get() & (0b11 << 6)) << 30;
	}
}
