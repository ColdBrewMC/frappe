/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.fabric_renderer;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.impl.base.extension.ExtensionRegistryImpl;

/// @see FabricRendererExtension
/// @apiNote Do not use this class during mod initialization! Use [RendererReadyEntrypoint]
/// to ensure a [Renderer] has been registered already.
public interface FrappeRenderer {
	private static FrappeRenderer get() {
		return ((FabricRendererExtension) ExtensionRegistryImpl.getExtensionOrThrow(ExtensionRegistryImpl.getId(FabricRendererExtension.class))).getRenderer(Renderer.get());
	}

	/// @return the unique (mod) ID of this [Renderer].
	static String id() {
		return get().conduit$id();
	}

	/// @param id the [Identifier] of the [RendererExtension].
	/// @return whether the [RendererExtension] is loaded.
	static boolean isExtensionLoaded(Identifier id) {
		return get().conduit$isExtensionLoaded(id);
	}

	/// @param <T> the type of the [RendererExtension].
	/// @param clazz the [Class] of the [RendererExtension].
	/// @return whether the [RendererExtension] is loaded.
	static <T extends RendererExtension> boolean isExtensionLoaded(Class<T> clazz) {
		return get().conduit$isExtensionLoaded(clazz);
	}

	/// @param id the [Identifier] of the [RendererExtension].
	/// @return if present, the [RendererExtension] associated with the given [Identifier].
	/// @throws NullPointerException if the [RendererExtension] is not loaded.
	static RendererExtension getExtension(Identifier id) {
		return get().conduit$getExtension(id);
	}

	/// @param <T> the type of the [RendererExtension].
	/// @param clazz the [Class] of the [RendererExtension].
	/// @return if present, the [RendererExtension] associated with the given [Class].
	/// @throws NullPointerException if the [RendererExtension] is not loaded.
	static <T extends RendererExtension> T getExtension(Class<T> clazz) {
		return get().conduit$getExtension(clazz);
	}

	/// @see #id()
	default String conduit$id() {
		throw new IllegalStateException("Implemented via Mixin.");
	}

	/// @see #isExtensionLoaded(Identifier)
	@ApiStatus.OverrideOnly
	default boolean conduit$isExtensionLoaded(Identifier id) {
		return ExtensionRegistryImpl.isLoaded(id);
	}

	/// @see #isExtensionLoaded(Class)
	@ApiStatus.OverrideOnly
	default <T extends RendererExtension> boolean conduit$isExtensionLoaded(Class<T> clazz) {
		return conduit$isExtensionLoaded(ExtensionRegistryImpl.getId(clazz));
	}

	/// @see #getExtension(Identifier)
	@ApiStatus.OverrideOnly
	default RendererExtension conduit$getExtension(Identifier id) {
		return ExtensionRegistryImpl.getExtensionOrThrow(id);
	}

	/// @see #getExtension(Class)
	@ApiStatus.OverrideOnly
	default <T extends RendererExtension> T conduit$getExtension(Class<T> clazz) {
		//noinspection unchecked // Type T will always be the type T of Class<T> of parameter clazz.
		return (T) conduit$getExtension(ExtensionRegistryImpl.getId(clazz));
	}
}
