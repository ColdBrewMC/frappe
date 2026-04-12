/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.caffeinemc.mods.sodium.client.render.model.EncodingFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;

@Mixin(EncodingFormat.class)
public abstract class Mixin_EncodingFormat {
	@Definition(id = "VERTEX_X", field = "Lnet/caffeinemc/mods/sodium/client/render/model/EncodingFormat;VERTEX_X:I")
	@Expression("VERTEX_X = @(?)")
	@ModifyExpressionValue(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
	private static int setVertexX(int original) {
		return original + MochaIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@Definition(id = "TOTAL_STRIDE", field = "Lnet/caffeinemc/mods/sodium/client/render/model/EncodingFormat;TOTAL_STRIDE:I")
	@Expression("TOTAL_STRIDE = @(?)")
	@ModifyExpressionValue(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
	private static int setTotalStride(int original) {
		return original + MochaIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}
}
