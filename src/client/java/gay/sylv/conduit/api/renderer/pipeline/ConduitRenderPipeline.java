package gay.sylv.conduit.api.renderer.pipeline;

/// This is automatically implemented via interface injection on [com.mojang.blaze3d.pipeline.RenderPipeline].
public interface ConduitRenderPipeline {
	/// @see ConduitRenderPipelineBuilder#withSetup$conduit(RenderPipelineSetup)
	default RenderPipelineSetup setup$conduit() {
		throw new UnsupportedOperationException("Implemented in Mixin");
	}
}
