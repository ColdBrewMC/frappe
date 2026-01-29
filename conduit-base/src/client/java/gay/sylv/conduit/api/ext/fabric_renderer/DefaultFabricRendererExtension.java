package gay.sylv.conduit.api.ext.fabric_renderer;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;

/// A sane default implementation of [FabricRendererExtension].
///
/// This implementation's details MAY change at any time.
public class DefaultFabricRendererExtension implements FabricRendererExtension {
	@Override
	public int priority() {
		return DEFAULT_PRIORITY;
	}

	@Override
	public String getRendererId() {
		return "missingno";
	}

	@Override
	public ConduitRenderer getRenderer(Renderer renderer) {
		return (ConduitRenderer) renderer;
	}
}
