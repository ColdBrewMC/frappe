package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;

// It is impossible to make this compatible, so we overwrite the pipelines.
// For simplicity's sake, we assume Mocha is special.
// Mocha's mixins can be disabled, so this should be fine.
// If you are reading this and need to overwrite these pipelines, you are
// likely doing something wrong.
@Mixin(ChunkSectionLayer.class)
public abstract class Mixin_ChunkSectionLayer {
	@Definition(id = "pipeline", local = @Local(type = RenderPipeline.class, argsOnly = true))
	@Expression("pipeline")
	@ModifyExpressionValue(method = "<init>", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private static RenderPipeline overridePipeline(RenderPipeline original) {
		IndigoTerrainMaterialExtension.resolveMaterials();
		RenderPipeline mochaPipeline = IndigoTerrainMaterialExtension.VANILLA_2_MOCHA_TERRAIN_PIPELINES.get(original);
		return mochaPipeline != null ? mochaPipeline : original;
	}
}
