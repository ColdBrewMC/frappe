/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.fabric_renderer;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;

/// A sane default implementation of [FabricRendererExtension].
///
/// This implementation's details MAY change at any time.
public class DefaultFabricRendererExtension implements FabricRendererExtension {
	@Override
	public int priority() {
		return DEFAULT_PRIORITY;
	}

	@Override
	public String getRendererId() {
		return "missingno";
	}

	@Override
	public FrappeRenderer getRenderer(Renderer renderer) {
		return (FrappeRenderer) renderer;
	}
}
