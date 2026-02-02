/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.conduit.api.ext.custom_chunk_layer;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;

import gay.sylv.conduit.api.ext.fabric_renderer.ConduitRenderer;

public interface CustomChunkLayer {
	/// @return a new instance of [CustomChunkLayer].
	static CustomChunkLayer of(
			RenderPipeline pipeline,
			int bufferSize,
			boolean sortOnUpload,
			ChunkSectionLayerGroup group,
			String label
	) {
		return ConduitRenderer.getExtension(CustomChunkLayerExtension.class)
				.createChunkLayer(pipeline, bufferSize, sortOnUpload, group, label);
	}

	RenderPipeline pipeline();

	int bufferSize();

	boolean sortOnUpload();

	ChunkSectionLayerGroup group();

	String label();
}
