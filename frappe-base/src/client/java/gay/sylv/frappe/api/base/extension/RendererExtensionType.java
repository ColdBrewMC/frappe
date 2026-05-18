/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.extension;

import org.jetbrains.annotations.ApiStatus;

import gay.sylv.frappe.impl.base.extension.ExtensionRegistryImpl;

/// A definition of an interface of [RendererExtension].
///
/// This is used to find implementations of [renderer extensions][RendererExtension].
/// This interface also holds metadata about a [renderer extension][RendererExtension].
///
/// Implementations of this interface must declare themselves with the
/// `frappe-base:renderer_extension_type` entrypoint in the `fabric.mod.json`.
public interface RendererExtensionType {
	/// Extensions may override this method in favor of using modules with separate mods.
	///
	/// The default value is this extension type's mod ID.
	///
	/// @return this extension type's unique ID.
	@ApiStatus.OverrideOnly
	default String id() {
		return ExtensionRegistryImpl.getEntrypoint(this.getClass()).getProvider().getMetadata().getId();
	}

	SupportTier supportTier();

	/// @return the type of the interface of [RendererExtension] that implementations will extend.
	@ApiStatus.OverrideOnly
	Class<? extends RendererExtension> implClass();
}
