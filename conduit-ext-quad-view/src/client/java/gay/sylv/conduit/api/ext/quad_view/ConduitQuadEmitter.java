package gay.sylv.conduit.api.ext.quad_view;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.util.TriState;

@SuppressWarnings({"NullableProblems", "unchecked"}) // IJ nullity issue
public interface ConduitQuadEmitter<Q extends QuadEmitter> extends ConduitMutableQuadView<Q>, QuadEmitter {
	static ConduitQuadEmitter<QuadEmitter> of(QuadEmitter emitter) {
		return (ConduitQuadEmitter<QuadEmitter>) emitter;
	}

	@Override
	Q pos(int vertexIndex, float x, float y, float z);

	@Override
	default Q pos(int vertexIndex, Vector3f pos) {
		return (Q) QuadEmitter.super.pos(
				vertexIndex,
				pos
		);
	}

	@Override
	default Q pos(int vertexIndex, Vector3fc pos) {
		return (Q) QuadEmitter.super.pos(
				vertexIndex,
				pos
		);
	}

	@Override
	Q color(int vertexIndex, int color);

	@Override
	default Q color(int c0, int c1, int c2, int c3) {
		return (Q) QuadEmitter.super.color(
				c0,
				c1,
				c2,
				c3
		);
	}

	@Override
	Q uv(int vertexIndex, float u, float v);

	@Override
	default Q uv(int vertexIndex, Vector2f uv) {
		return (Q) QuadEmitter.super.uv(
				vertexIndex,
				uv
		);
	}

	@Override
	default Q uv(int vertexIndex, Vector2fc uv) {
		return (Q) QuadEmitter.super.uv(
				vertexIndex,
				uv
		);
	}

	@Override
	default Q spriteBake(TextureAtlasSprite sprite, int bakeFlags) {
		return (Q) QuadEmitter.super.spriteBake(
				sprite,
				bakeFlags
		);
	}

	@Override
	default Q uvUnitSquare() {
		return (Q) QuadEmitter.super.uvUnitSquare();
	}

	@Override
	Q lightmap(int vertexIndex, int lightmap);

	@Override
	default Q lightmap(int l0, int l1, int l2, int l3) {
		return (Q) QuadEmitter.super.lightmap(
				l0,
				l1,
				l2,
				l3
		);
	}

	@Override
	Q normal(int vertexIndex, float x, float y, float z);

	@Override
	default Q normal(int vertexIndex, Vector3f normal) {
		return (Q) QuadEmitter.super.normal(
				vertexIndex,
				normal
		);
	}

	@Override
	default Q normal(int vertexIndex, Vector3fc normal) {
		return (Q) QuadEmitter.super.normal(
				vertexIndex,
				normal
		);
	}

	@Override
	Q nominalFace(@Nullable Direction face);

	@Override
	Q cullFace(@Nullable Direction face);

	@Override
	Q chunkLayer(@Nullable ChunkSectionLayer layer);

	@Override
	Q emissive(boolean emissive);

	@Override
	Q diffuseShade(boolean shade);

	@Override
	Q ambientOcclusion(TriState ao);

	@Override
	Q foilType(ItemStackRenderState.@Nullable FoilType foilType);

	@Override
	Q shadeMode(ShadeMode mode);

	@Override
	Q atlas(QuadAtlas quadAtlas);

	@Override
	Q tintIndex(int tintIndex);

	@Override
	Q tag(int tag);

	@ApiStatus.NonExtendable
	@Override
	default Q copyFrom(QuadView quad) {
		return this.copyFrom((ConduitQuadView<QuadView>) quad);
	}

	Q copyFrom(ConduitQuadView<QuadView> quad);

	@Override
	Q fromBakedQuad(BakedQuad quad);

	@Override
	default Q square(Direction nominalFace, float left, float bottom, float right, float top, float depth) {
		return (Q) QuadEmitter.super.square(
				nominalFace,
				left,
				bottom,
				right,
				top,
				depth
		);
	}

	@Override
	ConduitQuadEmitter<QuadEmitter> emit();
}
