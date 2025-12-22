package gay.sylv.conduit.impl.renderer.mesh;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import gay.sylv.conduit.api.renderer.mesh.PrimitiveEmitter;

public class PrimitiveEmitterImpl extends MutablePrimitiveViewImpl implements PrimitiveEmitter {
	public PrimitiveEmitterImpl(RenderPipeline pipeline, int vertexExtensions) {
		super(pipeline, vertexExtensions);
	}
}
