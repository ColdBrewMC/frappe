package gay.sylv.conduit.api.renderer.pipeline;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.resources.Identifier;

import gay.sylv.conduit.impl.renderer.pipeline.ConduitPipelinesImpl;

/// Utilities for registering, extending, or overwriting existing pipelines.
public final class ConduitPipelines {
	private ConduitPipelines() {
	}

	public static void register(Identifier identifier, RenderPipeline.Builder builder) {
		ConduitPipelinesImpl.register(identifier, builder);
	}
}
