package gay.sylv.frappe.api.ext.terrain_material;

import gay.sylv.frappe.api.ext.quad_view.ConduitMutableQuadView;

public interface MQV_ExtTerrainMaterial<Q extends MQV_ExtTerrainMaterial<Q>> extends ConduitMutableQuadView<Q>, QV_ExtTerrainMaterial<Q> {
	MQV_ExtTerrainMaterial<Q> conduit$terrainMaterial(TerrainMaterial material);
}
