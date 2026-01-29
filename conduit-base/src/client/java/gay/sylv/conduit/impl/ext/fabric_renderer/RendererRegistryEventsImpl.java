package gay.sylv.conduit.impl.ext.fabric_renderer;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import gay.sylv.conduit.api.ext.fabric_renderer.RendererRegistryEvents;

public final class RendererRegistryEventsImpl {
	public static boolean beforeRegistryInvoked = false;
	public static final Event<RendererRegistryEvents.BeforeRendererRegistry> BEFORE_REGISTRY = EventFactory.createArrayBacked(
			RendererRegistryEvents.BeforeRendererRegistry.class, callbacks -> () -> {
				beforeRegistryInvoked = true;

				for (RendererRegistryEvents.BeforeRendererRegistry callback : callbacks) {
					callback.beforeRegistry();
				}
			});
	public static boolean afterRegistryInvoked = false;
	public static final Event<RendererRegistryEvents.AfterRendererRegistry> AFTER_REGISTRY = EventFactory.createArrayBacked(
					RendererRegistryEvents.AfterRendererRegistry.class, callbacks -> () -> {
						afterRegistryInvoked = true;

						for (RendererRegistryEvents.AfterRendererRegistry callback : callbacks) {
							callback.afterRegistry();
						}
					});

	private RendererRegistryEventsImpl() {
	}
}
