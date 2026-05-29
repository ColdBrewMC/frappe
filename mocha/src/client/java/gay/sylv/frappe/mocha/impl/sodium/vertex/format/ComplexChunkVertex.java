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

/// An extension of [CompactChunkVertex] providing more vertex information for complex terrain materials.
public class ComplexChunkVertex extends CompactChunkVertex {
	public static final GlVertexFormat VERTEX_FORMAT = GlVertexFormat
			.builder(28)
			.addElement(DefaultChunkMeshAttributes.POSITION, 0, 0)
			.addElement(DefaultChunkMeshAttributes.COLOR, 1, 8)
			.addElement(DefaultChunkMeshAttributes.TEXTURE, 2, 12)
			.addElement(DefaultChunkMeshAttributes.LIGHT_MATERIAL_INDEX, 3, 16)
			.addElement(MochaChunkMeshAttributes.FRAPPE_UV, MochaChunkShaderBindingPoints.FRAPPE_UV, 20)
			.build();
	public static final ComplexChunkVertex INSTANCE = new ComplexChunkVertex();
	public static final ScopedValue<Integer> STRIDE_OFFSET = ScopedValue.newInstance();

	@Override
	public GlVertexFormat getVertexFormat() {
		return VERTEX_FORMAT;
	}

	@Override
	public ChunkVertexEncoder getEncoder() {
		ChunkVertexEncoder encoder = super.getEncoder();

		return (ptr, materialBits, vertices, section) -> {
			final long initialPtr = ptr;
			ScopedValue.where(STRIDE_OFFSET, 8)
					.call(() -> encoder.write(initialPtr, materialBits, vertices, section));

			for (int i = 0; i < 4; i++) {
				// FIXME: we tried just casting it without checking, but it throws a cast exception,
				//  so we have to check it with instanceof (expensive here).
				if (!(vertices[i] instanceof ComplexVertex vertex)) {
					MemoryIntrinsics.putInt(ptr + 20, Float.floatToIntBits(0.0f));
					MemoryIntrinsics.putInt(ptr + 24, Float.floatToIntBits(0.0f));

					ptr += 28;
					continue;
				}

				MemoryIntrinsics.putInt(ptr + 20, Float.floatToIntBits(vertex.frappeU));
				MemoryIntrinsics.putInt(ptr + 24, Float.floatToIntBits(vertex.frappeV));

				ptr += 28;
			}

			return ptr;
		};
	}
}
