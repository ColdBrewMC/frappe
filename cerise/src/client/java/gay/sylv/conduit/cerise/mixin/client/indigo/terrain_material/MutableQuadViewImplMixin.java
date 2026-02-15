package gay.sylv.conduit.cerise.mixin.client.indigo.terrain_material;

import static gay.sylv.conduit.cerise.impl.indigo.CeriseIndigoEncodingFormat.HEADER_CERISE_BITS;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.conduit.api.ext.terrain_material.QE_ExtTerrainMaterial;
import gay.sylv.conduit.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.conduit.cerise.impl.indigo.CeriseIndigoEncodingFormat;

@SuppressWarnings("UnstableApiUsage")
@Mixin(MutableQuadViewImpl.class)
public abstract class MutableQuadViewImplMixin<Q extends QE_ExtTerrainMaterial<Q>> extends QuadViewImpl implements QE_ExtTerrainMaterial<Q> {
	@Override
	public QE_ExtTerrainMaterial<Q> conduit$terrainMaterial(TerrainMaterial material) {
		this.data[this.baseIndex + HEADER_CERISE_BITS] =
				CeriseIndigoEncodingFormat.terrainMaterial(
						this.data[this.baseIndex + HEADER_CERISE_BITS],
						material
				);
		return this;
	}
}
