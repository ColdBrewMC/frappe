/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import com.mojang.blaze3d.vertex.VertexConsumer;

public interface ExtTerrainMaterial_BufferBuilder {
	default VertexConsumer frappe$setUv(float u, float v) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default VertexConsumer frappe$setMaterialId(byte id) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default boolean frappe$setAo(float ao) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}
}
