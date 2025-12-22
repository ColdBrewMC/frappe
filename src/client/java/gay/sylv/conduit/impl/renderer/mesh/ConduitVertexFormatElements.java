package gay.sylv.conduit.impl.renderer.mesh;

import com.mojang.blaze3d.vertex.VertexFormatElement;

public final class ConduitVertexFormatElements {
	public static final VertexFormatElement NORMAL = VertexFormatElement.register(
			7,
			0,
			VertexFormatElement.Type.FLOAT,
			VertexFormatElement.Usage.NORMAL,
			3
	);
	public static final VertexFormatElement LIGHTMAP = VertexFormatElement.register(
			8,
			0,
			VertexFormatElement.Type.INT,
			VertexFormatElement.Usage.GENERIC,
			1
	);

	private ConduitVertexFormatElements() {
	}
}
