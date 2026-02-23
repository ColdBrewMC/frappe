/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.terrain_material;

import static gay.sylv.frappe.impl.base.FrappeInitializer.modId;

import net.fabricmc.api.ClientModInitializer;

import gay.sylv.frappe.api.base.extension.RendererExtensionRegistry;
import gay.sylv.frappe.api.ext.fabric_renderer.RendererRegistryEvents;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialExtension;

public class TerrainMaterialInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		RendererRegistryEvents.registerBefore(() -> {
			RendererExtensionRegistry.register(modId("terrain-material"), TerrainMaterialExtension.class);
		});
	}
}
