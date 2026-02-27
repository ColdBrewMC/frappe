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

public interface FrappeMutableQuadView extends FrappeQuadView, MutableQuadView {
	static FrappeMutableQuadView of(MutableQuadView quad) {
		return (FrappeMutableQuadView) quad;
	}

	@Override
	default FrappeMutableQuadView pos(int vertexIndex, float x, float y, float z) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
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
		throw new UnsupportedOperationException("Implemented via Mixin.");
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
		throw new UnsupportedOperationException("Implemented via Mixin.");
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
	default FrappeMutableQuadView materialBake(Material.Baked material, int bakeFlags) {
		return (FrappeMutableQuadView) MutableQuadView.super.materialBake(
				material,
				bakeFlags
		);
	}

	@Override
	default FrappeMutableQuadView lightmap(int vertexIndex, int lightmap) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
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
		throw new UnsupportedOperationException("Implemented via Mixin.");
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
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView cullFace(@Nullable Direction face) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView chunkLayer(ChunkSectionLayer layer) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView itemRenderType(RenderType renderType) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView emissive(boolean emissive) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView diffuseShade(boolean shade) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView ambientOcclusion(TriState ao) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView foilType(ItemStackRenderState.@Nullable FoilType foilType) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView shadeMode(ShadeMode mode) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView atlas(QuadAtlas quadAtlas) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView tintIndex(int tintIndex) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView tag(int tag) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView copyFrom(QuadView quad) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	@Override
	default FrappeMutableQuadView fromBakedQuad(BakedQuad quad) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}
}
