/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.jspecify.annotations.Nullable;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;

import gay.sylv.frappe.api.base.config.FrappeConfigUtil;
import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionMetadata;
import gay.sylv.frappe.api.base.extension.RendererInfo;
import gay.sylv.frappe.api.base.extension.SupportTier;
import gay.sylv.frappe.impl.base.FrappeExtensionMetadataUtil;
import gay.sylv.frappe.impl.base.FrappeInitializer;

public final class ExtensionRegistryImpl {
	private static final Map<Class<? extends RendererExtension>, String> CLASS_2_IMPL_ID = new HashMap<>();
	private static final Map<Class<? extends RendererExtension>, String> CLASS_2_ID = new HashMap<>();
	private static final Map<String, RendererExtension> EXTENSIONS = new HashMap<>();
	public static final String UNSUPPORTED = "The current Renderer implementation is not supported by Frappé; try adding a mod that supports the current rendering optimization (Sodium, VulkanMod, etc.) or renderer mod.";

	private static boolean loaded = false;

	private ExtensionRegistryImpl() {
	}

	public static <T extends RendererExtension> String getImplId(Class<T> clazz) {
		loadExtensions();
		return CLASS_2_IMPL_ID.get(clazz);
	}

	public static <T extends RendererExtension> String getId(Class<T> clazz) {
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

		Collection<RendererExtensionType> rendererExtensionTypes = new ArrayList<>();

		for (RendererExtensionMetadata metadata : FrappeExtensionMetadataUtil.METADATA) {
			rendererExtensionTypes.add(new RendererExtensionTypeImpl(metadata));
		}

		for (RendererExtensionType type : rendererExtensionTypes) {
			RendererExtensionMetadata metadata = type.getMetadata();
			String typeId = metadata.id();
			CLASS_2_ID.put(type.implClass(), typeId);
			//noinspection unchecked // Safe downcast
			Class<RendererExtension> implClass = (Class<RendererExtension>) type.implClass();
			Deque<EntrypointContainer<RendererExtension>> extensionContainers = new ConcurrentLinkedDeque<>(FabricLoader.getInstance()
					.getEntrypointContainers("frappe:" + typeId, implClass));

			String rendererId = RendererInfo.getModId();

			while (extensionContainers.size() > 1) {
				EntrypointContainer<RendererExtension> entrypoint0 = extensionContainers.pop();
				EntrypointContainer<RendererExtension> entrypoint1 = extensionContainers.pop();

				if (entrypoint0.getEntrypoint().getTargetRenderers().contains(rendererId)) {
					extensionContainers.push(entrypoint0);
				} else if (entrypoint1.getEntrypoint().getTargetRenderers().contains(rendererId)) {
					extensionContainers.push(entrypoint1);
				}
			}

			boolean enabledByDefault = metadata.enabled();
			boolean unloaded = extensionContainers.isEmpty() || !FrappeConfigUtil.getBooleanProperty(typeId + ".enabled", enabledByDefault);

			if (unloaded) {
				String crashMissing = "frappe-base.extension.crash-if-missing-tier";
				String logMissing = "frappe-base.extension.log-if-missing-tier";

				if (metadata.supportTier().equals(SupportTier.CORE)) {
					if (FrappeConfigUtil.getBooleanProperty(logMissing + ".core", true)) {
						FrappeInitializer.LOGGER.error("====================================================");
						FrappeInitializer.LOGGER.error("                       Frappé                       ");
						FrappeInitializer.LOGGER.error("A core renderer extension is unimplemented.         ");
						FrappeInitializer.LOGGER.error("This is strictly unsupported. Consider asking the   ");
						FrappeInitializer.LOGGER.error("developers of your renderer mod or compatibility mod");
						FrappeInitializer.LOGGER.error("to add support for the {}", typeId);
						FrappeInitializer.LOGGER.error("extension.                                          ");
						FrappeInitializer.LOGGER.error("====================================================");
					}

					if (FrappeConfigUtil.getBooleanProperty(crashMissing + ".core", true)) {
						throw new UnsupportedOperationException("A core tier renderer extension is unimplemented. See the above Frappé error message for more details.");
					}
				} else if (metadata.supportTier().equals(SupportTier.STANDARD)) {
					if (FrappeConfigUtil.getBooleanProperty(logMissing + ".standard", true)) {
						FrappeInitializer.LOGGER.warn("====================================================");
						FrappeInitializer.LOGGER.warn("                       Frappé                       ");
						FrappeInitializer.LOGGER.warn("A standard renderer extension is unimplemented.     ");
						FrappeInitializer.LOGGER.warn("This is bad for compatibility. Consider asking the  ");
						FrappeInitializer.LOGGER.warn("developers of your renderer mod or compatibility mod");
						FrappeInitializer.LOGGER.warn("to add support for the {}", typeId);
						FrappeInitializer.LOGGER.warn("extension.                                          ");
						FrappeInitializer.LOGGER.warn("====================================================");
					}

					if (FrappeConfigUtil.getBooleanProperty(crashMissing + ".standard", false)) {
						throw new UnsupportedOperationException("A standard tier renderer extension is unimplemented. See the above Frappé error message for more details.");
					}
				} else if (metadata.supportTier().equals(SupportTier.NON_STANDARD)) {
					boolean crash = FrappeConfigUtil.getBooleanProperty(crashMissing + ".non_standard", false);
					boolean log = FrappeConfigUtil.getBooleanProperty(logMissing + ".non_standard", false);

					if (crash || log) {
						FrappeInitializer.LOGGER.warn("====================================================");
						FrappeInitializer.LOGGER.warn("                       Frappé                       ");
						FrappeInitializer.LOGGER.warn("A non-standard renderer extension is unimplemented. ");
						FrappeInitializer.LOGGER.warn("This is bad for compatibility. Consider asking the  ");
						FrappeInitializer.LOGGER.warn("developers of your renderer mod or compatibility mod");
						FrappeInitializer.LOGGER.warn("to add support for the {}", typeId);
						FrappeInitializer.LOGGER.warn("extension.                                          ");
						FrappeInitializer.LOGGER.warn("====================================================");
					}

					if (crash) {
						throw new UnsupportedOperationException("A non-standard tier renderer extension is unimplemented. See the above Frappé error message for more details.");
					}
				} else if (metadata.supportTier().equals(SupportTier.EXPERIMENTAL)) {
					boolean crash = FrappeConfigUtil.getBooleanProperty(crashMissing + ".experimental", false);
					boolean log = FrappeConfigUtil.getBooleanProperty(logMissing + ".experimental", false);

					if (crash || log) {
						FrappeInitializer.LOGGER.warn("====================================================");
						FrappeInitializer.LOGGER.warn("                       Frappé                       ");
						FrappeInitializer.LOGGER.warn("An experimental renderer extension is unimplemented.");
						FrappeInitializer.LOGGER.warn("This is bad for compatibility. Consider asking the  ");
						FrappeInitializer.LOGGER.warn("developers of your renderer mod or compatibility mod");
						FrappeInitializer.LOGGER.warn("to add support for the {}", typeId);
						FrappeInitializer.LOGGER.warn("extension.                                          ");
						FrappeInitializer.LOGGER.warn("====================================================");
					}

					if (crash) {
						throw new UnsupportedOperationException("An experimental tier renderer extension is unimplemented. See the above Frappé error message for more details.");
					}
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
