/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.quad_view;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;

import gay.sylv.frappe.api.ext.quad_view.BlockAndTintGetterWithLevel;

@Mixin(RenderSectionRegion.class)
public abstract class Mixin_RenderSectionRegion implements BlockAndTintGetterWithLevel {
	@Shadow
	@Final
	private ClientLevel level;

	@Override
	public @Nullable ClientLevel frappe$getClientLevel() {
		return this.level;
	}
}
