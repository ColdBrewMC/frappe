/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.quad_view;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.util.TriState;

public interface FrappeMutableQuadView extends FrappeQuadView, MutableQuadView {
	static FrappeMutableQuadView of(MutableQuadView quad) {
		return (FrappeMutableQuadView) quad;
	}

	@Override
	default FrappeMutableQuadView pos(int vertexIndex, float x, float y, float z) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).pos(vertexIndex, x, y, z);
	}

	@Override
	default FrappeMutableQuadView pos(int vertexIndex, Vector3f pos) {
		return (FrappeMutableQuadView) MutableQuadView.super.pos(
				vertexIndex,
				pos
		);
	}

	@Override
	default FrappeMutableQuadView pos(int vertexIndex, Vector3fc pos) {
		return (FrappeMutableQuadView) MutableQuadView.super.pos(
				vertexIndex,
				pos
		);
	}

	@Override
	default FrappeMutableQuadView color(int vertexIndex, int color) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).color(vertexIndex, color);
	}

	@Override
	default FrappeMutableQuadView color(int c0, int c1, int c2, int c3) {
		return (FrappeMutableQuadView) MutableQuadView.super.color(
				c0,
				c1,
				c2,
				c3
		);
	}

	@Override
	default FrappeMutableQuadView uv(int vertexIndex, float u, float v) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).uv(vertexIndex, u, v);
	}

	@Override
	default FrappeMutableQuadView uv(int vertexIndex, Vector2f uv) {
		return (FrappeMutableQuadView) MutableQuadView.super.uv(
				vertexIndex,
				uv
		);
	}

	@Override
	default FrappeMutableQuadView uv(int vertexIndex, Vector2fc uv) {
		return (FrappeMutableQuadView) MutableQuadView.super.uv(
				vertexIndex,
				uv
		);
	}

	@Override
	default FrappeMutableQuadView translate(
			float x,
			float y,
			float z
	) {
		return (FrappeMutableQuadView) MutableQuadView.super.translate(x, y, z);
	}

	@Override
	default FrappeMutableQuadView multiplyColor(int color) {
		return (FrappeMutableQuadView) MutableQuadView.super.multiplyColor(color);
	}

	@Override
	default FrappeMutableQuadView uvUnitSquare() {
		return (FrappeMutableQuadView) MutableQuadView.super.uvUnitSquare();
	}

	@Override
	default MutableQuadView postMaterialBake(Material.Baked material) {
		return MutableQuadView.super.postMaterialBake(material);
	}

	@Override
	default MutableQuadView minLightmap(int lightmap) {
		return MutableQuadView.super.minLightmap(lightmap);
	}

	@Override
	MutableQuadView animated(boolean animated);

	@Override
	MutableQuadView clear();

	@Override
	default MutableQuadView square(
			Direction nominalFace,
			float left,
			float bottom,
			float right,
			float top,
			float depth
	) {
		return MutableQuadView.super.square(nominalFace, left, bottom, right, top, depth);
	}

	@Override
	default FrappeMutableQuadView materialBake(Material.Baked material, int bakeFlags) {
		return (FrappeMutableQuadView) MutableQuadView.super.materialBake(
				material,
				bakeFlags
		);
	}

	@Override
	default FrappeMutableQuadView lightmap(int vertexIndex, int lightmap) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).lightmap(vertexIndex, lightmap);
	}

	@Override
	default FrappeMutableQuadView lightmap(int l0, int l1, int l2, int l3) {
		return (FrappeMutableQuadView) MutableQuadView.super.lightmap(
				l0,
				l1,
				l2,
				l3
		);
	}

	@Override
	default FrappeMutableQuadView normal(int vertexIndex, float x, float y, float z) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).normal(vertexIndex, x, y, z);
	}

	@Override
	default FrappeMutableQuadView normal(int vertexIndex, Vector3f normal) {
		return (FrappeMutableQuadView) MutableQuadView.super.normal(
				vertexIndex,
				normal
		);
	}

	@Override
	default FrappeMutableQuadView normal(int vertexIndex, Vector3fc normal) {
		return (FrappeMutableQuadView) MutableQuadView.super.normal(
				vertexIndex,
				normal
		);
	}

	@Override
	default FrappeMutableQuadView nominalFace(@Nullable Direction face) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).nominalFace(face);
	}

	@Override
	default FrappeMutableQuadView cullFace(@Nullable Direction face) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).cullFace(face);
	}

	@Override
	default FrappeMutableQuadView chunkLayer(ChunkSectionLayer layer) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).chunkLayer(layer);
	}

	@Override
	default FrappeMutableQuadView itemRenderType(RenderType renderType) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).itemRenderType(renderType);
	}

	@Override
	default FrappeMutableQuadView emissive(boolean emissive) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).emissive(emissive);
	}

	@Override
	default FrappeMutableQuadView diffuseShade(boolean shade) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).diffuseShade(shade);
	}

	@Override
	default FrappeMutableQuadView ambientOcclusion(TriState ao) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).ambientOcclusion(ao);
	}

	@Override
	default FrappeMutableQuadView foilType(ItemStackRenderState.@Nullable FoilType foilType) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).foilType(foilType);
	}

	@Override
	default FrappeMutableQuadView shadeMode(ShadeMode mode) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).shadeMode(mode);
	}

	@Override
	default FrappeMutableQuadView atlas(QuadAtlas quadAtlas) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).atlas(quadAtlas);
	}

	@Override
	default FrappeMutableQuadView tintIndex(int tintIndex) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).tintIndex(tintIndex);
	}

	@Override
	default FrappeMutableQuadView tag(int tag) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).tag(tag);
	}

	@Override
	default FrappeMutableQuadView copyFrom(QuadView quad) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).copyFrom(quad);
	}

	@Override
	default FrappeMutableQuadView fromBakedQuad(BakedQuad quad) {
		return (FrappeMutableQuadView) ((MutableQuadView) this).fromBakedQuad(quad);
	}
}
