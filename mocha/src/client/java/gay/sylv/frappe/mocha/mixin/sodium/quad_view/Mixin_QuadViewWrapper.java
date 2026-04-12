/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.quad_view;

import net.caffeinemc.mods.sodium.client.render.frapi.wrapper.QuadViewWrapper;
import org.spongepowered.asm.mixin.Mixin;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadView;

@Mixin(QuadViewWrapper.class)
public abstract class Mixin_QuadViewWrapper implements FrappeQuadView {
}
