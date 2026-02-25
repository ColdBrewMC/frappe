/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.terrain_material;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadEmitter;

public interface QE_ExtTerrainMaterial<Q extends QE_ExtTerrainMaterial<Q>> extends FrappeQuadEmitter<Q>, MQV_ExtTerrainMaterial<Q> {
	@Override
	QE_ExtTerrainMaterial<Q> frappe$terrainMaterial(TerrainMaterial material);
}
