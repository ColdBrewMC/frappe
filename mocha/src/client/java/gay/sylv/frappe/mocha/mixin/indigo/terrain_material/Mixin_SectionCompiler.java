package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import java.util.Map;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadBrightness;
import com.mojang.blaze3d.vertex.QuadLightmapCoords;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.SectionCompiler;

import gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension;

@Mixin(SectionCompiler.class)
public abstract class Mixin_SectionCompiler {
	@Inject(method = {"lambda$compile$0", "lambda$compile$1"}, at = @At("RETURN"))
	private void correctQuadOutput(
			Map<ChunkSectionLayer, BufferBuilder> startedLayers,
			SectionBufferBuilderPack builders,
			PoseStack.Pose pose,
			BakedQuad quad,
			QuadBrightness brightness,
			int color,
			QuadLightmapCoords lightmapCoord,
			int overlayCoords,
			CallbackInfo ci,
			@Local(name = "builder") BufferBuilder builder
	) {
		builder.setUv1(0, 42);
	}

	@Definition(
			id = "BLOCK",
			field = "Lcom/mojang/blaze3d/vertex/DefaultVertexFormat;BLOCK:Lcom/mojang/blaze3d/vertex/VertexFormat;"
	)
	@Expression("BLOCK")
	@ModifyExpressionValue(method = "getOrBeginLayer", at = @At("MIXINEXTRAS:EXPRESSION"))
	private VertexFormat overridePipeline(VertexFormat original) {
		return IndigoTerrainMaterialExtension.MATERIAL_BLOCK;
	}
}
