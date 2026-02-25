/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.terrain_material;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionType;
import gay.sylv.frappe.api.base.extension.SupportTier;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialExtension;

public final class TerrainMaterialExtensionType implements RendererExtensionType {
	@Override
	public SupportTier supportTier() {
		return SupportTier.EXPERIMENTAL;
	}

	@Override
	public Class<? extends RendererExtension> implClass() {
		return TerrainMaterialExtension.class;
	}
}
