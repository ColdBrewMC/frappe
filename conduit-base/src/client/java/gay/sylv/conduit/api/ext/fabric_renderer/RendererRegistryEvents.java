package gay.sylv.conduit.api.ext.fabric_renderer;

import gay.sylv.conduit.impl.ext.fabric_renderer.RendererRegistryEventsImpl;

/// Events related to the registration of [net.fabricmc.fabric.api.client.renderer.v1.Renderer] and
/// [gay.sylv.conduit.api.base.extension.RendererExtension]
public final class RendererRegistryEvents {
	private RendererRegistryEvents() {
	}

	/// Register a callback for [BeforeRendererRegistry#beforeRegistry()].
	///
	/// Mods MUST register interfaces of
	/// [gay.sylv.conduit.api.base.extension.RendererExtension] using this callback.
	public static void registerBefore(BeforeRendererRegistry callback) {
		if (RendererRegistryEventsImpl.beforeRegistryInvoked) {
			callback.beforeRegistry();
		}

		RendererRegistryEventsImpl.BEFORE_REGISTRY.register(callback);
	}

	/// Register a callback for [AfterRendererRegistry#afterRegistry()].
	///
	/// This is useful for mods that need to use extensions. Mods should not be checking if extensions
	/// are loaded or exist before this event is invoked. Instead, they should do those checks in a
	/// callback registered with this method.
	///
	/// @see #registerBefore(BeforeRendererRegistry)
	public static void registerAfter(AfterRendererRegistry callback) {
		if (RendererRegistryEventsImpl.afterRegistryInvoked) {
			throw new IllegalStateException("An AfterRendererRegistry callback has been registered too late!");
		}

		RendererRegistryEventsImpl.AFTER_REGISTRY.register(callback);
	}

	@FunctionalInterface
	public interface BeforeRendererRegistry {
		/// Called after a [net.fabricmc.fabric.api.client.renderer.v1.Renderer] is registered and
		/// before any [gay.sylv.conduit.api.base.extension.RendererExtension] are loaded.
		void beforeRegistry();
	}

	@FunctionalInterface
	public interface AfterRendererRegistry {
		/// Called after a [net.fabricmc.fabric.api.client.renderer.v1.Renderer] is registered and after
		/// all [gay.sylv.conduit.api.base.extension.RendererExtension] are loaded.
		void afterRegistry();
	}
}
