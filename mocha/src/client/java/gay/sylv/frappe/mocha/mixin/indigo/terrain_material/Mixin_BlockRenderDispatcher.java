package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadBrightness;
import com.mojang.blaze3d.vertex.QuadLightmapCoords;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;

@Mixin(BlockRenderDispatcher.class)
public abstract class Mixin_BlockRenderDispatcher {
	@Inject(method = "lambda$createQuadOutput$0", at = @At("RETURN"))
	private static void correctUv1(
			MultiBufferSource bufferSource,
			PoseStack.Pose pose,
			BakedQuad quad,
			QuadBrightness brightness,
			int color,
			QuadLightmapCoords lightmapCoord,
			int overlayCoords,
			CallbackInfo ci,
			@Local(name = "buffer") VertexConsumer buffer
	) {
		buffer.setUv1(0, 42);
	}
}
