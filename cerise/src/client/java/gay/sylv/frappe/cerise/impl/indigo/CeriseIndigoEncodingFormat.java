package gay.sylv.frappe.cerise.impl.indigo;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Mth;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.EncodingFormat;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;

@SuppressWarnings("UnstableApiUsage")
public final class CeriseIndigoEncodingFormat {
	public static final int DELTA_HEADER_STRIDE = 1;
	public static final int HEADER_CERISE_BITS = 4;
	public static final int HEADER_STRIDE = EncodingFormat.HEADER_STRIDE + DELTA_HEADER_STRIDE;

	private static final Map<TerrainMaterial, Integer> TERRAIN_MATERIAL_2_INDEX = new HashMap<>();
	private static int terrainMaterialCount = 0;
	private static final TerrainMaterial[] TERRAIN_MATERIALS = new TerrainMaterial[64];

	private static final int TERRAIN_MATERIAL_BIT_LENGTH = Mth.ceillog2(TERRAIN_MATERIALS.length);

	private static final int TERRAIN_MATERIAL_BIT_OFFSET = 0;

	private static final int TERRAIN_MATERIAL_MASK = bitMask(
			TERRAIN_MATERIAL_BIT_LENGTH,
			TERRAIN_MATERIAL_BIT_OFFSET
	);

	private CeriseIndigoEncodingFormat() {
	}

	public static TerrainMaterial terrainMaterial(int bits) {
		return TERRAIN_MATERIALS[(bits & TERRAIN_MATERIAL_MASK) >>> TERRAIN_MATERIAL_BIT_OFFSET];
	}

	public static int terrainMaterial(int bits, TerrainMaterial terrainMaterial) {
		int index = TERRAIN_MATERIAL_2_INDEX.computeIfAbsent(terrainMaterial, material -> {
			int idx = terrainMaterialCount;
			TERRAIN_MATERIAL_2_INDEX.put(material, idx);
			TERRAIN_MATERIALS[idx] = material;
			terrainMaterialCount++;
			return idx;
		});
		return (bits & TERRAIN_MATERIAL_MASK) | (index << TERRAIN_MATERIAL_BIT_OFFSET);
	}

	private static int bitMask(int bitLength, int bitOffset) {
		return ((1 << bitLength) - 1) << bitOffset;
	}
}
