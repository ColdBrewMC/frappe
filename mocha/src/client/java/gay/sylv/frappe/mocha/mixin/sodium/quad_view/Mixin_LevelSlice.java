/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.quad_view;

import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.multiplayer.ClientLevel;

import gay.sylv.frappe.api.ext.quad_view.BlockAndTintGetterWithLevel;

@Mixin(LevelSlice.class)
public abstract class Mixin_LevelSlice implements BlockAndTintGetterWithLevel {
	@Shadow
	@Final
	private ClientLevel level;

	@Override
	public @Nullable ClientLevel frappe$getClientLevel() {
		return this.level;
	}
}
