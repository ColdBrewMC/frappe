package gay.sylv.frappe.api.base.extension;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;

/// An entrypoint that is invoked when the [Renderer] is ready to be used.
@FunctionalInterface
public interface RendererReadyEntrypoint {
	void onRendererReady(Renderer renderer);
}
