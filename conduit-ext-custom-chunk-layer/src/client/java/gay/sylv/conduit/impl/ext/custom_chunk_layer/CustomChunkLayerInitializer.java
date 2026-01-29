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
