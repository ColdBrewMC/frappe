/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.Material;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.Renderer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;
import gay.sylv.frappe.api.ext.terrain_material.MQV_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialExtension;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialRegistryEntrypoint;

public final class MochaTest implements ClientModInitializer, TerrainMaterialRegistryEntrypoint {
	private static Block testBlock;
	private static Block testGrassBlock;
	private static Block testGreenGlassBlock;
	private static Item testBlockItem;
	private static Item testGrassBlockItem;
	private static Item testGreenGlassBlockItem;
	private static TerrainMaterial testMaterial;
	private static TerrainMaterial testGreenGlassMaterial;
	private static GpuBufferSlice dynamicTransforms;
	private static TestDynamicUniforms dynamicUniforms;

	@Override
	public void onTerrainMaterialRegistry() {
		ClientLifecycleEvents.CLIENT_STARTED.register(_ -> {
			dynamicUniforms = new TestDynamicUniforms();
		});
		testMaterial = TerrainMaterial.Builder.of(modId("test_glint"))
				.complexity(TerrainMaterial.Complexity.COMPLEX)
				.build();
		testGreenGlassMaterial = TerrainMaterial.Builder.of(modId("test_terrain"))
				.complexity(TerrainMaterial.Complexity.SIMPLE)
				.build();
		TerrainMaterialExtension.registerMaterial(testMaterial);
		TerrainMaterialExtension.registerMaterial(testGreenGlassMaterial);
	}

