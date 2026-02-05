package gay.sylv.conduit.cerise.mixin.client.indigo.custom_chunk_layer;

import static gay.sylv.conduit.cerise.impl.indigo.CeriseIndigoEncodingFormat.HEADER_CERISE_BITS;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.conduit.api.ext.custom_chunk_layer.CustomChunkLayer;
import gay.sylv.conduit.api.ext.custom_chunk_layer.QuadView_CustomChunkLayer;
import gay.sylv.conduit.cerise.impl.indigo.CeriseIndigoEncodingFormat;

@SuppressWarnings("UnstableApiUsage")
@Mixin(QuadViewImpl.class)
public abstract class QuadViewImplMixin implements QuadView_CustomChunkLayer {
	@Shadow
	protected int[] data;

	@Shadow
	protected int baseIndex;

	@Override
	public @Nullable CustomChunkLayer conduit$chunkLayer() {
		return CeriseIndigoEncodingFormat.chunkLayer(this.data[this.baseIndex + HEADER_CERISE_BITS]);
	}
}
