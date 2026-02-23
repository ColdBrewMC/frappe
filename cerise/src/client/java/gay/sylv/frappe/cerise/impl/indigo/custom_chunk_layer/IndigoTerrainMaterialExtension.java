/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.cerise.impl.indigo.custom_chunk_layer;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialExtension;

public final class IndigoTerrainMaterialExtension implements TerrainMaterialExtension, IndigoRendererExtension {
	@Override
	public TerrainMaterial createChunkLayer(
			RenderPipeline pipeline,
			String label
	) {
		return new IndigoTerrainMaterial(pipeline, label);
	}
}
