package gay.sylv.conduit.api.renderer.pipeline;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;

/// A setup routine for a [RenderPass]
/// with a specific [RenderPipeline].
@FunctionalInterface
public interface RenderPipelineSetup {
	void setup(GpuDevice device, CommandEncoder commandEncoder, RenderPass renderPass, RenderPipeline pipeline);
}
