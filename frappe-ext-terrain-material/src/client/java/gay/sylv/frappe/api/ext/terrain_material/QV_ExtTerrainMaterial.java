/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.terrain_material;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadView;

public interface QV_ExtTerrainMaterial extends FrappeQuadView {
	TerrainMaterial frappe$terrainMaterial();

	/// The U coordinate of the extra UVs present on quads supporting {@link TerrainMaterial}.
	/// @see #u(int)
	float frappe$u(int vertexIndex);

	/// The V coordinate of the extra UVs present on quads supporting {@link TerrainMaterial}.
	/// @see #v(int)
	float frappe$v(int vertexIndex);

	/// The color of ambient occlusion as applied to this vertex.
	float frappe$ao(int vertexIndex);
}
