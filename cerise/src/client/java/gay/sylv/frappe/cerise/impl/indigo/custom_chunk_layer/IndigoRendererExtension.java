/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.cerise.impl.indigo.custom_chunk_layer;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.ext.fabric_renderer.FrappeRenderer;

public interface IndigoRendererExtension extends RendererExtension {
	@Override
	default int priority() {
		return COMPATIBILITY_PRIORITY;
	}

	@Override
	default boolean isEnabled() {
		return FrappeRenderer.id().equals("fabric-renderer-indigo");
	}
}
