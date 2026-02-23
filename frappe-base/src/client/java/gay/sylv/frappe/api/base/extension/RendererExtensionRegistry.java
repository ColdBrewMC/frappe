/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.extension;

import net.minecraft.resources.Identifier;

import gay.sylv.frappe.impl.base.extension.ExtensionRegistryImpl;

/// A registry containing types of [renderer extensions][RendererExtension].
///
/// @apiNote this is not for implementations of [RendererExtension].
public final class RendererExtensionRegistry {
	private RendererExtensionRegistry() {
	}

	/// Registers an API for an interface extending [RendererExtension].
	///
	/// @apiNote This method MUST NOT be called after
	/// [gay.sylv.frappe.api.ext.fabric_renderer.RendererRegistryEvents#registerBefore].
	public static <T extends RendererExtension> void register(Identifier id, Class<T> extensionClass) {
		//noinspection unchecked // Safe generic upcast
		final Class<RendererExtension> clazz = (Class<RendererExtension>) extensionClass;

		ExtensionRegistryImpl.register(id, clazz);
	}
}
