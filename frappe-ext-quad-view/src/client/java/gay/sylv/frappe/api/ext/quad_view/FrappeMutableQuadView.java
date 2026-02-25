package gay.sylv.frappe.api.ext.quad_view;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.Material;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.Direction;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.util.TriState;

@SuppressWarnings({"unchecked", "NullableProblems"}) // Cast to Q means they're implemented; IJ nullity issue
public interface FrappeMutableQuadView<Q extends MutableQuadView> extends FrappeQuadView<Q>, MutableQuadView {
	static FrappeMutableQuadView<MutableQuadView> of(MutableQuadView quad) {
		return (FrappeMutableQuadView<MutableQuadView>) quad;
	}

	@Override
	Q pos(int vertexIndex, float x, float y, float z);

	@Override
	default Q pos(int vertexIndex, Vector3f pos) {
		return (Q) MutableQuadView.super.pos(
				vertexIndex,
				pos
		);
	}

	@Override
	default Q pos(int vertexIndex, Vector3fc pos) {
		return (Q) MutableQuadView.super.pos(
				vertexIndex,
				pos
		);
	}

	@Override
	Q color(int vertexIndex, int color);

	@Override
	default Q color(int c0, int c1, int c2, int c3) {
		return (Q) MutableQuadView.super.color(
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
		return (Q) MutableQuadView.super.uv(
				vertexIndex,
				uv
		);
	}

	@Override
	default Q uv(int vertexIndex, Vector2fc uv) {
		return (Q) MutableQuadView.super.uv(
				vertexIndex,
				uv
		);
	}

	@Override
	default Q materialBake(Material.Baked material, int bakeFlags) {
		return (Q) MutableQuadView.super.materialBake(
				material,
				bakeFlags
		);
	}

	@Override
	Q lightmap(int vertexIndex, int lightmap);

	@Override
	default Q lightmap(int l0, int l1, int l2, int l3) {
		return (Q) MutableQuadView.super.lightmap(
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
		return (Q) MutableQuadView.super.normal(
				vertexIndex,
				normal
		);
	}

	@Override
	default Q normal(int vertexIndex, Vector3fc normal) {
		return (Q) MutableQuadView.super.normal(
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
	Q itemRenderType(RenderType renderType);

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

	@Override
	Q copyFrom(QuadView quad);

	@Override
	Q fromBakedQuad(BakedQuad quad);
}
