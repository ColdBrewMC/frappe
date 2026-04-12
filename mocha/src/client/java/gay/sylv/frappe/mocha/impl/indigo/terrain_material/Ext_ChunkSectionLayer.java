/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.jspecify.annotations.Nullable;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;

public interface Ext_ChunkSectionLayer {
	default @Nullable TerrainMaterial mocha$getAssociatedMaterial() {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default void mocha$setAssociatedMaterial(TerrainMaterial material) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default RenderPipeline mocha$getWireframePipeline() {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default void mocha$setWireframePipeline(RenderPipeline pipeline) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default boolean mocha$isFromMocha() {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default void mocha$setFromMocha() {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}
}
