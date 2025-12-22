package gay.sylv.conduit.impl.renderer.pipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.resources.Identifier;

import gay.sylv.conduit.api.renderer.pipeline.ConduitPipelineEvents;

public final class ConduitPipelinesImpl {
	private static final Map<Identifier, RenderPipeline> PIPELINE_REGISTRY = new HashMap<>();

	private ConduitPipelinesImpl() {
	}

	public static void register(Identifier id, RenderPipeline.Builder builder) {
		RenderPipeline.Builder replacement = ConduitPipelineEvents.MODIFY_REGISTRY
				.invoker().modifyRegistry(id, builder);

		if (replacement == null) {
			replacement = builder;
		}

		PIPELINE_REGISTRY.put(id, replacement.build());
	}

	public static RenderPipeline get(Identifier id) {
		return Objects.requireNonNull(PIPELINE_REGISTRY.get(id));
	}
}
