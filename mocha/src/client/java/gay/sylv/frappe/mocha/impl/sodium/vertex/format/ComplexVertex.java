/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.sodium.vertex.format;

import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;

public class ComplexVertex extends ChunkVertexEncoder.Vertex {
	public float frappeU;
	public float frappeV;
	public long blockPos;

	public static ComplexVertex[] uninitializedQuad() {
		ComplexVertex[] vertices = new ComplexVertex[4];

		for (int i = 0; i < 4; ++i) {
			vertices[i] = new ComplexVertex();
		}

		return vertices;
	}
}
