package gay.sylv.conduit.cerise.impl.indigo;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Mth;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.EncodingFormat;

import gay.sylv.conduit.api.ext.custom_chunk_layer.CustomChunkLayer;

@SuppressWarnings("UnstableApiUsage")
public final class CeriseIndigoEncodingFormat {
	public static int HEADER_CERISE_BITS = 4;
	public static int HEADER_STRIDE = EncodingFormat.HEADER_STRIDE + 1;

	private static final Map<CustomChunkLayer, Integer> CHUNK_LAYER_2_INDEX = new HashMap<>();
	private static int chunkLayerCount = 1;
	private static final CustomChunkLayer[] CHUNK_LAYERS = new CustomChunkLayer[64];

	private static final int CHUNK_LAYER_BIT_LENGTH = Mth.ceillog2(CHUNK_LAYERS.length);

	private static final int CHUNK_LAYER_BIT_OFFSET = 0;

	private static final int CHUNK_LAYER_MASK = bitMask(CHUNK_LAYER_BIT_LENGTH, CHUNK_LAYER_BIT_OFFSET);

	private CeriseIndigoEncodingFormat() {
	}

	public static CustomChunkLayer chunkLayer(int bits) {
		return CHUNK_LAYERS[(bits & CHUNK_LAYER_MASK) >>> CHUNK_LAYER_BIT_OFFSET];
	}

	public static int chunkLayer(int bits, CustomChunkLayer chunkLayer) {
		int index = CHUNK_LAYER_2_INDEX.computeIfAbsent(chunkLayer, layer -> {
			int idx = chunkLayerCount;
			CHUNK_LAYER_2_INDEX.put(layer, idx);
			CHUNK_LAYERS[idx] = layer;
			chunkLayerCount++;
			return idx;
		});
		return (bits & CHUNK_LAYER_MASK) | (index << CHUNK_LAYER_BIT_OFFSET);
	}

	private static int bitMask(int bitLength, int bitOffset) {
		return ((1 << bitLength) - 1) << bitOffset;
	}
}
