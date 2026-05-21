/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.base;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;

import gay.sylv.frappe.api.base.extension.MCPFriendly;
import gay.sylv.frappe.api.base.extension.RendererExtensionMetadata;
import gay.sylv.frappe.api.base.extension.SupportTier;
import gay.sylv.frappe.impl.base.extension.RendererExtensionMetadataImpl;

@MCPFriendly
public final class FrappeExtensionMetadataUtil {
	private static final Properties properties = new Properties();
	public static final List<RendererExtensionMetadata> METADATA = new ArrayList<>();
	public static final Map<String, RendererExtensionMetadata> ID_2_METADATA = new HashMap<>();

	private FrappeExtensionMetadataUtil() {
	}

	static {
		Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();

		for (ModContainer mod : mods) {
			ModMetadata modMetadata = mod.getMetadata();

			if (modMetadata.containsCustomValue("frappe")) {
				CustomValue frappe = modMetadata.getCustomValue("frappe");

				if (frappe.getType().equals(CustomValue.CvType.OBJECT)) {
					if (processConfigOverride(modMetadata, frappe)) {
						continue;
					}
				} else if (frappe.getType().equals(CustomValue.CvType.ARRAY)) {
					CustomValue.CvArray frappeArray = frappe.getAsArray();

					for (CustomValue value : frappeArray) {
						if (value.getType().equals(CustomValue.CvType.OBJECT)) {
							// don't continue because there may be non-override metadata in this array
							processConfigOverride(modMetadata, value);
						}
					}
				}

				Collection<RendererExtensionMetadata> metadataCollection = RendererExtensionMetadata.fromModMetadata(modMetadata);
				METADATA.addAll(metadataCollection);

				for (RendererExtensionMetadata metadata : metadataCollection) {
					ID_2_METADATA.put(metadata.id(), metadata);
				}
			}
		}

		for (RendererExtensionMetadata metadata : METADATA) {
			if (Boolean.parseBoolean(properties.getProperty(metadata.id() + ".enabled", Boolean.toString(metadata.enabled())))) {
				properties.putAll(metadata.config());
			}
		}

		try (InputStream inputStream = Files.newInputStream(FabricLoader.getInstance().getConfigDir().resolve("frappe.properties"))) {
			properties.load(inputStream);
		} catch (NoSuchFileException _) {
			// ignored
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean processConfigOverride(ModMetadata modMetadata, CustomValue frappe) {
		CustomValue.CvObject frappeObj = frappe.getAsObject();

		if (propertyExists(frappeObj, "override", CustomValue.CvType.BOOLEAN) && frappeObj.get("override").getAsBoolean()) {
			checkSchemaVersion(frappeObj, " in mod " + modMetadata.getName());
			properties.putAll(getConfig(frappeObj));
			return true;
		}

		return false;
	}

	public static boolean getBooleanProperty(String key, boolean orElse) {
		return Boolean.parseBoolean(properties.getProperty(key, Boolean.toString(orElse)));
	}

	private static Map<String, String> traverseProperties(CustomValue.CvObject value, @Nullable String key, Map<String, String> stringMap) {
		for (Map.Entry<String, CustomValue> entry : value) {
			String key1;

			if (key != null) {
				key1 = key + "." + entry.getKey();
			} else {
				key1 = entry.getKey();
			}

			CustomValue value1 = entry.getValue();

			switch (value1.getType()) {
				case STRING -> stringMap.put(key1, value1.getAsString());
				case NUMBER -> stringMap.put(key1, Float.toString(value1.getAsNumber().floatValue()));
				case BOOLEAN -> stringMap.put(key1, Boolean.toString(value1.getAsBoolean()));
				case OBJECT -> traverseProperties(value1.getAsObject(), key1, stringMap);
				default -> {
				}
			}
		}

		return stringMap;
	}

	public static RendererExtensionMetadata getExtension(
			ModMetadata metadata,
			CustomValue.CvObject frappeObj
	) {
		return getExtension(metadata, frappeObj, null);
	}

	public static @Nullable RendererExtensionMetadata getSubExtension(
			CustomValue.CvObject frappeObj,
			String declaringModId
	) {
		if (propertyExists(frappeObj, "override", CustomValue.CvType.BOOLEAN) && frappeObj.get("override").getAsBoolean()) {
			return null;
		}

		return getExtension(null, frappeObj, declaringModId);
	}

	private static RendererExtensionMetadata getExtension(
			@Nullable ModMetadata metadata,
			CustomValue.CvObject frappeObj,
			@Nullable String declaringModId
	) {
		String errorAppendix = getErrorAppendix(metadata, frappeObj, declaringModId);

		checkSchemaVersion(frappeObj, errorAppendix);
		String id = getId(metadata, frappeObj, errorAppendix);
		Version version = getVersion(metadata, frappeObj, errorAppendix);
		SupportTier supportTier = getSupportTier(frappeObj, errorAppendix);
		boolean enabled = isEnabled(frappeObj, supportTier);
		String implType = getImplType(frappeObj, errorAppendix);
		String name = getName(metadata, frappeObj);
		String description = getDescription(metadata, frappeObj);
		Map<String, String> config = getConfig(frappeObj);
		return new RendererExtensionMetadataImpl(id, version, supportTier, enabled, implType, name, description, config);
	}

	private static String getErrorAppendix(
			@Nullable ModMetadata metadata,
			CustomValue.CvObject frappeObj,
			@Nullable String declaringModId
	) {
		String errorAppendix = "";

		if (propertyExists(frappeObj, "id", CustomValue.CvType.STRING)) {
			errorAppendix += " in extension " + frappeObj.get("id").getAsString();
		}

		if (metadata != null) {
			if (declaringModId == null && errorAppendix.isEmpty()) {
				errorAppendix += " in extension " + metadata.getId();
			} else {
				errorAppendix += " in mod " + metadata.getId();
			}
		} else if (declaringModId != null) {
			errorAppendix += " in mod " + declaringModId;
		}

		return errorAppendix;
	}

	private static void checkSchemaVersion(
			CustomValue.CvObject frappeObj,
			String errorAppendix
	) {
		int schemaVersion;

		if (propertyExists(frappeObj, "schemaVersion", CustomValue.CvType.NUMBER)) {
			schemaVersion = frappeObj.get("schemaVersion").getAsNumber().intValue();
		} else {
			schemaVersion = -1;
		}

		if (schemaVersion == -1) {
			throw new IllegalArgumentException("Frappé metadata schemaVersion not specified" + errorAppendix);
		} else if (schemaVersion != 0) {
			throw new IllegalArgumentException("Unsupported Frappé metadata schemaVersion " + schemaVersion + errorAppendix);
		}
	}

	private static String getId(@Nullable ModMetadata metadata, CustomValue.CvObject frappeObj, String errorAppendix) {
		if (propertyExists(frappeObj, "id", CustomValue.CvType.STRING)) {
			return frappeObj.get("id").getAsString();
		} else if (metadata != null) {
			return metadata.getId();
		} else {
			throw new IllegalArgumentException("Frappé metadata id not specified" + errorAppendix);
		}
	}

	private static Version getVersion(@Nullable ModMetadata metadata, CustomValue.CvObject frappeObj, String errorAppendix) {
		if (propertyExists(frappeObj, "version", CustomValue.CvType.STRING)) {
			try {
				return Version.parse(frappeObj.get("version").getAsString());
			} catch (VersionParsingException e) {
				throw new IllegalArgumentException("Frappé metadata version was invalid" + errorAppendix, e);
			}
		} else if (metadata != null) {
			return metadata.getVersion();
		} else {
			throw new IllegalArgumentException("Frappé metadata version not speicified" + errorAppendix);
		}
	}

	private static SupportTier getSupportTier(CustomValue.CvObject frappeObj, String errorAppendix) {
		if (propertyExists(frappeObj, "supportTier", CustomValue.CvType.STRING)) {
			try {
				return SupportTier.valueOf(frappeObj.get("supportTier").getAsString().toUpperCase(Locale.ROOT));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Frappé metadata supportTier was invalid" + errorAppendix, e);
			}
		} else {
			throw new IllegalArgumentException("Frappé metadata supportTier not specified" + errorAppendix);
		}
	}

	private static boolean isEnabled(CustomValue.CvObject frappeObj, SupportTier supportTier) {
		if (propertyExists(frappeObj, "enabled", CustomValue.CvType.BOOLEAN)) {
			return frappeObj.get("enabled").getAsBoolean();
		} else {
			return !supportTier.isUnstable();
		}
	}

	private static String getImplType(CustomValue.CvObject frappeObj, String errorAppendix) {
		if (propertyExists(frappeObj, "implType", CustomValue.CvType.STRING)) {
			return frappeObj.get("implType").getAsString();
		} else {
			throw new IllegalArgumentException("Frappé metadata implType not specified" + errorAppendix);
		}
	}

	private static @Nullable String getName(@Nullable ModMetadata metadata, CustomValue.CvObject frappeObj) {
		if (propertyExists(frappeObj, "name", CustomValue.CvType.STRING)) {
			return frappeObj.get("name").getAsString();
		} else if (metadata != null) {
			return metadata.getName();
		} else {
			return null;
		}
	}

	private static @Nullable String getDescription(@Nullable ModMetadata metadata, CustomValue.CvObject frappeObj) {
		if (propertyExists(frappeObj, "description", CustomValue.CvType.STRING)) {
			return frappeObj.get("description").getAsString();
		} else if (metadata != null) {
			return metadata.getDescription();
		} else {
			return null;
		}
	}

	private static @Unmodifiable Map<String, String> getConfig(CustomValue.CvObject frappeObj) {
		if (propertyExists(frappeObj, "config", CustomValue.CvType.OBJECT)) {
			return traverseProperties(frappeObj.get("config").getAsObject(), null, new HashMap<>());
		} else {
			return Map.of();
		}
	}

	public static boolean propertyExists(
			CustomValue.CvObject frappeObj,
			String key,
			CustomValue.CvType type
	) {
		return frappeObj.containsKey(key) && frappeObj.get(key).getType().equals(type);
	}
}
