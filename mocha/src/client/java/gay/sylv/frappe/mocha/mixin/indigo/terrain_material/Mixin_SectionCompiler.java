package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.BLOCK_POS;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(SectionCompiler.class)
public abstract class Mixin_SectionCompiler {
	@WrapOperation(method = "compile", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;tesselateBlock(Lnet/minecraft/client/renderer/block/BlockQuadOutput;FFFLnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/renderer/block/dispatch/BlockStateModel;J)V"
			))
	private void setBlockPos(
			ModelBlockRenderer instance,
			BlockQuadOutput output,
			float x,
			float y,
			float z,
			BlockAndTintGetter level,
			BlockPos pos,
			BlockState blockState,
			BlockStateModel model,
			long seed,
			Operation<Void> original
	) {
		ScopedValue.where(BLOCK_POS, pos)
				.call(() -> original.call(instance, output, x, y, z, level, pos, blockState, model, seed));
	}
}
