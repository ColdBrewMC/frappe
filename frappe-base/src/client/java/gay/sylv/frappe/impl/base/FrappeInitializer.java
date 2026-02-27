/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;

import gay.sylv.frappe.impl.base.extension.ExtensionRegistryImpl;

public class FrappeInitializer implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Frappé");
	public static final String MOD_ID = "frappe";

	@Override
	public void onInitializeClient() {
		ExtensionRegistryImpl.loadExtensions(); // Ensure loaded
	}

	public static Identifier frappeId(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
