package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import com.mojang.blaze3d.vertex.VertexConsumer;

public interface ExtTerrainMaterial_BufferBuilder {
	default VertexConsumer frappe$setUv(float u, float v) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default VertexConsumer frappe$setMaterialId(byte id) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}
}
