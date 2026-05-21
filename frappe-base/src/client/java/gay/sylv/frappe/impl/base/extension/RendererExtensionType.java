/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base.extension;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionMetadata;

/// A definition of an interface of [RendererExtension].
///
/// This is used to find implementations of [renderer extensions][RendererExtension].
interface RendererExtensionType {
	/// @return this extension's [metadata][RendererExtensionMetadata].
	RendererExtensionMetadata getMetadata();

	/// This class is the same class referenced in [RendererExtensionMetadata#implType()] from [#getMetadata()].
	///
	/// @return the type of the interface of [RendererExtension] that implementations will extend.
	default Class<? extends RendererExtension> implClass() {
		try {
			//noinspection unchecked // We assume the class reference is valid
			return (Class<? extends RendererExtension>) Class.forName(this.getMetadata().implType());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
