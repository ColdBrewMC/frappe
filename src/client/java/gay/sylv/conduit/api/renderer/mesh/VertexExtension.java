package gay.sylv.conduit.api.renderer.mesh;

import com.mojang.blaze3d.vertex.VertexFormatElement;

import gay.sylv.conduit.impl.renderer.mesh.ConduitVertexFormatElements;

/// A vertex extension is an extra vertex attribute
/// Conduit handles in special cases. These are
/// typically only added when existing
/// [pipelines][com.mojang.blaze3d.pipeline.RenderPipeline]
/// lack a necessary vertex attribute.
///
/// Each extension is defined by specific bits in
/// the vertex extension integer. How many bits are
/// in each extension determines how many ints the
/// extension requires.
public enum VertexExtension {
	LIGHTMAP(ConduitVertexFormatElements.LIGHTMAP),
	NORMAL(ConduitVertexFormatElements.NORMAL);

	private final VertexFormatElement vertexFormatElement;

	VertexExtension(VertexFormatElement vertexFormatElement) {
		this.vertexFormatElement = vertexFormatElement;
	}

	public VertexFormatElement getVertexFormatElement() {
		return vertexFormatElement;
	}
}
