/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import net.minecraft.resources.Identifier;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;

public record IndigoTerrainMaterial(
		Identifier shaderId,
		String label,
		boolean simple
) implements TerrainMaterial {
}
