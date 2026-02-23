/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base.extension;

import static gay.sylv.frappe.impl.base.FrappeInitializer.modId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.ext.fabric_renderer.FabricRendererExtension;

public final class ExtensionRegistryImpl {
	private static final Map<Identifier, Class<RendererExtension>> EXTENSION_CLASSES = new HashMap<>();
	private static final Map<Class<RendererExtension>, Identifier> CLASS_2_EXTENSIONS = new HashMap<>();
	private static final Map<Identifier, RendererExtension> EXTENSIONS = new HashMap<>();
	private static final Set<Identifier> EXTENSIONS_TO_LOAD = new HashSet<>();

	private ExtensionRegistryImpl() {
	}

	public static <T extends RendererExtension> Identifier getId(Class<T> clazz) {
		return CLASS_2_EXTENSIONS.get(clazz);
	}

	public static boolean isLoaded(Identifier id) {
		return EXTENSIONS.containsKey(id);
	}

	public @Nullable static RendererExtension getExtension(Identifier id) {
		return EXTENSIONS.get(id);
	}

	public static RendererExtension getExtensionOrThrow(Identifier id) {
		return Objects.requireNonNull(getExtension(id), "Extension of ID " + id + " is not loaded");
	}

	/// Loads all extensions that have been registered.
	///
	/// This may be called at any point before [gay.sylv.frappe.api.ext.fabric_renderer.RendererReadyEntrypoint]
	/// has been invoked.
	public static void loadReadyExtensions() {
		// Special-case the default implementation so other extensions can use it immediately
		if (!EXTENSIONS.containsKey(modId("fabric-renderer"))) {
			@SuppressWarnings("unchecked") // We can just assume it extends RendererExtension
			ServiceLoader<RendererExtension> loader = ServiceLoader.load((Class<RendererExtension>) (Class<?>) FabricRendererExtension.class);
			AtomicReference<@Nullable RendererExtension> extensionAtomic = new AtomicReference<>();
			findSuitableExtension(
					Map.entry(modId("fabric-renderer"), loader),
					entry -> extensionAtomic.set(entry.getValue())
			);

			RendererExtension extension = Objects.requireNonNull(
					extensionAtomic.get(),
					"An implementation of FabricRendererExtension must be present"
			);
			EXTENSIONS.put(modId("fabric-renderer"), extension);
		}

		EXTENSIONS_TO_LOAD.stream()
				.map(id -> Map.entry(id, ServiceLoader.load(EXTENSION_CLASSES.get(id))))
				.mapMulti(ExtensionRegistryImpl::findSuitableExtension)
				.forEach(entry -> {
					EXTENSIONS.put(entry.getKey(), entry.getValue());
					EXTENSIONS_TO_LOAD.remove(entry.getKey());
				});
	}

	private static void findSuitableExtension(
			Map.Entry<Identifier, ServiceLoader<RendererExtension>> entry,
			Consumer<Map.Entry<Identifier, RendererExtension>> consumer
	) {
		ServiceLoader<RendererExtension> serviceLoader = entry.getValue();
		Optional<RendererExtension> optional = serviceLoader.findFirst();

		if (optional.isEmpty()) {
			return;
		}

		RendererExtension extension = serviceLoader.stream()
				.map(ServiceLoader.Provider::get)
				.filter(RendererExtension::isEnabled)
				.reduce(
						optional.get(),
						(a, b) ->
								a.priority() > b.priority() ? a : b
				);
		consumer.accept(Map.entry(entry.getKey(), extension));
	}

	public static void register(Identifier id, Class<RendererExtension> extensionClass) {
		if (EXTENSION_CLASSES.put(id, extensionClass) != null || CLASS_2_EXTENSIONS.put(extensionClass, id) != null) {
			throw new IllegalStateException("Conduit renderer extension of ID " + id + " is already registered");
		}

		EXTENSIONS_TO_LOAD.add(id);
	}
}
