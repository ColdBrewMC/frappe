/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.quad_view;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;
import gay.sylv.frappe.api.ext.quad_view.FrappeQuadEmitter;

@SuppressWarnings("UnstableApiUsage")
@Mixin(MutableQuadViewImpl.class)
public abstract class Mixin_MutableQuadViewImpl implements FrappeMutableQuadView, FrappeQuadEmitter {
}
