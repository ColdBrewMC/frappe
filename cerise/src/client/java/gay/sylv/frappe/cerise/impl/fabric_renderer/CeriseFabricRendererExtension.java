/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.cerise.impl.fabric_renderer;

import net.fabricmc.loader.api.FabricLoader;

import gay.sylv.frappe.api.ext.fabric_renderer.DefaultFabricRendererExtension;

public final class CeriseFabricRendererExtension extends DefaultFabricRendererExtension {
	@Override
	public int priority() {
		return COMPATIBILITY_PRIORITY;
	}

	@Override
	public String getRendererId() {
		if (FabricLoader.getInstance().isModLoaded("sodium")) {
			return "sodium";
		} else if (FabricLoader.getInstance().isModLoaded("fabric-renderer-indigo")) {
			return "fabric-renderer-indigo";
		} else {
			return super.getRendererId();
		}
	}
}
