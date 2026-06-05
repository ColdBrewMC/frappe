/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import java.util.Set;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.opengl.GlProgram;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GlProgram.class)
public abstract class Mixin_GlProgram {
	@Definition(
			id = "BUILT_IN_UNIFORMS",
			field = "Lcom/mojang/blaze3d/opengl/GlProgram;BUILT_IN_UNIFORMS:Ljava/util/Set;"
	)
	@Definition(id = "contains", method = "Ljava/util/Set;contains(Ljava/lang/Object;)Z")
	@Definition(id = "name", local = @Local(type = String.class, name = "name"))
	@Expression("BUILT_IN_UNIFORMS.contains(name)")
	@WrapOperation(method = "setupUniforms", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean excuseFrpUniformBlocks(Set<String> instance, Object o, Operation<Boolean> original) {
		return original.call(instance, o) || ((String) o).startsWith("frp_uniformBlock_");
	}
}
