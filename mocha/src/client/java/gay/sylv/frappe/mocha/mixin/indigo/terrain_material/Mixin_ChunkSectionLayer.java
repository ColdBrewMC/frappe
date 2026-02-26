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
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_SOLID;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.VANILLA_2_MOCHA_TERRAIN_PIPELINES;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.VANILLA_2_SIMPLE_MOCHA_TERRAIN_PIPELINES;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;

// It is impossible to make this compatible, so we enum extend.
// For simplicity's sake, we assume Mocha is special.
// Mocha's mixins can be disabled, so this should be fine.
// If you are reading this and need to enum extend, you are
// likely doing something wrong.
@Mixin(ChunkSectionLayer.class)
public abstract class Mixin_ChunkSectionLayer {
	@Unique
	private static int offset;

	@Mutable
	@Shadow
	@Final
	private static ChunkSectionLayer[] $VALUES;

	@Shadow
	@Final
	public static ChunkSectionLayer SOLID;

	@Shadow
	@Final
	public static ChunkSectionLayer CUTOUT;

	@Shadow
	@Final
	public static ChunkSectionLayer TRANSLUCENT;

	@Definition(id = "pipeline", local = @Local(type = RenderPipeline.class, argsOnly = true))
	@Expression("pipeline")
	@ModifyExpressionValue(method = "<init>", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private static RenderPipeline overridePipeline(RenderPipeline original, @Local(argsOnly = true) String name) {
		if (name.equals("SOLID") || name.equals("CUTOUT") || name.equals("TRANSLUCENT")) {
			IndigoTerrainMaterialExtension.resolveMaterials();
			RenderPipeline mochaPipeline = VANILLA_2_SIMPLE_MOCHA_TERRAIN_PIPELINES.get(original);
			return mochaPipeline != null ? mochaPipeline : original;
		} else {
			return original;
		}
	}

	@Definition(id = "ordinal", local = @Local(type = int.class, argsOnly = true, ordinal = 0))
	@Expression("ordinal")
	@ModifyExpressionValue(method = "<init>", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private static int overrideOrdinal(int ordinal, @Local(argsOnly = true) String name) {
		if (name.equals("SOLID") || name.equals("CUTOUT")) {
			offset++;
		}

		return ordinal + offset;
	}

	@SuppressWarnings("CheckStyle")
	@Invoker(value = "<init>")
	private static ChunkSectionLayer init(
			String name,
			int ordinal,
			RenderPipeline pipeline,
			int bufferSize,
			boolean translucent
	) {
		throw new UnsupportedOperationException("@Invoker in Mixin");
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void onClinit(CallbackInfo ci) {
		IndigoTerrainMaterialExtension.resolveMaterials();
		RenderPipeline solid = VANILLA_2_MOCHA_TERRAIN_PIPELINES.get(RenderPipelines.SOLID_TERRAIN);
		RenderPipeline cutout = VANILLA_2_MOCHA_TERRAIN_PIPELINES.get(RenderPipelines.CUTOUT_TERRAIN);
		offset = 0;
		MOCHA_SOLID = init(
				"MOCHA_SOLID",
				0,
				solid,
				SOLID.bufferSize(),
				SOLID.translucent()
		);
		MOCHA_CUTOUT = init(
				"MOCHA_CUTOUT",
				2,
				cutout,
				CUTOUT.bufferSize(),
				CUTOUT.translucent()
		);

		$VALUES = new ChunkSectionLayer[]{MOCHA_SOLID, SOLID, MOCHA_CUTOUT, CUTOUT, TRANSLUCENT};
	}
}
