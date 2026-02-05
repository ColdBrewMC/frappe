package gay.sylv.conduit.api.ext.custom_chunk_layer;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;

/// An extension of [MutableQuadView] with a [CustomChunkLayer] field.
///
/// When this extension is available, you may cast all [MutableQuadView] to this type.
public interface MutableQuadView_CustomChunkLayer extends MutableQuadView, QuadView_CustomChunkLayer {
	void conduit$setChunkLayer(CustomChunkLayer chunkLayer);
}
