/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.CUTOUT_LAYERS;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_OPAQUE_CUTOUT;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_OPAQUE_SOLID;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.SOLID_LAYERS;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;

@Mixin(ChunkSectionLayerGroup.class)
public abstract class Mixin_ChunkSectionLayerGroup {
	@Unique
	private static int ordinalOffset;

	@Mutable
	@Shadow
	@Final
	private static ChunkSectionLayerGroup[] $VALUES;

	@Definition(id = "ordinal", local = @Local(type = int.class, argsOnly = true, ordinal = 0))
	@Expression("ordinal")
	@ModifyExpressionValue(method = "<init>", at = @At("MIXINEXTRAS:EXPRESSION"))
	private static int overrideOrdinal(int original, @Local(argsOnly = true) String name) {
		ordinalOffset++;
		return original;
	}

	//CHECKSTYLE.OFF: MatchXpath
	@SuppressWarnings("CheckStyle")
	@Invoker(value = "<init>")
	private static ChunkSectionLayerGroup init(String name, int ordinal, ChunkSectionLayer... layers) {
		throw new UnsupportedOperationException("@Invoker in Mixin");
	}

	//CHECKSTYLE.ON: MatchXpath

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void onClinit(CallbackInfo ci) {
		clinit();
	}

	@Unique
	private static void clinit() {
		IndigoTerrainMaterialExtension.resolveMaterials();

		try {
			Class.forName(ChunkSectionLayer.class.getName(), true, ChunkSectionLayerGroup.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		MOCHA_OPAQUE_SOLID = init(
				"MOCHA_OPAQUE_SOLID",
				ordinalOffset,
				SOLID_LAYERS.toArray(ChunkSectionLayer[]::new)
		);
		MOCHA_OPAQUE_CUTOUT = init(
				"MOCHA_OPAQUE_CUTOUT",
				ordinalOffset,
				CUTOUT_LAYERS.toArray(ChunkSectionLayer[]::new)
		);
		$VALUES = ArrayUtils.addAll($VALUES, MOCHA_OPAQUE_SOLID, MOCHA_OPAQUE_CUTOUT);
	}
}
