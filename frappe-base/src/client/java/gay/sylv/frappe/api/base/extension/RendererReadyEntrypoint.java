/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.extension;

import net.fabricmc.fabric.api.client.renderer.v1.Renderer;

/// An entrypoint that is invoked when the [Renderer] is ready to be used.
@FunctionalInterface
public interface RendererReadyEntrypoint {
	void onRendererReady(Renderer renderer);
}
