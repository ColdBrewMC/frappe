/*
 * Frappé
 * Copyright (C) 2025 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import static net.minecraft.resources.Identifier.DEFAULT_NAMESPACE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.FileUtil;

import net.fabricmc.fabric.api.resource.v1.pack.ModPackResources;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

import gay.sylv.frappe.api.base.extension.RendererInfo;

public final class TerrainMaterialResourcePack implements PackResources, ModPackResources {
	public static final TerrainMaterialResourcePack INSTANCE = new TerrainMaterialResourcePack();
	public static final PackSelectionConfig SELECTION_CONFIG = new PackSelectionConfig(true, Pack.Position.TOP, true);
	public static final Pack.ResourcesSupplier RESOURCES_SUPPLIER = new Pack.ResourcesSupplier() {
		@Override
		public PackResources openPrimary(PackLocationInfo location) {
			return INSTANCE;
		}

		@Override
		public PackResources openFull(
				PackLocationInfo location,
				Pack.Metadata metadata
		) {
			return INSTANCE;
		}
	};
	private @Nullable ResourceMetadata resourceMetadata;

	@Override
	public @Nullable IoSupplier<InputStream> getRootResource(String... path) {
		FileUtil.validatePath(path);
		Optional<Path> optionalPath = FabricLoader.getInstance().getModContainer("mocha").orElseThrow().findPath("assets/" + String.join("/", path));
		return optionalPath.map(IoSupplier::create).orElse(null);
	}

	@Override
	public @Nullable IoSupplier<InputStream> getResource(
			PackType type,
			Identifier location
	) {
		if (type == PackType.CLIENT_RESOURCES) {
			if (RendererInfo.getModId().equals("fabric-renderer-indigo") && location.getNamespace().equals(DEFAULT_NAMESPACE) && location.getPath().startsWith("shaders/core/terrain")) {
				if (location.getPath().endsWith("terrain.vsh")) {
					return () -> new ByteArrayInputStream(IndigoTerrainMaterialExtension.mochaVertexShader.getBytes(
							StandardCharsets.UTF_8));
				} else if (location.getPath().endsWith("terrain.fsh")) {
					return () -> new ByteArrayInputStream(IndigoTerrainMaterialExtension.mochaFragmentShader.getBytes(
							StandardCharsets.UTF_8));
				}
			}
		}

		return null;
	}

	@Override
	public void listResources(
			PackType type,
			String namespace,
			String directory,
			ResourceOutput output
	) {
		if (RendererInfo.getModId().equals("fabric-renderer-indigo") && namespace.equals(DEFAULT_NAMESPACE) && directory.startsWith("shaders")) {
			output.accept(
					Identifier.withDefaultNamespace("shaders/core/terrain.vsh"), () -> new ByteArrayInputStream(IndigoTerrainMaterialExtension.mochaVertexShader.getBytes(
							StandardCharsets.UTF_8))
			);
			output.accept(
					Identifier.withDefaultNamespace("shaders/core/terrain.fsh"), () -> new ByteArrayInputStream(IndigoTerrainMaterialExtension.mochaFragmentShader.getBytes(
							StandardCharsets.UTF_8))
			);
		}
	}

	@Override
	public Set<String> getNamespaces(PackType type) {
		return Set.of("mocha", "minecraft");
	}

	@Override
	public @Nullable <T> T getMetadataSection(MetadataSectionType<T> metadataSerializer) throws IOException {
		if (this.resourceMetadata == null) {
			this.resourceMetadata = AbstractPackResources.loadMetadata(this);
		}

		return this.resourceMetadata.getSection(metadataSerializer).orElse(null);
	}

	@Override
	public PackLocationInfo location() {
		return new PackLocationInfo("terrain_material", Component.literal("Mocha Terrain Material Resources"), PackSource.BUILT_IN, Optional.empty());
	}

	@Override
	public void close() {
	}

	@Override
	public ModMetadata getFabricModMetadata() {
		return FabricLoader.getInstance().getModContainer("mocha").orElseThrow().getMetadata();
	}

	@Override
	public ModPackResources createOverlay(String overlay) {
		return this;
	}
}
