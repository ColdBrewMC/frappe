package gay.sylv.frappe.api.ext.terrain_material;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadEmitter;

public interface QE_ExtTerrainMaterial<Q extends QE_ExtTerrainMaterial<Q>> extends FrappeQuadEmitter<Q>, MQV_ExtTerrainMaterial<Q> {
	@Override
	QE_ExtTerrainMaterial<Q> frappe$terrainMaterial(TerrainMaterial material);
}
