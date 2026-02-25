/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.extension;

import org.jetbrains.annotations.ApiStatus;

/// An additional, optional API that [renderers][net.fabricmc.fabric.api.client.renderer.v1.Renderer]
/// may opt into implementing. Mods may also implement extensions for renderers; however, multiple
/// present implementations of the same extensions may crash.
///
/// @apiNote By convention, interfaces of [RendererExtension] should end with `Extension`; however,
/// this not required. This is to distinguish the extension definitions from related or possibly
/// identically named classes.
public interface RendererExtension {
	/// @return the mod ID of the [renderer][net.fabricmc.fabric.api.client.renderer.v1.Renderer] that this
	/// extension implementation applies to.
	@ApiStatus.OverrideOnly
	String getTargetRenderer();
}
