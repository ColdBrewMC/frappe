package gay.sylv.conduit.cerise.impl.indigo.custom_chunk_layer;

import gay.sylv.conduit.api.base.extension.RendererExtension;
import gay.sylv.conduit.api.ext.fabric_renderer.ConduitRenderer;

public interface IndigoRendererExtension extends RendererExtension {
	@Override
	default int priority() {
		return COMPATIBILITY_PRIORITY;
	}

	@Override
	default boolean isEnabled() {
		return ConduitRenderer.get().id().equals("fabric-renderer-indigo");
	}
}
