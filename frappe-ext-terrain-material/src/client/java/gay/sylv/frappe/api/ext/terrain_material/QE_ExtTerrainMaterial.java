package gay.sylv.frappe.api.ext.terrain_material;

import gay.sylv.frappe.api.ext.quad_view.ConduitQuadEmitter;

public interface QE_ExtTerrainMaterial<Q extends QE_ExtTerrainMaterial<Q>> extends ConduitQuadEmitter<Q>, MQV_ExtTerrainMaterial<Q> {
	@Override
	QE_ExtTerrainMaterial<Q> conduit$terrainMaterial(TerrainMaterial material);
}
