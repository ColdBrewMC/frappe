/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AltModelBlockRendererImpl;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;
import gay.sylv.frappe.api.ext.terrain_material.MQV_ExtTerrainMaterial;

@SuppressWarnings("UnstableApiUsage")
@Mixin(AltModelBlockRendererImpl.class)
public abstract class Mixin_AltModelBlockRendererImpl {
	@Definition(id = "ao", field = "Lnet/fabricmc/fabric/impl/client/indigo/renderer/aocalc/AoCalculator;ao:[F")
	@Expression("?.ao[?]")
	@ModifyExpressionValue(method = "shadeQuad", at = @At("MIXINEXTRAS:EXPRESSION"))
	private float captureAoValue(float original, @Share("ao") LocalFloatRef ao) {
		ao.set(original);
		return original;
	}

	@Definition(id = "quad", local = @Local(type = MutableQuadViewImpl.class, name = "quad", argsOnly = true))
	@Definition(
			id = "color",
			method = "Lnet/fabricmc/fabric/impl/client/indigo/renderer/mesh/MutableQuadViewImpl;color(II)Lnet/fabricmc/fabric/impl/client/indigo/renderer/mesh/MutableQuadViewImpl;"
	)
	@Definition(id = "scaleRGB", method = "Lnet/minecraft/util/ARGB;scaleRGB(IF)I")
	@Definition(id = "ao", field = "Lnet/fabricmc/fabric/impl/client/indigo/renderer/aocalc/AoCalculator;ao:[F")
	@Expression("quad.color(?, scaleRGB(?, ?.ao[?]))")
	@WrapOperation(method = "shadeQuad", at = @At("MIXINEXTRAS:EXPRESSION"))
	private MutableQuadViewImpl useAoMethod(
			MutableQuadViewImpl instance,
			int vertexIndex,
			int color,
			Operation<MutableQuadViewImpl> original,
			@Share("ao") LocalFloatRef ao
	) {
		FrappeMutableQuadView.of(instance)
				.as(MQV_ExtTerrainMaterial.class)
				.frappe$ao(vertexIndex, ao.get());
		return instance;
	}
}
