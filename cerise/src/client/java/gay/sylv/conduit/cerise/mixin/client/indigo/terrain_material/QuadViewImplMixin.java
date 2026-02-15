package gay.sylv.conduit.cerise.mixin.client.indigo.terrain_material;

import static gay.sylv.conduit.cerise.impl.indigo.CeriseIndigoEncodingFormat.HEADER_CERISE_BITS;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.conduit.api.ext.terrain_material.QV_ExtTerrainMaterial;
import gay.sylv.conduit.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.conduit.cerise.impl.indigo.CeriseIndigoEncodingFormat;

@SuppressWarnings("UnstableApiUsage")
@Mixin(QuadViewImpl.class)
public abstract class QuadViewImplMixin<Q extends QV_ExtTerrainMaterial<Q>> implements QV_ExtTerrainMaterial<Q> {
	@Shadow
	protected int[] data;

	@Shadow
	protected int baseIndex;

	@Override
	public @Nullable TerrainMaterial conduit$terrainMaterial() {
		return CeriseIndigoEncodingFormat.terrainMaterial(this.data[this.baseIndex + HEADER_CERISE_BITS]);
	}
}
