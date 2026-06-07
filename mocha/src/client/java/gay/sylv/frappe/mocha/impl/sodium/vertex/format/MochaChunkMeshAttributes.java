/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.sodium.vertex.format;

import net.caffeinemc.mods.sodium.client.gl.attribute.GlVertexAttributeFormat;
import net.caffeinemc.mods.sodium.client.render.vertex.VertexFormatAttribute;

public final class MochaChunkMeshAttributes {
	public static final VertexFormatAttribute FRAPPE_UV = new VertexFormatAttribute("FRAPPE_UV", GlVertexAttributeFormat.FLOAT, 2, false, false);
	public static final VertexFormatAttribute FRAPPE_AO = new VertexFormatAttribute("FRAPPE_AO", GlVertexAttributeFormat.FLOAT, 1, false, false);

	private MochaChunkMeshAttributes() {
	}
}
