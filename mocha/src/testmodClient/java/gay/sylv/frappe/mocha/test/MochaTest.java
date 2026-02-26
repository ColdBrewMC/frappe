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

import org.jspecify.annotations.Nullable;

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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;
import gay.sylv.frappe.api.ext.terrain_material.MQV_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterialExtension;

public final class MochaTest implements ClientModInitializer {
	private static Block testBlock;
	private static Block testGrassBlock;
	private static Block testGreenGlassBlock;
	private static Item testBlockItem;
	private static Item testGrassBlockItem;
	private static Item testGreenGlassBlockItem;
	private static TerrainMaterial testMaterial;
	private static TerrainMaterial testGreenGlassMaterial;

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
		testMaterial = TerrainMaterial.Builder.of(modId("test_terrain"))
				.build();
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
				new Block(BlockBehaviour.Properties.of().setId(glassKey))
		);
		testGreenGlassBlockItem = Registry.register(
				BuiltInRegistries.ITEM,
				glassItemKey,
				new BlockItem(testGreenGlassBlock, new Item.Properties().setId(glassItemKey).useBlockDescriptionPrefix())
		);
		testGreenGlassMaterial = TerrainMaterial.Builder.of(modId("test_terrain"))
				.simple()
				.build();
		TerrainMaterialExtension.registerMaterial(testMaterial);
		TerrainMaterialExtension.registerMaterial(testGreenGlassMaterial);
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
						if (!state.is(testBlock) && !state.is(testGrassBlock) && !state.is(testGreenGlassBlock)) return model;

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
								emitter.pushTransform(quad -> {
									if (state.is(testBlock)) {
										FrappeMutableQuadView.of(quad)
												.as(MQV_ExtTerrainMaterial.class)
												.frappe$terrainMaterial(testMaterial);
									} else {
										FrappeMutableQuadView.of(quad)
												.as(MQV_ExtTerrainMaterial.class)
												.frappe$terrainMaterial(testGreenGlassMaterial);
									}

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
