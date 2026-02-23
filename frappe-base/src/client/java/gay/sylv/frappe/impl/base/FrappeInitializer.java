/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base;

import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;

public class FrappeInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
	}

	public static Identifier modId(String path) {
		return Identifier.fromNamespaceAndPath("frappe", path);
	}
}
