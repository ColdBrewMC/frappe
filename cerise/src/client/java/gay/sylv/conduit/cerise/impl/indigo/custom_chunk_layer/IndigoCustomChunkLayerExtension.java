package gay.sylv.conduit.cerise.impl.indigo.custom_chunk_layer;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;

import gay.sylv.conduit.api.ext.custom_chunk_layer.CustomChunkLayer;
import gay.sylv.conduit.api.ext.custom_chunk_layer.CustomChunkLayerExtension;

public final class IndigoCustomChunkLayerExtension implements CustomChunkLayerExtension, IndigoRendererExtension {
	@Override
	public CustomChunkLayer createChunkLayer(
			RenderPipeline pipeline,
			int bufferSize,
			boolean sortOnUpload,
			ChunkSectionLayerGroup group,
			String label
	) {
		return new IndigoCustomChunkLayer(pipeline, bufferSize, sortOnUpload, group, label);
	}
}
