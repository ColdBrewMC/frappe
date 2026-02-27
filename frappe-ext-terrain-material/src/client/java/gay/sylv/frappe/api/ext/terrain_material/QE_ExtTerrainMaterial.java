/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.terrain_material;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadEmitter;

public interface QE_ExtTerrainMaterial extends FrappeQuadEmitter, MQV_ExtTerrainMaterial {
	@Override
	QE_ExtTerrainMaterial frappe$terrainMaterial(TerrainMaterial material);

	@Override
	QE_ExtTerrainMaterial frappe$uv(int vertexIndex, float u, float v);

	@Override
	default QE_ExtTerrainMaterial frappe$uv(int vertexIndex, Vector2f uv) {
		return (QE_ExtTerrainMaterial) MQV_ExtTerrainMaterial.super.frappe$uv(
				vertexIndex,
				uv
		);
	}

	@Override
	default QE_ExtTerrainMaterial frappe$uv(int vertexIndex, Vector2fc uv) {
		return (QE_ExtTerrainMaterial) MQV_ExtTerrainMaterial.super.frappe$uv(
				vertexIndex,
				uv
		);
	}
}
