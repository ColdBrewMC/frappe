/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.conduit.api.ext.terrain_material;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import gay.sylv.conduit.api.ext.fabric_renderer.ConduitRenderer;

public interface TerrainMaterial {
	/// @return a new instance of [TerrainMaterial].
	static TerrainMaterial of(
			RenderPipeline pipeline,
			String label
	) {
		return ConduitRenderer.getExtension(TerrainMaterialExtension.class)
				.createChunkLayer(pipeline, label);
	}

	RenderPipeline pipeline();

	String label();
}
