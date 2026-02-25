/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.quad_view;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionType;
import gay.sylv.frappe.api.base.extension.SupportTier;
import gay.sylv.frappe.api.ext.quad_view.QuadViewExtension;

public final class QuadViewExtensionType implements RendererExtensionType {
	@Override
	public SupportTier supportTier() {
		return SupportTier.CORE;
	}

	@Override
	public Class<? extends RendererExtension> implClass() {
		return QuadViewExtension.class;
	}
}
