package gay.sylv.frappe.mocha.impl.indigo.terrain_material;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.jspecify.annotations.Nullable;

import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;

public interface Ext_ChunkSectionLayer {
	default @Nullable TerrainMaterial mocha$getAssociatedMaterial() {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default void mocha$setAssociatedMaterial(TerrainMaterial material) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default RenderPipeline mocha$getWireframePipeline() {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}

	default void mocha$setWireframePipeline(RenderPipeline pipeline) {
		throw new UnsupportedOperationException("Implemented via Mixin.");
	}
}
