/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;

import gay.sylv.frappe.api.base.extension.RendererInfo;
import gay.sylv.frappe.impl.base.extension.ExtensionRegistryImpl;

public class FrappeInitializer implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Frappé");
	public static final String MOD_ID = "frappe";
	private static @Nullable RendererInfo rendererInfo;

	@Override
	public void onInitializeClient() {
		ExtensionRegistryImpl.loadExtensions(); // Ensure loaded
	}

	public static RendererInfo getRendererInfo() {
		if (rendererInfo == null) {
			List<EntrypointContainer<RendererInfo>> entrypointContainers = FabricLoader.getInstance().getEntrypointContainers("frappe-base:renderer_info", RendererInfo.class);

			for (EntrypointContainer<RendererInfo> container : entrypointContainers) {
				RendererInfo rendererInfo = container.getEntrypoint();

				if (rendererInfo.getLoadedRendererModId() != null) {
					FrappeInitializer.rendererInfo = rendererInfo;
					break;
				}
			}

			if (FrappeInitializer.rendererInfo == null) {
				FrappeInitializer.LOGGER.error("====================================================");
				FrappeInitializer.LOGGER.error("                       Frappé                       ");
				FrappeInitializer.LOGGER.error("The current Renderer implementation is unsupported  ");
				FrappeInitializer.LOGGER.error("by Frappé, and no compatibility mod is present.     ");
				FrappeInitializer.LOGGER.error("Add a mod that adds support for the currently loaded");
				FrappeInitializer.LOGGER.error("rendering optimization mod (Sodium, VulkanMod, etc.)");
				FrappeInitializer.LOGGER.error("or renderer mod.                                    ");
				FrappeInitializer.LOGGER.error("====================================================");
				throw new IllegalStateException(ExtensionRegistryImpl.UNSUPPORTED);
			}
		}

		return rendererInfo;
	}

	public static Identifier frappeId(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
