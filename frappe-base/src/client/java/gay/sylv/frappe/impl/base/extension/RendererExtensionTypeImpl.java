/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base.extension;

import gay.sylv.frappe.api.base.extension.RendererExtensionMetadata;

public record RendererExtensionTypeImpl(RendererExtensionMetadata metadata) implements RendererExtensionType {
	@Override
	public RendererExtensionMetadata getMetadata() {
		return this.metadata();
	}
}
