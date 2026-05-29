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
import com.mojang.blaze3d.shaders.ShaderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;

@Mixin(ShaderManager.class)
public abstract class Mixin_ShaderManager {
	@WrapOperation(method = "loadShader", at = @At(
			value = "INVOKE",
			target = "Lorg/apache/commons/io/IOUtils;toString(Ljava/io/Reader;)Ljava/lang/String;"
			))
	private static String useMochaShaders(
			Reader reader,
			Operation<String> original,
			@Local(name = "location", argsOnly = true) Identifier location,
			@Local(name = "type", argsOnly = true) ShaderType type
	) {
		if (location.getPath().startsWith("terrain") && location.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
			return switch (type) {
				case VERTEX -> IndigoTerrainMaterialExtension.mochaVertexShader;
				case FRAGMENT -> IndigoTerrainMaterialExtension.mochaFragmentShader;
			};
		}

		return original.call(reader);
	}
}
