/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

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

	@FunctionalInterface
	public interface BeforeRendererRegistry {
		/// Called after a [net.fabricmc.fabric.api.client.renderer.v1.Renderer] is registered and
		/// before any [gay.sylv.conduit.api.base.extension.RendererExtension] are loaded.
		void beforeRegistry();
	}
}
