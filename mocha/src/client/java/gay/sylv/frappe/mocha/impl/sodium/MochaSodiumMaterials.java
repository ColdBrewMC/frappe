/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.sodium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public final class MochaSodiumMaterials {
	public static final Map<ChunkSectionLayer, Material> MOCHA_MATERIALS = new HashMap<>();
	public static final List<TerrainRenderPass> RENDER_PASSES = new ArrayList<>();

	private MochaSodiumMaterials() {
	}
}
