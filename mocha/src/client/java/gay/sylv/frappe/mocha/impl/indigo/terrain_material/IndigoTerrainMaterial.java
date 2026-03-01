/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.Identifier;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;

public record IndigoTerrainMaterial(
		Identifier shaderId,
		String label,
		Complexity complexity,
		@Nullable Function<RenderPipeline.Builder, RenderPipeline.Builder> renderPipelineModifier,
		@Nullable Runnable preRenderPassState,
		@Nullable Runnable postRenderPassState,
		@Nullable Consumer<RenderPass> renderPassSetup,
		@Nullable Consumer<RenderPass> renderPassCleanup
) implements TerrainMaterial {
	public static final Map<TerrainMaterial, ChunkSectionLayer> MATERIAL_2_LAYER = new ConcurrentHashMap<>();

	public ChunkSectionLayer getChunkLayer() {
		return MATERIAL_2_LAYER.computeIfAbsent(this, material -> {
			for (ChunkSectionLayer layer : ChunkSectionLayer.values()) {
				if (material.equals(layer.mocha$getAssociatedMaterial())) {
					return layer;
				}
			}

			throw new NullPointerException("This TerrainMaterial has no associated ChunkSectionLayer");
		});
	}
}
