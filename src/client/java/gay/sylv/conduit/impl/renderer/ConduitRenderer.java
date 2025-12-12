package gay.sylv.conduit.impl.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.BlockVertexConsumerProvider;

public class ConduitRenderer implements Renderer {
	@Override
	public MutableMesh mutableMesh() {
		return null;
	}

	@Override
	public void render(
			ModelBlockRenderer modelBlockRenderer,
			BlockAndTintGetter blockAndTintGetter,
			BlockStateModel blockStateModel,
			BlockState blockState,
			BlockPos blockPos,
			PoseStack poseStack,
			BlockVertexConsumerProvider blockVertexConsumerProvider,
			boolean b,
			long l,
			int i
	) {

	}

	@Override
	public void render(
			PoseStack.Pose pose,
			BlockVertexConsumerProvider blockVertexConsumerProvider,
			BlockStateModel blockStateModel,
			float v,
			float v1,
			float v2,
			int i,
			int i1,
			BlockAndTintGetter blockAndTintGetter,
			BlockPos blockPos,
			BlockState blockState
	) {

	}

	@Override
	public void renderBlockAsEntity(
			BlockRenderDispatcher blockRenderDispatcher,
			BlockState blockState,
			PoseStack poseStack,
			MultiBufferSource multiBufferSource,
			int i,
			int i1,
			BlockAndTintGetter blockAndTintGetter,
			BlockPos blockPos
	) {

	}

	@Override
	public QuadEmitter getLayerRenderStateEmitter(ItemStackRenderState.LayerRenderState layerRenderState) {
		return null;
	}
}
