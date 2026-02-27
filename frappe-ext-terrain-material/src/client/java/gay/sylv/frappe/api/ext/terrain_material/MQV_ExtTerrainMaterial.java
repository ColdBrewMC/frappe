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

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;

public interface MQV_ExtTerrainMaterial extends FrappeMutableQuadView, QV_ExtTerrainMaterial {
	MQV_ExtTerrainMaterial frappe$terrainMaterial(TerrainMaterial material);

	/// Set the extra UVs present on quads supporting {@link TerrainMaterial}.
	/// @see #uv(int, float, float)
	MQV_ExtTerrainMaterial frappe$uv(int vertexIndex, float u, float v);

	/// @see #frappe$uv(int, float, float)
	default MQV_ExtTerrainMaterial frappe$uv(int vertexIndex, Vector2f uv) {
		return frappe$uv(vertexIndex, uv.x, uv.y);
	}

	/// @see #frappe$uv(int, float, float)
	default MQV_ExtTerrainMaterial frappe$uv(int vertexIndex, Vector2fc uv) {
		return frappe$uv(vertexIndex, uv.x(), uv.y());
	}
}
