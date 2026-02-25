/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.EncodingFormat;

import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;

@SuppressWarnings("UnstableApiUsage")
@Mixin(EncodingFormat.class)
public abstract class Mixin_EncodingFormat {
	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0)
	)
	private static int setVertexX(int original) {
		return original + MochaIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@Definition(
			id = "QUAD_STRIDE",
			field = "Lnet/fabricmc/fabric/impl/client/indigo/renderer/mesh/EncodingFormat;QUAD_STRIDE:I"
	)
	@Expression("4 + QUAD_STRIDE")
	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private static int setTotalStride(int original) {
		return original + MochaIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}
}
