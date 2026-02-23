/*
 * Conduit
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.fabric_renderer;

/// An entrypoint that is invoked as soon as [net.fabricmc.fabric.api.client.renderer.v1.Renderer] and all its
/// [extensions][gay.sylv.frappe.api.base.extension.RendererExtension] are ready for use.
///
/// This is useful for mods that need to use extensions. Mods should not be checking if extensions
/// are loaded or exist before this event is invoked. Instead, they should do those checks in a
/// callback registered with this method.
///
/// @see RendererRegistryEvents#registerBefore(RendererRegistryEvents.BeforeRendererRegistry)
@FunctionalInterface
public interface RendererReadyEntrypoint {
	/// @see RendererReadyEntrypoint
	void onRendererReady();
}
