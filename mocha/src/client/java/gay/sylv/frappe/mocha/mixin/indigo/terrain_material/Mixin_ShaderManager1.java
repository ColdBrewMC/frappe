/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import java.io.Reader;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.resources.Identifier;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;

// This is used to transform Mocha's template shaders.
@Mixin(targets = "net.minecraft.client.renderer.ShaderManager$1")
public abstract class Mixin_ShaderManager1 {
	@WrapOperation(method = "applyImport", at = @At(
			value = "INVOKE",
			target = "Lorg/apache/commons/io/IOUtils;toString(Ljava/io/Reader;)Ljava/lang/String;"
			))
	private String processMochaImport(Reader sw, Operation<String> original, @Local(name = "location") Identifier location) {
		String origShader = original.call(sw);

		if (location.getNamespace().equals("mocha") && location.getPath().endsWith("fragment.glsl")) {
			IndigoTerrainMaterialExtension.resolveMaterials();
			return IndigoTerrainMaterialExtension.mochaFragmentShader;
		}

		return origShader;
	}
}
