package gay.sylv.conduit.api.ext.custom_chunk_layer;

import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;

/// An extension of [QuadView] with a [CustomChunkLayer] field.
///
/// When this extension is available, you may cast all [QuadView] to this type.
public interface QuadView_CustomChunkLayer extends QuadView {
	@Nullable CustomChunkLayer conduit$chunkLayer();
}
