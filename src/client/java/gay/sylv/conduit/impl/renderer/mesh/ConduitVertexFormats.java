package gay.sylv.conduit.impl.renderer.mesh;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public final class ConduitVertexFormats {
	public static final VertexFormat BLOCK = VertexFormat.builder()
			.add("position", VertexFormatElement.POSITION)
			.add("color", VertexFormatElement.COLOR)
			.add("uv0", VertexFormatElement.UV0)
			.add("uv2", VertexFormatElement.UV2)
			.add("normal", ConduitVertexFormatElements.NORMAL)
			.add("lightmap", ConduitVertexFormatElements.LIGHTMAP)
			.padding(4) // pads from 44 to 48
			.build();

	private ConduitVertexFormats() {
	}
}
