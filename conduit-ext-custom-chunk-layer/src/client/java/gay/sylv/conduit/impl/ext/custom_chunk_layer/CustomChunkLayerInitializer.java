/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.conduit.impl.ext.custom_chunk_layer;

import static gay.sylv.conduit.impl.base.ConduitInitializer.modId;

import net.fabricmc.api.ClientModInitializer;

import gay.sylv.conduit.api.base.extension.RendererExtensionRegistry;
import gay.sylv.conduit.api.ext.custom_chunk_layer.CustomChunkLayerExtension;
import gay.sylv.conduit.api.ext.fabric_renderer.RendererRegistryEvents;

public class CustomChunkLayerInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		RendererRegistryEvents.registerBefore(() -> {
			RendererExtensionRegistry.register(modId("custom-chunk-layer"), CustomChunkLayerExtension.class);
		});
	}
}
