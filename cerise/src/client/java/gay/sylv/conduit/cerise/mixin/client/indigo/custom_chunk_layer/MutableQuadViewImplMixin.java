package gay.sylv.conduit.cerise.mixin.client.indigo.custom_chunk_layer;

import static gay.sylv.conduit.cerise.impl.indigo.CeriseIndigoEncodingFormat.HEADER_CERISE_BITS;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.conduit.api.ext.custom_chunk_layer.CustomChunkLayer;
import gay.sylv.conduit.api.ext.custom_chunk_layer.MutableQuadView_CustomChunkLayer;
import gay.sylv.conduit.cerise.impl.indigo.CeriseIndigoEncodingFormat;

@SuppressWarnings("UnstableApiUsage")
@Mixin(MutableQuadViewImpl.class)
public abstract class MutableQuadViewImplMixin extends QuadViewImpl implements MutableQuadView_CustomChunkLayer {
	@Override
	public void conduit$setChunkLayer(CustomChunkLayer chunkLayer) {
		this.data[this.baseIndex + HEADER_CERISE_BITS] =
				CeriseIndigoEncodingFormat.chunkLayer(this.data[this.baseIndex + HEADER_CERISE_BITS], chunkLayer);
	}
}
