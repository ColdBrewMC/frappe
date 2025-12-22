package gay.sylv.conduit.api.renderer.pipeline;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.jetbrains.annotations.ApiStatus;

/// This is automatically implemented via interface injection on [RenderPipeline.Builder].
@ApiStatus.NonExtendable
public interface ConduitRenderPipelineBuilder {
	/// Sets the [RenderPipelineSetup].
	///
	/// This is used when rendering to perform the
	/// setup necessary for a particular
	/// [render pipeline][RenderPipeline]
	/// in a [render pass][com.mojang.blaze3d.systems.RenderPass].
	default RenderPipeline.Builder withSetup$conduit(RenderPipelineSetup setup) {
		throw new UnsupportedOperationException("Implemented in Mixin");
	}

	/// Gets the [VertexFormat] of this pipeline.
	default VertexFormat getVertexFormat$conduit() {
		throw new UnsupportedOperationException("Implemented in Mixin");
	}
}
