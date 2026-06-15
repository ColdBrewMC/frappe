/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.vertex.format;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public final class MochaVertexFormats {
	public static final VertexFormatElement FAST_POSITION = VertexFormatElement.register(
			31,
			0,
			VertexFormatElement.Type.USHORT,
			true,
			3
	);
	public static final VertexFormatElement AO = VertexFormatElement.register(
			30,
			0,
			VertexFormatElement.Type.UBYTE,
			false,
			1
	);
	public static final VertexFormatElement UV3 = VertexFormatElement.register(
			28,
			3,
			VertexFormatElement.Type.FLOAT,
			false,
			2
	);
	public static final VertexFormatElement SIMPLE_MATERIAL_INFO = VertexFormatElement.register(
			29,
			0,
			VertexFormatElement.Type.UBYTE,
			false,
			1
	);
	public static final VertexFormatElement CENTER_OFFSET = VertexFormatElement.register(
			27,
			0,
			VertexFormatElement.Type.BYTE,
			false,
			3
	);
	public static final VertexFormat COMPLEX_TERRAIN = VertexFormat.builder()
			.add("Position", VertexFormatElement.POSITION) // 12
			.add("Color", VertexFormatElement.COLOR) // 4
			.add("UV0", VertexFormatElement.UV0) // 8
			.add("UV2", VertexFormatElement.UV2) // 4
			.add("_vert_frappe_uv", UV3) // 8
			.add("_vert_frappe_simple_material_info", SIMPLE_MATERIAL_INFO) // 1
			.add("_vert_frappe_center_offset", CENTER_OFFSET) // 3
			.add("_vert_frappe_ao", AO) // 1
			.padding(3)
			.build(); // 44
	// FIXME: after glsl-preprocessor is used, don't define _vert_frappe_uv
	public static final VertexFormat SIMPLE_TERRAIN = VertexFormat.builder()
			.add("Position", VertexFormatElement.POSITION) // 12
			.add("Color", VertexFormatElement.COLOR) // 4
			.add("UV0", VertexFormatElement.UV0) // 8
			.add("UV2", VertexFormatElement.UV2) // 4
			.add("_vert_frappe_uv", UV3) // 8
			.add("_vert_frappe_simple_material_info", SIMPLE_MATERIAL_INFO) // 1
			.add("_vert_frappe_center_offset", CENTER_OFFSET) // 3
			.add("_vert_frappe_ao", AO) // 1
			.padding(3)
			.build(); // 44

	private MochaVertexFormats() {
	}

	public static byte calculateCenterOffset(int block, float vertex) {
		// convert to block center position
		float pos = block + 0.5f;
		// get difference from block center (to be added in shader)
		pos = pos - vertex;
		// quantize displacement into sixty-fourths
		return (byte) (pos * 64.0f);
	}

	// Quantize and pack AO into a byte.
	public static byte packAo(float ao) {
		// We assume AO is always positive.
		return (byte) (Math.round(ao * 255.0f) & 255);
	}
}