	@Override
	public void onInitializeClient() {
		ResourceKey<Block> key = ResourceKey.create(
				BuiltInRegistries.BLOCK.key(),
				modId("test_block")
		);
		ResourceKey<Item> itemKey = ResourceKey.create(
				BuiltInRegistries.ITEM.key(),
				modId("test_block")
		);
		ResourceKey<Block> grassKey = ResourceKey.create(
				BuiltInRegistries.BLOCK.key(),
				modId("test_grass_block")
		);
		ResourceKey<Item> grassItemKey = ResourceKey.create(
				BuiltInRegistries.ITEM.key(),
				modId("test_grass_block")
		);
		ResourceKey<Block> glassKey = ResourceKey.create(
				BuiltInRegistries.BLOCK.key(),
				modId("test_green_glass_block")
		);
		ResourceKey<Item> glassItemKey = ResourceKey.create(
				BuiltInRegistries.ITEM.key(),
				modId("test_green_glass_block")
		);
		testBlock = Registry.register(
				BuiltInRegistries.BLOCK,
				key,
				new Block(BlockBehaviour.Properties.of().setId(key))
		);
		testBlockItem = Registry.register(
				BuiltInRegistries.ITEM,
				itemKey,
				new BlockItem(testBlock, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix())
		);
		testGrassBlock = Registry.register(
				BuiltInRegistries.BLOCK,
				grassKey,
				new Block(BlockBehaviour.Properties.of().setId(grassKey))
		);
		testGrassBlockItem = Registry.register(
				BuiltInRegistries.ITEM,
				grassItemKey,
				new BlockItem(testGrassBlock, new Item.Properties().setId(grassItemKey).useBlockDescriptionPrefix())
		);
		testGreenGlassBlock = Registry.register(
				BuiltInRegistries.BLOCK,
				glassKey,
				new Block(BlockBehaviour.Properties.of().setId(glassKey).noOcclusion())
		);
		testGreenGlassBlockItem = Registry.register(
				BuiltInRegistries.ITEM,
				glassItemKey,
				new BlockItem(testGreenGlassBlock, new Item.Properties().setId(glassItemKey).useBlockDescriptionPrefix())
		);
		PreparableModelLoadingPlugin.register(
				(store, executor) -> {
					FileToIdConverter cobble = FileToIdConverter.json("models/block/cobblestone");
					FileToIdConverter grass = FileToIdConverter.json("models/block/grass_block");
					FileToIdConverter glass = FileToIdConverter.json("models/block/lime_stained_glass");
					return CompletableFuture.supplyAsync(
							() -> Stream.concat(Stream.concat(
									cobble.listMatchingResources(store.resourceManager())
											.keySet()
											.stream().map(id ->
													id.withPath(s -> s.substring(
															7,
															s.length() - 5
													))),
									grass.listMatchingResources(store.resourceManager())
											.keySet()
											.stream().map(id ->
													id.withPath(s -> s.substring(
															7,
															s.length() - 5
													)))
							), glass.listMatchingResources(store.resourceManager())
											.keySet()
											.stream().map(id ->
													id.withPath(s -> s.substring(
															7,
															s.length() - 5
													)))).toList(),
							executor
					);
				},
				(data, pluginContext) -> {
					for (Identifier id : data) {
						pluginContext.addModel(ExtraModelKey.create(id::toString), SimpleUnbakedExtraModel.blockStateModel(id));
					}

					pluginContext.modifyBlockModelAfterBake().register((model, context) -> {
						BlockState state = context.state();
						if (!state.is(testBlock) && !state.is(testGrassBlock) && !state.is(testGreenGlassBlock) && !state.is(Blocks.NETHERITE_BLOCK)) return model;

						return new WrapperBlockStateModel(model) {
							@Override
							public void emitQuads(
									QuadEmitter emitter,
									BlockAndTintGetter level,
									BlockPos pos,
									BlockState state,
									RandomSource random,
									Predicate<@Nullable Direction> cullTest
							) {
								TextureAtlas atlas = Minecraft.getInstance()
										.getAtlasManager()
										.getAtlasOrThrow(QuadAtlas.BLOCK.getId());
								TextureAtlasSprite glintSprite = atlas.getSprite(modId("block/enchanted_glint_terrain"));
								final MutableMesh glintMesh = Renderer.get().mutableMesh();
								final QuadEmitter glintQuad = glintMesh.emitter();

								emitter.pushTransform(quad -> {
									MQV_ExtTerrainMaterial materialQuad = FrappeMutableQuadView.of(quad)
											.as(MQV_ExtTerrainMaterial.class);

									glintQuad.copyFrom(quad);

									if (state.is(testBlock) || state.is(Blocks.NETHERITE_BLOCK)) {
										// Find the minimum and maximum UV's.
										// A simulation of the algorithm is provided in comments below.
										int minUi = -1;
										int minVi = -1;
										int maxUi = -1;
										int maxVi = -1;
										float minU = Float.MAX_VALUE; // 7, 7, 5, 3
										float minV = Float.MAX_VALUE; // 9, 8, 8, 8
										float maxU = 0; // 7, 12, 12, 12
										float maxV = 0; // 9, 9,  10, 13

										// u, v
										// 7, 9
										// 12,8
										// 5, 10
										// 3, 13

										glintQuad.materialBake(new Material.Baked(glintSprite, false), MutableQuadView.BAKE_LOCK_UV);
										Vector3fc normal = glintQuad.faceNormal();
										Direction direction = Direction.getApproximateNearest(normal.x(), normal.y(), normal.z());

										//CHECKSTYLE.OFF: MatchXpath
										for (int i = 0; i < 4; i++) {
											float u = 0;
											float v = 0;

											switch (direction) {
												case UP, DOWN -> {
													u = glintQuad.x(i);
													v = glintQuad.z(i);
												}
												case SOUTH, NORTH -> {
													u = glintQuad.x(i);
													v = glintQuad.y(i);
												}
												case WEST, EAST -> {
													u = glintQuad.z(i);
													v = glintQuad.y(i);
												}
											}

											if (u < minU) {
												minUi = i;
												minU = u;
											}

											if (u > maxU) {
												maxUi = i;
												maxU = u;
											}

											if (v < minV) {
												minVi = i;
												minV = v;
											}

											if (v > maxV) {
												maxVi = i;
												maxV = v;
											}
										}

										maxU -= (maxU - minU) / 2.0f;
										maxV -= (maxV - minV) / 2.0f;
										minU += (maxU - minU) / 2.0f;
										minV += (maxV - minV) / 2.0f;

										switch (direction) {
											case UP, DOWN -> {
												glintQuad.pos(maxUi, maxU, glintQuad.y(maxUi), glintQuad.z(maxUi));
												glintQuad.pos(maxVi, glintQuad.x(maxVi), glintQuad.y(maxVi), maxV);
												glintQuad.pos(minUi, minU, glintQuad.y(minUi), glintQuad.z(minUi));
												glintQuad.pos(minVi, glintQuad.x(minVi), glintQuad.y(minVi), minV);
											}
											case SOUTH, NORTH -> {
												glintQuad.pos(maxUi, maxU, glintQuad.y(maxUi), glintQuad.z(maxUi));
												glintQuad.pos(maxVi, glintQuad.x(maxVi), maxV, glintQuad.z(maxVi));
												glintQuad.pos(minUi, minU, glintQuad.y(minUi), glintQuad.z(minUi));
												glintQuad.pos(minVi, glintQuad.x(minVi), minV, glintQuad.z(minVi));
											}
											case EAST, WEST -> {
												glintQuad.pos(maxUi, glintQuad.x(maxUi), glintQuad.y(maxUi), maxU);
												glintQuad.pos(maxVi, glintQuad.x(maxVi), maxV, glintQuad.z(maxVi));
												glintQuad.pos(minUi, glintQuad.x(minUi), glintQuad.y(minUi), minU);
												glintQuad.pos(minVi, glintQuad.x(minVi), minV, glintQuad.z(minVi));
											}
										}

										//CHECKSTYLE.ON: MatchXpath

										float scale = 1.0625f;
										materialQuad
												.frappe$terrainMaterial(testMaterial)
												.frappe$uv(
														0,
														glintQuad.u(0),
														glintQuad.v(0)
												)
												.frappe$uv(
														1,
														glintQuad.u(1),
														glintQuad.v(1) - (glintQuad.v(1) - glintQuad.v(0)) / scale
												)
												.frappe$uv(
														2,
														glintQuad.u(2) - (glintQuad.u(2) - glintQuad.u(0)) / scale,
														glintQuad.v(2) - (glintQuad.v(2) - glintQuad.v(3)) / scale
												)
												.frappe$uv(
														3,
														glintQuad.u(3) - (glintQuad.u(3) - glintQuad.u(1)) / scale,
														glintQuad.v(3)
												);
									} else {
										materialQuad.frappe$terrainMaterial(testGreenGlassMaterial);
									}

									glintQuad.emit();
									return true;
								});
								super.emitQuads(
										emitter,
										level,
										pos,
										state,
										random,
										cullTest
								);
								emitter.popTransform();
								glintMesh.clear();
							}
						};
					});
				}
		);
	}

	private Identifier modId(String path) {
		return Identifier.fromNamespaceAndPath("mocha-testmod", path);
	}
}
