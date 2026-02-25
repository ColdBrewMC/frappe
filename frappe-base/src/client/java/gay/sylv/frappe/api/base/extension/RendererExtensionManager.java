/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.extension;

import org.jetbrains.annotations.ApiStatus;

import gay.sylv.frappe.impl.ext.fabric_renderer.RendererExtensionManagerImpl;

@ApiStatus.NonExtendable
public interface RendererExtensionManager {
	private static RendererExtensionManager get() {
		return RendererExtensionManagerImpl.INSTANCE;
	}

	/// @param id the [String] of the [RendererExtensionType].
	/// @return whether the [RendererExtensionType] is loaded.
	static boolean isExtensionLoaded(String id) {
		return get().frappe$isExtensionLoaded(id);
	}

	/// @param <T> the type of the [RendererExtensionType].
	/// @param clazz the [Class] of the [RendererExtensionType].
	/// @return whether the [RendererExtensionType] is loaded.
	static <T extends RendererExtensionType> boolean isExtensionLoaded(Class<T> clazz) {
		return get().frappe$isExtensionLoaded(clazz);
	}

	/// @param id the [String] of the [RendererExtension].
	/// @return if present, the [RendererExtension] associated with the given [String].
	/// @throws NullPointerException if the [RendererExtension] is not loaded.
	static RendererExtension getExtension(String id) {
		return get().frappe$getExtension(id);
	}

	/// @param <T> the type of the [RendererExtension].
	/// @param clazz the [Class] of the [RendererExtension].
	/// @return if present, the [RendererExtension] associated with the given [Class].
	/// @throws NullPointerException if the [RendererExtension] is not loaded.
	static <T extends RendererExtension> T getExtension(Class<T> clazz) {
		return get().frappe$getExtension(clazz);
	}

	/// @see #isExtensionLoaded(String)
	@ApiStatus.OverrideOnly
	boolean frappe$isExtensionLoaded(String id);

	/// @see #isExtensionLoaded(Class)
	@ApiStatus.OverrideOnly
	<T extends RendererExtensionType> boolean frappe$isExtensionLoaded(Class<T> clazz);

	/// @see #getExtension(String)
	@ApiStatus.OverrideOnly
	RendererExtension frappe$getExtension(String id);

	/// @see #getExtension(Class)
	@ApiStatus.OverrideOnly
	<T extends RendererExtension> T frappe$getExtension(Class<T> clazz);
}
