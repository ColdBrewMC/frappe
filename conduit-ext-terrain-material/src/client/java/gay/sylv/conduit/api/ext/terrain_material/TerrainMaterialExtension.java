/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.conduit.api.ext.terrain_material;

import static gay.sylv.conduit.impl.base.ConduitInitializer.modId;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.Identifier;

import gay.sylv.conduit.api.base.extension.RendererExtension;

public interface TerrainMaterialExtension extends RendererExtension {
	@Override
	default Identifier id() {
		return modId("terrain-material");
	}

	/// @return a new instance of [TerrainMaterial].
	/// @see TerrainMaterial#of
	@ApiStatus.OverrideOnly
	TerrainMaterial createChunkLayer(
			RenderPipeline pipeline,
			String label
	);
}
