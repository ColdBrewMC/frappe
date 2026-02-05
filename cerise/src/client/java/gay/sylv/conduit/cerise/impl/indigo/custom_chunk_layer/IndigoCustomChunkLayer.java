/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.conduit.cerise.impl.indigo.custom_chunk_layer;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import gay.sylv.conduit.api.ext.custom_chunk_layer.CustomChunkLayer;

public record IndigoCustomChunkLayer(
		RenderPipeline pipeline,
		String label
) implements CustomChunkLayer {
}
