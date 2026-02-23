/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.fabric_renderer;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import gay.sylv.frappe.api.ext.fabric_renderer.RendererRegistryEvents;

public final class RendererRegistryEventsImpl {
	public static boolean beforeRegistryInvoked = false;
	public static final Event<RendererRegistryEvents.BeforeRendererRegistry> BEFORE_REGISTRY = EventFactory.createArrayBacked(
			RendererRegistryEvents.BeforeRendererRegistry.class, callbacks -> () -> {
				beforeRegistryInvoked = true;

				for (RendererRegistryEvents.BeforeRendererRegistry callback : callbacks) {
					callback.beforeRegistry();
				}
			});

	private RendererRegistryEventsImpl() {
	}
}
