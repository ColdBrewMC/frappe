/*
 * Conduit
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
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
	private static TerrainMaterial solidPlastic;

	@Override
	public void onInitializeClient() {
		ResourceKey<Block> key = ResourceKey.create(
				BuiltInRegistries.BLOCK.key(),
				modId("test_block")
		);
		testBlock = Registry.register(
				BuiltInRegistries.BLOCK,
				key,
				new Block(BlockBehaviour.Properties.of().setId(key))
		);
		solidPlastic = TerrainMaterial.Builder.of(modId("plastic_terrain"))
				.build();
		TerrainMaterialExtension.registerMaterial(solidPlastic);
		PreparableModelLoadingPlugin.register(
				(store, executor) -> {
					FileToIdConverter fileToIdConverter = FileToIdConverter.json("models/block/cobblestone");
					return CompletableFuture.supplyAsync(
							() -> fileToIdConverter.listMatchingResources(store.resourceManager())
									.keySet()
									.stream().map(id ->
											id.withPath(s -> s.substring(
													7,
													s.length() - 5
											)))
									.toList(),
							executor
					);
				},
				(data, pluginContext) -> {
					for (Identifier id : data) {
						pluginContext.addModel(ExtraModelKey.create(id::toString), SimpleUnbakedExtraModel.blockStateModel(id));
					}

					pluginContext.modifyBlockModelAfterBake().register((model, context) -> {
						BlockState state = context.state();
						if (!state.is(testBlock)) return model;

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
									FrappeMutableQuadView.of(quad)
											.as(MQV_ExtTerrainMaterial.class)
											.frappe$terrainMaterial(solidPlastic);
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
