/*
 * Conduit
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.extension;

import static gay.sylv.frappe.impl.base.NoInlineUtil.noInline;

import net.minecraft.resources.Identifier;

/// An additional, optional API that [renderers][net.fabricmc.fabric.api.client.renderer.v1.Renderer]
/// may opt into implementing. Mods may also implement extensions for renderers; however, multiple
/// present implementations of the same extensions may crash.
///
/// @apiNote By convention, interfaces of [RendererExtension] should end with `Extension`; however,
/// this not required. This is to distinguish the extension definitions from related or possibly
/// identically named classes.
public interface RendererExtension {
	/// The [#priority()] FRAPI implementations (renderers) should use by default.
	int RENDERER_PRIORITY = noInline(10000);
	/// The [#priority()] mods implementing extensions for other renderers should use by default.
	int COMPATIBILITY_PRIORITY = noInline(-10000);
	/// The [#priority()] default implementations of extensions MUST use.
	int DEFAULT_PRIORITY = noInline(Integer.MIN_VALUE);

	/// Implementations of [renderer extensions][RendererExtension] MUST NOT override this method.
	///
	/// @return this extension's unique [Identifier].
	Identifier id();

	/// When multiple implementations of the same extensions are loaded, the extension with the
	/// highest priority gets loaded.
	///
	/// Custom [RendererExtension] interfaces MUST NOT override this method.
	///
	/// @return the priority by which this extension is loaded when other implementations are present.
	int priority();

	/// This method is especially useful in cases where implementations may conflict or may only be
	/// enabled if another mod is present.
	///
	/// Custom [RendererExtension] interfaces MUST NOT override this method.
	///
	/// @return whether this extension should be enabled.
	default boolean isEnabled() {
		return true;
	}
}
