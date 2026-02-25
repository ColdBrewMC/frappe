/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base.extension;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.api.client.renderer.v1.RendererProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionType;
import gay.sylv.frappe.api.base.extension.SupportTier;
import gay.sylv.frappe.impl.base.FrappeInitializer;

public final class ExtensionRegistryImpl {
	private static final Map<Class<? extends RendererExtension>, String> CLASS_2_IMPL_ID = new HashMap<>();
	private static final Map<Class<? extends RendererExtensionType>, String> CLASS_2_ID = new HashMap<>();
	private static final Map<Class<? extends RendererExtensionType>, EntrypointContainer<RendererExtensionType>> TYPE_2_ENTRYPOINT = new HashMap<>();
	private static final Map<String, RendererExtension> EXTENSIONS = new HashMap<>();

	private static boolean loaded = false;

	private ExtensionRegistryImpl() {
	}

	@SuppressWarnings("unchecked") // Type matches when checked in parameter
	public static <T extends RendererExtensionType> EntrypointContainer<T> getEntrypoint(Class<T> clazz) {
		loadExtensions();
		return (EntrypointContainer<T>) TYPE_2_ENTRYPOINT.get(clazz);
	}

	public static <T extends RendererExtension> String getImplId(Class<T> clazz) {
		loadExtensions();
		return CLASS_2_IMPL_ID.get(clazz);
	}

	public static <T extends RendererExtensionType> String getId(Class<T> clazz) {
		loadExtensions();
		return CLASS_2_ID.get(clazz);
	}

	public static boolean isLoaded(String id) {
		loadExtensions();
		return EXTENSIONS.containsKey(id);
	}

	public @Nullable static RendererExtension getExtension(String id) {
		loadExtensions();
		return EXTENSIONS.get(id);
	}

	public static RendererExtension getExtensionOrThrow(String id) {
		loadExtensions();
		return Objects.requireNonNull(getExtension(id), "Extension of ID " + id + " is not loaded");
	}

	/// Loads all [extension types][RendererExtensionType] and their [implementations][RendererExtension].
	public static void loadExtensions() {
		if (loaded) return;
		loaded = true;

		List<EntrypointContainer<RendererExtensionType>> typeContainers = FabricLoader.getInstance()
				.getEntrypointContainers(
						"frappe:renderer_extension_type",
						RendererExtensionType.class
				);
		for (EntrypointContainer<RendererExtensionType> typeContainer : typeContainers) {
			TYPE_2_ENTRYPOINT.put(typeContainer.getEntrypoint().getClass(), typeContainer);
			RendererExtensionType type = typeContainer.getEntrypoint();
			String typeId = type.id();
			CLASS_2_ID.put(type.getClass(), typeId);
			//noinspection unchecked // Safe downcast
			Class<RendererExtension> implClass = (Class<RendererExtension>) type.implClass();
			Deque<EntrypointContainer<RendererExtension>> extensionContainers = new ConcurrentLinkedDeque<>(FabricLoader.getInstance()
					.getEntrypointContainers("frappe:" + typeId, implClass));

			String rendererId = RendererProvider.getModId();

			while (extensionContainers.size() > 1) {
				EntrypointContainer<RendererExtension> entrypoint0 = extensionContainers.pop();
				EntrypointContainer<RendererExtension> entrypoint1 = extensionContainers.pop();

				if (rendererId.equals(entrypoint0.getEntrypoint().getTargetRenderer())) {
					extensionContainers.push(entrypoint0);
				} else if (rendererId.equals(entrypoint1.getEntrypoint().getTargetRenderer())) {
					extensionContainers.push(entrypoint1);
				}
			}

			if (extensionContainers.isEmpty()) {
				if (type.supportTier().equals(SupportTier.CORE)) {
					FrappeInitializer.LOGGER.error("====================================================");
					FrappeInitializer.LOGGER.error("                       Frappé                       ");
					FrappeInitializer.LOGGER.error("A core renderer extension is unimplemented.         ");
					FrappeInitializer.LOGGER.error("This is strictly unsupported. Consider asking the   ");
					FrappeInitializer.LOGGER.error("developers of your renderer mod or compatibility mod");
					FrappeInitializer.LOGGER.error("to add support for the {}", typeId);
					FrappeInitializer.LOGGER.error("extension.                                          ");
					FrappeInitializer.LOGGER.error("====================================================");

					// Exempt dev envs from crashing
					if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
						throw new UnsupportedOperationException("A core tier renderer extension is unimplemented. See the above Frappé error message for more details.");
					}
				} else if (type.supportTier().equals(SupportTier.STANDARD)) {
					FrappeInitializer.LOGGER.warn("====================================================");
					FrappeInitializer.LOGGER.warn("                       Frappé                       ");
					FrappeInitializer.LOGGER.warn("A standard renderer extension is unimplemented.     ");
					FrappeInitializer.LOGGER.warn("This is bad for compatibility. Consider asking the  ");
					FrappeInitializer.LOGGER.warn("developers of your renderer mod or compatibility mod");
					FrappeInitializer.LOGGER.warn("to add support for the {}", typeId);
					FrappeInitializer.LOGGER.warn("extension.                                          ");
					FrappeInitializer.LOGGER.warn("====================================================");
				}

				continue;
			}

			EntrypointContainer<RendererExtension> extensionContainer = extensionContainers.getFirst();
			RendererExtension extension = extensionContainer.getEntrypoint();
			EXTENSIONS.put(typeId, extension);
			CLASS_2_IMPL_ID.put(implClass, typeId);
		}
	}
}
