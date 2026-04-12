/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.caffeinemc.mods.sodium.client.gl.shader.ShaderParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.resources.Identifier;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;

@Mixin(ShaderParser.class)
public abstract class Mixin_ShaderParser {
	@WrapOperation(method = "processImport", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gl/shader/ShaderLoader;getShaderSource(Lnet/minecraft/resources/Identifier;)Ljava/lang/String;"))
	private String processMochaImport(
			Identifier name,
			Operation<String> original
	) {
		if (name.getNamespace().equals("mocha")) {
			if (name.getPath().endsWith("fragment.glsl")) {
				IndigoTerrainMaterialExtension.resolveMaterials(true);
				return IndigoTerrainMaterialExtension.mochaFragmentShader.replace("#version 330", "");
			} else if (name.getPath().endsWith("vertex.glsl")) {
				IndigoTerrainMaterialExtension.resolveMaterials(true);
				return IndigoTerrainMaterialExtension.mochaVertexShader.replace("#version 330", "");
			}
		}

		return original.call(name);
	}
}
