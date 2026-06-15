/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.sodium.vertex.format;

import net.caffeinemc.mods.sodium.api.memory.MemoryIntrinsics;
import net.caffeinemc.mods.sodium.client.gl.attribute.GlVertexFormat;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.impl.CompactChunkVertex;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.impl.DefaultChunkMeshAttributes;

import net.minecraft.core.BlockPos;

import gay.sylv.frappe.mocha.impl.indigo.vertex.format.MochaVertexFormats;
import gay.sylv.frappe.mocha.mixin.sodium.terrain_material.chunk.Accessor_CompactChunkVertex;

/// An extension of [CompactChunkVertex] providing more vertex information for complex terrain materials.
public class ComplexChunkVertex extends CompactChunkVertex {
	public static final GlVertexFormat VERTEX_FORMAT = GlVertexFormat
			.builder(28)
			.addElement(DefaultChunkMeshAttributes.POSITION, 0, 0)
			.addElement(DefaultChunkMeshAttributes.COLOR, 1, 8)
			.addElement(DefaultChunkMeshAttributes.TEXTURE, 2, 12)
			.addElement(DefaultChunkMeshAttributes.LIGHT_MATERIAL_INDEX, 3, 16)
			.addElement(MochaChunkMeshAttributes.FRAPPE_UV, MochaChunkShaderBindingPoints.FRAPPE_UV, 20)
			.addElement(MochaChunkMeshAttributes.FRAPPE_CENTER_OFFSET, MochaChunkShaderBindingPoints.FRAPPE_CENTER_OFFSET, 24)
			.addElement(MochaChunkMeshAttributes.FRAPPE_AO, MochaChunkShaderBindingPoints.FRAPPE_AO, 27)
			.build();
	public static final ComplexChunkVertex INSTANCE = new ComplexChunkVertex();
	public static final ScopedValue<Integer> STRIDE_OFFSET = ScopedValue.newInstance();
	public static final ScopedValue<BlockPos> BLOCK_POS = ScopedValue.newInstance();
	private static final int STRIDE_DIFFERENCE = VERTEX_FORMAT.getStride() - CompactChunkVertex.VERTEX_FORMAT.getStride();
	private static final int VERTEX_STRIDE = VERTEX_FORMAT.getStride();
	private static final long POSITION_HORIZONTAL_MASK = 0x3FFFFFF;
	private static final long POSITION_Y_MASK = 0xFFF;

	@Override
	public GlVertexFormat getVertexFormat() {
		return VERTEX_FORMAT;
	}

	@Override
	public ChunkVertexEncoder getEncoder() {
		ChunkVertexEncoder encoder = super.getEncoder();
		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

		return (ptr, materialBits, vertices, section) -> {
			final long initialPtr = ptr;
			ScopedValue.where(STRIDE_OFFSET, STRIDE_DIFFERENCE)
					.call(() -> encoder.write(initialPtr, materialBits, vertices, section));

			// please excuse me Sodium gods for I have used your own tricks on you
			float frappeTexCentroidU = 0.0f;
			float frappeTexCentroidV = 0.0f;

			// accumulate UVs
			for (int i = 0; i < 4; i++) {
				if (vertices[i] instanceof ComplexVertex vertex) {
					frappeTexCentroidU += vertex.frappeU;
					frappeTexCentroidV += vertex.frappeV;
				}
			}

			// average UVs to get centers of textures
			frappeTexCentroidU *= 0.25f;
			frappeTexCentroidV *= 0.25f;

			for (int i = 0; i < 4; i++) {
				// FIXME: we tried just casting it without checking, but it throws a cast exception,
				//  so we have to check it with instanceof (expensive here).
				if (!(vertices[i] instanceof ComplexVertex vertex)) {
					MemoryIntrinsics.putInt(ptr + 20, 0);
					MemoryIntrinsics.putInt(ptr + 24, 0);
					MemoryIntrinsics.putByte(ptr + 27, MochaVertexFormats.packAo(vertices[i].ao));

					ptr += VERTEX_STRIDE;
					continue;
				}

				int frappeU = Accessor_CompactChunkVertex.mocha$encodeTexture(frappeTexCentroidU, vertex.frappeU);
				int frappeV = Accessor_CompactChunkVertex.mocha$encodeTexture(frappeTexCentroidV, vertex.frappeV);
				MemoryIntrinsics.putInt(ptr + 20, Accessor_CompactChunkVertex.mocha$packTexture(frappeU, frappeV));
				blockPos.set(vertex.blockPos);
				MemoryIntrinsics.putByte(ptr + 24, MochaVertexFormats.calculateCenterOffset(blockPos.getX(), vertex.x));
				MemoryIntrinsics.putByte(ptr + 25, MochaVertexFormats.calculateCenterOffset(blockPos.getY(), vertex.y));
				MemoryIntrinsics.putByte(ptr + 26, MochaVertexFormats.calculateCenterOffset(blockPos.getZ(), vertex.z));
				MemoryIntrinsics.putByte(ptr + 27, MochaVertexFormats.packAo(vertex.ao));

				ptr += VERTEX_STRIDE;
			}

			return ptr;
		};
	}
}
