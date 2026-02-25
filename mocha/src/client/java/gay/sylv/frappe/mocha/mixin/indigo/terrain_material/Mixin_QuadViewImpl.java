package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.HEADER_MOCHA_BITS;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.frappe.api.ext.terrain_material.QV_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;

@SuppressWarnings("UnstableApiUsage")
@Mixin(QuadViewImpl.class)
public abstract class Mixin_QuadViewImpl<Q extends QV_ExtTerrainMaterial<Q>> implements QV_ExtTerrainMaterial<Q> {
	@Shadow
	protected int[] data;

	@Shadow
	protected int baseIndex;

	@Override
	public @Nullable TerrainMaterial frappe$terrainMaterial() {
		return MochaIndigoEncodingFormat.terrainMaterial(this.data[this.baseIndex + HEADER_MOCHA_BITS]);
	}
}
