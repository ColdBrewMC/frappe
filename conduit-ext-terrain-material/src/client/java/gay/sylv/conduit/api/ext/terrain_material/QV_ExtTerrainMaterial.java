package gay.sylv.conduit.api.ext.terrain_material;

import org.jspecify.annotations.Nullable;

import gay.sylv.conduit.api.ext.quad_view.ConduitQuadView;

public interface QV_ExtTerrainMaterial<Q extends QV_ExtTerrainMaterial<Q>> extends ConduitQuadView<Q> {
	@Nullable TerrainMaterial conduit$terrainMaterial();
}
