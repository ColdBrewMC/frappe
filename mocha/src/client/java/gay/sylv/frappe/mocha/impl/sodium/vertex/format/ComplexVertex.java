package gay.sylv.frappe.mocha.impl.sodium.vertex.format;

import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;

public class ComplexVertex extends ChunkVertexEncoder.Vertex {
	public float frappeU;
	public float frappeV;

	public static ComplexVertex[] uninitializedQuad() {
		ComplexVertex[] vertices = new ComplexVertex[4];

		for (int i = 0; i < 4; ++i) {
			vertices[i] = new ComplexVertex();
		}

		return vertices;
	}
}
