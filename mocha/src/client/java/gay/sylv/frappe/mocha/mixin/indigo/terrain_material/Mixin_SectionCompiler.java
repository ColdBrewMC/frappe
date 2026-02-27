/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_CUTOUT;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.SectionCompiler;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;
import gay.sylv.frappe.mocha.impl.indigo.vertex.format.MochaVertexFormats;

@Mixin(SectionCompiler.class)
public abstract class Mixin_SectionCompiler {
	@Definition(
			id = "BLOCK",
			field = "Lcom/mojang/blaze3d/vertex/DefaultVertexFormat;BLOCK:Lcom/mojang/blaze3d/vertex/VertexFormat;"
	)
	@Expression("BLOCK")
	@ModifyExpressionValue(method = "getOrBeginLayer", at = @At("MIXINEXTRAS:EXPRESSION"))
	private VertexFormat overridePipeline(VertexFormat original, @Local(name = "renderType") ChunkSectionLayer layer) {
		if (layer.equals(IndigoTerrainMaterialExtension.MOCHA_SOLID) || layer.equals(MOCHA_CUTOUT)) {
			return MochaVertexFormats.COMPLEX_TERRAIN;
		} else {
			return MochaVertexFormats.SIMPLE_TERRAIN;
		}
	}
}
