package gay.sylv.conduit.api.renderer.mesh;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadTransform;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.util.TriState;

/// An extension of [QuadEmitter].
@ApiStatus.NonExtendable
public interface PrimitiveEmitter extends QuadEmitter, MutablePrimitiveView {
	@Override
	PrimitiveEmitter atlas$conduit(TextureAtlas atlas);

	@Override
	PrimitiveEmitter sprite$conduit(TextureAtlasSprite sprite);

	@Override
	PrimitiveEmitter pos(int vertexIndex, float x, float y, float z);

	@Override
	default PrimitiveEmitter pos(int vertexIndex, Vector3f pos) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.pos(vertexIndex, pos);
	}

	@Override
	default PrimitiveEmitter pos(int vertexIndex, Vector3fc pos) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.pos(vertexIndex, pos);
	}

	@Override
	PrimitiveEmitter color(int vertexIndex, int color);

	@Override
	default PrimitiveEmitter color(int c0, int c1, int c2, int c3) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.color(c0, c1, c2, c3);
	}

	@Override
	PrimitiveEmitter uv(int vertexIndex, float u, float v);

	@Override
	default PrimitiveEmitter uv(int vertexIndex, Vector2f uv) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.uv(vertexIndex, uv);
	}

	@Override
	default PrimitiveEmitter uv(int vertexIndex, Vector2fc uv) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.uv(vertexIndex, uv);
	}

	@Override
	default PrimitiveEmitter atlas(QuadAtlas quadAtlas) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.atlas(quadAtlas);
	}

	@Override
	default PrimitiveEmitter spriteBake(TextureAtlasSprite sprite, int bakeFlags) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.spriteBake(sprite, bakeFlags);
	}

	@Override
	default PrimitiveEmitter uvUnitSquare() {
		return (PrimitiveEmitter) QuadEmitter.super.uvUnitSquare();
	}

	@Override
	PrimitiveEmitter lightmap(int vertexIndex, int lightmap);

	@Override
	default PrimitiveEmitter lightmap(int l0, int l1, int l2, int l3) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.lightmap(l0, l1, l2, l3);
	}

	@Override
	PrimitiveEmitter normal(int vertexIndex, float x, float y, float z);

	@Override
	default PrimitiveEmitter normal(int vertexIndex, Vector3f normal) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.normal(vertexIndex, normal);
	}

	@Override
	default PrimitiveEmitter normal(int vertexIndex, Vector3fc normal) {
		return (PrimitiveEmitter) MutablePrimitiveView.super.normal(vertexIndex, normal);
	}

	@Override
	PrimitiveEmitter nominalFace(@Nullable Direction face);

	@Override
	PrimitiveEmitter cullFace(@Nullable Direction face);

	@Override
	PrimitiveEmitter renderLayer(@Nullable ChunkSectionLayer renderLayer);

	@Override
	PrimitiveEmitter emissive(boolean emissive);

	@Override
	PrimitiveEmitter diffuseShade(boolean shade);

	@Override
	PrimitiveEmitter ambientOcclusion(TriState ao);

	@Override
	PrimitiveEmitter glint(ItemStackRenderState.@Nullable FoilType foilType);

	@Override
	PrimitiveEmitter shadeMode(ShadeMode mode);

	@Override
	PrimitiveEmitter tintIndex(int tintIndex);

	@Override
	PrimitiveEmitter tag(int tag);

	@Override
	PrimitiveEmitter copyFrom(QuadView quad);

	@Override
	PrimitiveEmitter fromBakedQuad(BakedQuad quad);

	@Override
	default PrimitiveEmitter square(
			Direction nominalFace,
			float left,
			float bottom,
			float right,
			float top,
			float depth
	) {
		return (PrimitiveEmitter) QuadEmitter.super.square(
				nominalFace,
				left,
				bottom,
				right,
				top,
				depth
		);
	}

	/// @deprecated Use [#pushTransform(PrimitiveTransform)]
	@Override
	@Deprecated
	default void pushTransform(QuadTransform transform) {
		this.pushTransform((PrimitiveTransform) transform);
	}

	void pushTransform(PrimitiveTransform transform);

	@Override
	void popTransform();

	@Override
	PrimitiveEmitter emit();
}
