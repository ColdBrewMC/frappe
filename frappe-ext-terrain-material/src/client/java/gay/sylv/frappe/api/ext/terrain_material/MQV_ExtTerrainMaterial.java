package gay.sylv.frappe.api.ext.terrain_material;

import gay.sylv.frappe.api.ext.quad_view.FrappeMutableQuadView;

public interface MQV_ExtTerrainMaterial<Q extends MQV_ExtTerrainMaterial<Q>> extends FrappeMutableQuadView<Q>, QV_ExtTerrainMaterial<Q> {
	MQV_ExtTerrainMaterial<Q> frappe$terrainMaterial(TerrainMaterial material);
}
