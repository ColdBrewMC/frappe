package gay.sylv.conduit.api.renderer.pipeline;

import static gay.sylv.conduit.impl.base.ConduitInitializer.id;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/// Events related to the registration and manipulation of
/// [render pipelines][com.mojang.blaze3d.pipeline.RenderPipeline].
public final class ConduitPipelineEvents {
	public static final Event<BeforeRegistry> BEFORE_REGISTRY = EventFactory.createArrayBacked(BeforeRegistry.class, callbacks -> () -> {
		for (BeforeRegistry callback : callbacks) {
			callback.beforeRegistry();
		}
	});

	public static final Event<ModifyRegistry> MODIFY_REGISTRY = EventFactory.createWithPhases(
			ModifyRegistry.class,
			callbacks -> (id, builder) -> {
				RenderPipeline.Builder newBuilder = null;

				for (ModifyRegistry callback : callbacks) {
					if (newBuilder != null) {
						newBuilder = callback.modifyRegistry(id, newBuilder);
					} else {
						newBuilder = callback.modifyRegistry(id, builder);
					}
				}

				return newBuilder;
			},
			id("vertex_extensions"),
			Event.DEFAULT_PHASE
	);

	private ConduitPipelineEvents() {
	}

	@FunctionalInterface
	public interface BeforeRegistry {
		void beforeRegistry();
	}

	public interface ModifyRegistry {
		RenderPipeline.@Nullable Builder modifyRegistry(Identifier id, RenderPipeline.Builder builder);
	}
}
