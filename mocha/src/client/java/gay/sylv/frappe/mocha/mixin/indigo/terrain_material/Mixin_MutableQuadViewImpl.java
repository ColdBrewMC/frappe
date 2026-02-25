package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat.HEADER_MOCHA_BITS;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.frappe.api.ext.terrain_material.QE_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;

@SuppressWarnings("UnstableApiUsage")
@Mixin(MutableQuadViewImpl.class)
public abstract class Mixin_MutableQuadViewImpl<Q extends QE_ExtTerrainMaterial<Q>> extends QuadViewImpl implements QE_ExtTerrainMaterial<Q> {
	@Override
	public QE_ExtTerrainMaterial<Q> frappe$terrainMaterial(TerrainMaterial material) {
		this.data[this.baseIndex + HEADER_MOCHA_BITS] =
				MochaIndigoEncodingFormat.terrainMaterial(this.data[this.baseIndex + HEADER_MOCHA_BITS], material);
		return this;
	}
}
