/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.terrain_material;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;

public interface MQV_ExtTerrainMaterial<Q extends MQV_ExtTerrainMaterial<Q>> extends FrappeMutableQuadView<Q>, QV_ExtTerrainMaterial<Q> {
	MQV_ExtTerrainMaterial<Q> frappe$terrainMaterial(TerrainMaterial material);
}
