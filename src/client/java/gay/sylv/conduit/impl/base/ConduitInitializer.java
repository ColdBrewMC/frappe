package gay.sylv.conduit.impl.base;

import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.renderer.v1.Renderer;

import gay.sylv.conduit.api.renderer.pipeline.ConduitPipelineEvents;
import gay.sylv.conduit.impl.renderer.ConduitRenderer;
import gay.sylv.conduit.impl.renderer.mesh.ConduitVertexFormats;

public class ConduitInitializer implements ClientModInitializer {
	public static ConduitInitializer INSTANCE;
	public static boolean disabled;

	@Override
	public void onInitializeClient() {
		if (disabled) return;

		Renderer.register(new ConduitRenderer());

		ConduitPipelineEvents.MODIFY_REGISTRY.register(id("vertex_extensions"), (id, builder) -> {
			if (id.getPath().endsWith("terrain") && id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
				builder
						.withVertexFormat(ConduitVertexFormats.BLOCK, VertexFormat.Mode.QUADS)
						.withVertexShader(id("core/terrain"));
			}

			return null;
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath("conduit", path);
	}
}
