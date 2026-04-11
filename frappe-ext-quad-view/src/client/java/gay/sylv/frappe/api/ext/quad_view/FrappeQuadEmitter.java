/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.quad_view;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material.Baked;
import net.minecraft.core.Direction;

import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.util.TriState;

public interface FrappeQuadEmitter extends FrappeMutableQuadView, QuadEmitter {
	static FrappeQuadEmitter of(QuadEmitter emitter) {
		return (FrappeQuadEmitter) emitter;
	}

	@Override
	default FrappeQuadEmitter pos(int vertexIndex, float x, float y, float z) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).pos(vertexIndex, x, y, z);
	}

	@Override
	default FrappeQuadEmitter pos(int vertexIndex, Vector3f pos) {
		return (FrappeQuadEmitter) QuadEmitter.super.pos(
				vertexIndex,
				pos
		);
	}

	@Override
	default FrappeQuadEmitter pos(int vertexIndex, Vector3fc pos) {
		return (FrappeQuadEmitter) QuadEmitter.super.pos(
				vertexIndex,
				pos
		);
	}

	@Override
	default FrappeQuadEmitter color(int vertexIndex, int color) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).color(vertexIndex, color);
	}

	@Override
	default FrappeQuadEmitter color(int c0, int c1, int c2, int c3) {
		return (FrappeQuadEmitter) QuadEmitter.super.color(
				c0,
				c1,
				c2,
				c3
		);
	}

	@Override
	default FrappeQuadEmitter uv(int vertexIndex, float u, float v) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).uv(vertexIndex, u, v);
	}

	@Override
	default FrappeQuadEmitter uv(int vertexIndex, Vector2f uv) {
		return (FrappeQuadEmitter) QuadEmitter.super.uv(
				vertexIndex,
				uv
		);
	}

	@Override
	default FrappeQuadEmitter uv(int vertexIndex, Vector2fc uv) {
		return (FrappeQuadEmitter) QuadEmitter.super.uv(
				vertexIndex,
				uv
		);
	}

	@Override
	default FrappeQuadEmitter fromBakedQuad(BakedQuad quad) {
		return (FrappeQuadEmitter) FrappeMutableQuadView.super.fromBakedQuad(quad);
	}

	@Override
	default FrappeQuadEmitter materialBake(
			Baked material,
			int bakeFlags
	) {
		return (FrappeQuadEmitter) FrappeMutableQuadView.super.materialBake(material, bakeFlags);
	}

	@Override
	default FrappeQuadEmitter multiplyColor(int color) {
		return (FrappeQuadEmitter) FrappeMutableQuadView.super.multiplyColor(color);
	}

	@Override
	default FrappeQuadEmitter translate(
			float x,
			float y,
			float z
	) {
		return (FrappeQuadEmitter) FrappeMutableQuadView.super.translate(x, y, z);
	}

	@Override
	default FrappeQuadEmitter uvUnitSquare() {
		return (FrappeQuadEmitter) QuadEmitter.super.uvUnitSquare();
	}

	@Override
	default FrappeQuadEmitter lightmap(int vertexIndex, int lightmap) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).lightmap(vertexIndex, lightmap);
	}

	@Override
	default FrappeQuadEmitter lightmap(int l0, int l1, int l2, int l3) {
		return (FrappeQuadEmitter) QuadEmitter.super.lightmap(
				l0,
				l1,
				l2,
				l3
		);
	}

	@Override
	default FrappeQuadEmitter normal(int vertexIndex, float x, float y, float z) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).normal(vertexIndex, x, y, z);
	}

	@Override
	default FrappeQuadEmitter normal(int vertexIndex, Vector3f normal) {
		return (FrappeQuadEmitter) QuadEmitter.super.normal(
				vertexIndex,
				normal
		);
	}

	@Override
	default FrappeQuadEmitter normal(int vertexIndex, Vector3fc normal) {
		return (FrappeQuadEmitter) QuadEmitter.super.normal(
				vertexIndex,
				normal
		);
	}

	@Override
	default FrappeQuadEmitter nominalFace(@Nullable Direction face) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).nominalFace(face);
	}

	@Override
	default FrappeQuadEmitter cullFace(@Nullable Direction face) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).cullFace(face);
	}

	@Override
	default FrappeQuadEmitter chunkLayer(ChunkSectionLayer layer) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).chunkLayer(layer);
	}

	@Override
	default FrappeQuadEmitter itemRenderType(RenderType renderType) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).itemRenderType(renderType);
	}

	@Override
	default FrappeQuadEmitter emissive(boolean emissive) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).emissive(emissive);
	}

	@Override
	default FrappeQuadEmitter diffuseShade(boolean shade) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).diffuseShade(shade);
	}

	@Override
	default FrappeQuadEmitter ambientOcclusion(TriState ao) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).ambientOcclusion(ao);
	}

	@Override
	default FrappeQuadEmitter foilType(ItemStackRenderState.@Nullable FoilType foilType) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).foilType(foilType);
	}

	@Override
	default FrappeQuadEmitter shadeMode(ShadeMode mode) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).shadeMode(mode);
	}

	@Override
	default FrappeQuadEmitter atlas(QuadAtlas quadAtlas) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).atlas(quadAtlas);
	}

	@Override
	default FrappeQuadEmitter tintIndex(int tintIndex) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).tintIndex(tintIndex);
	}

	@Override
	default FrappeQuadEmitter tag(int tag) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).tag(tag);
	}

	@ApiStatus.NonExtendable
	@Override
	default FrappeQuadEmitter copyFrom(QuadView quad) {
		return this.copyFrom((FrappeQuadView) quad);
	}

	default FrappeQuadEmitter copyFrom(FrappeQuadView quad) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).copyFrom(quad);
	}

	@Override
	default FrappeQuadEmitter square(Direction nominalFace, float left, float bottom, float right, float top, float depth) {
		return (FrappeQuadEmitter) QuadEmitter.super.square(
				nominalFace,
				left,
				bottom,
				right,
				top,
				depth
		);
	}

	@Override
	default FrappeQuadEmitter emit() {
		return (FrappeQuadEmitter) ((QuadEmitter) this).emit();
	}

	@Override
	default FrappeQuadEmitter postMaterialBake(Baked material) {
		return (FrappeQuadEmitter) QuadEmitter.super.postMaterialBake(material);
	}

	@Override
	default FrappeQuadEmitter minLightmap(int lightmap) {
		return (FrappeQuadEmitter) QuadEmitter.super.minLightmap(lightmap);
	}

	@Override
	default FrappeQuadEmitter animated(boolean animated) {
		return (FrappeQuadEmitter) ((QuadEmitter) this).animated(animated);
	}

	@Override
	default FrappeQuadEmitter clear() {
		return (FrappeQuadEmitter) ((QuadEmitter) this).clear();
	}
}
