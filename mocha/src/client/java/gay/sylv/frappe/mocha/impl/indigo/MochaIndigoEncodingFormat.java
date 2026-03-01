/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.EncodingFormat;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;

@SuppressWarnings("UnstableApiUsage")
public final class MochaIndigoEncodingFormat {
	public static final int DELTA_HEADER_STRIDE = 12;
	public static final int HEADER_MOCHA_BITS = 4;
	public static final int FRAPPE_U_0 = 1;
	public static final int FRAPPE_V_0 = 2;
	public static final int HEADER_STRIDE = EncodingFormat.HEADER_STRIDE + DELTA_HEADER_STRIDE;

	public static final Map<TerrainMaterial, Integer> TERRAIN_MATERIAL_2_INDEX = new HashMap<>();
	public static int terrainMaterialCount = 1;
	public static final TerrainMaterial[] TERRAIN_MATERIALS = new TerrainMaterial[256];

	private static final int TERRAIN_MATERIAL_BIT_LENGTH = Mth.ceillog2(TERRAIN_MATERIALS.length);

	private static final int TERRAIN_MATERIAL_BIT_OFFSET = 0;

	private static final int TERRAIN_MATERIAL_MASK = bitMask(
			TERRAIN_MATERIAL_BIT_LENGTH,
			TERRAIN_MATERIAL_BIT_OFFSET
	);

	static {
		TERRAIN_MATERIALS[0] = TerrainMaterial.Builder.of(Identifier.fromNamespaceAndPath("frappe-ext-terrain-material", "default"))
				.label("Default")
				.complexity(TerrainMaterial.Complexity.SIMPLE)
				.build();
		TERRAIN_MATERIAL_2_INDEX.put(TERRAIN_MATERIALS[0], 0);
	}

	private MochaIndigoEncodingFormat() {
	}

	public static TerrainMaterial terrainMaterial(int bits) {
		return TERRAIN_MATERIALS[(bits & TERRAIN_MATERIAL_MASK) >>> TERRAIN_MATERIAL_BIT_OFFSET];
	}

	public static int terrainMaterial(int bits, TerrainMaterial terrainMaterial) {
		int index = TERRAIN_MATERIAL_2_INDEX.get(terrainMaterial);
		return (bits & TERRAIN_MATERIAL_MASK) | (index << TERRAIN_MATERIAL_BIT_OFFSET);
	}

	private static int bitMask(int bitLength, int bitOffset) {
		return ((1 << bitLength) - 1) << bitOffset;
	}
}
