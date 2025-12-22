package gay.sylv.conduit.api.renderer.mesh;

import com.mojang.blaze3d.pipeline.RenderPipeline;
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

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.util.TriState;

/// An extension of [MutableQuadView].
@ApiStatus.NonExtendable
public interface MutablePrimitiveView extends MutableQuadView, PrimitiveView {
	/// Sets this primitive's [pipeline][RenderPipeline].
	///
	/// **Note:** this resets any data stored in this primitive.
	/// Only use this method *before* defining the quad's properties.
	MutablePrimitiveView pipeline$conduit(RenderPipeline pipeline);

	/// Sets this primitive's [pipeline][RenderPipeline] with
	/// [vertex extensions][VertexExtension].
	///
	/// @see #pipeline$conduit(RenderPipeline)
	MutablePrimitiveView pipeline$conduit(RenderPipeline pipeline, int vertexExtensions);

	/// Sets this primitive's [atlas][TextureAtlas].
	MutablePrimitiveView atlas$conduit(TextureAtlas atlas);

	/// Sets this primitive's [sprite][TextureAtlasSprite].
	MutablePrimitiveView sprite$conduit(TextureAtlasSprite sprite);

	@Override
	default MutablePrimitiveView pos(int vertexIndex, Vector3f pos) {
		return this.pos(vertexIndex, (Vector3fc) pos);
	}

	@Override
	default MutablePrimitiveView pos(int vertexIndex, Vector3fc pos) {
		return this.pos(vertexIndex, pos.x(), pos.y(), pos.z());
	}

	@Override
	default MutablePrimitiveView uv(int vertexIndex, Vector2f uv) {
		return this.uv(vertexIndex, (Vector2fc) uv);
	}

	/// @deprecated Use [#atlas$conduit(TextureAtlas)]
	@Override
	@Deprecated
	default MutablePrimitiveView atlas(QuadAtlas quadAtlas) {
		throw new UnsupportedOperationException("This method is not supported on this type");
	}

	@Override
	MutablePrimitiveView pos(int vertexIndex, float x, float y, float z);

	@Override
	MutablePrimitiveView color(int vertexIndex, int color);

	@Override
	default MutablePrimitiveView color(int c0, int c1, int c2, int c3) {
		return (MutablePrimitiveView) MutableQuadView.super.color(c0, c1, c2, c3);
	}

	@Override
	MutablePrimitiveView uv(int vertexIndex, float u, float v);

	@Override
	default MutablePrimitiveView spriteBake(
			TextureAtlasSprite sprite,
			int bakeFlags
	) {
		return (MutablePrimitiveView) MutableQuadView.super.spriteBake(sprite, bakeFlags);
	}

	@Override
	MutablePrimitiveView lightmap(int vertexIndex, int lightmap);

	@Override
	default MutablePrimitiveView lightmap(int l0, int l1, int l2, int l3) {
		return (MutablePrimitiveView) MutableQuadView.super.lightmap(l0, l1, l2, l3);
	}

	@Override
	MutablePrimitiveView normal(int vertexIndex, float x, float y, float z);

	@Override
	default MutablePrimitiveView normal(int vertexIndex, Vector3f normal) {
		return (MutablePrimitiveView) MutableQuadView.super.normal(vertexIndex, normal);
	}

	@Override
	default MutablePrimitiveView normal(int vertexIndex, Vector3fc normal) {
		return (MutablePrimitiveView) MutableQuadView.super.normal(vertexIndex, normal);
	}

	@Override
	MutablePrimitiveView nominalFace(@Nullable Direction face);

	@Override
	MutablePrimitiveView cullFace(@Nullable Direction face);

	@Override
	MutablePrimitiveView renderLayer(@Nullable ChunkSectionLayer renderLayer);

	@Override
	MutablePrimitiveView emissive(boolean emissive);

	@Override
	MutablePrimitiveView diffuseShade(boolean shade);

	@Override
	MutablePrimitiveView ambientOcclusion(TriState ao);

	@Override
	MutablePrimitiveView glint(ItemStackRenderState.@Nullable FoilType glint);

	@Override
	MutablePrimitiveView shadeMode(ShadeMode mode);

	@Override
	MutablePrimitiveView tintIndex(int tintIndex);

	@Override
	MutablePrimitiveView tag(int tag);

	@Override
	MutablePrimitiveView copyFrom(QuadView quad);

	@Override
	MutablePrimitiveView fromBakedQuad(BakedQuad quad);

	@Override
	default MutablePrimitiveView uv(int vertexIndex, Vector2fc uv) {
		return this.uv(vertexIndex, uv.x(), uv.y());
	}
}
