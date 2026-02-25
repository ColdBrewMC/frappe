package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractRenderContext;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadView;
import gay.sylv.frappe.api.ext.terrain_material.QV_ExtTerrainMaterial;
import gay.sylv.frappe.api.ext.terrain_material.TerrainMaterial;
import gay.sylv.frappe.mocha.impl.indigo.MochaIndigoEncodingFormat;

// We use UV1 to encode the terrain material in each vertex.
@SuppressWarnings("UnstableApiUsage")
@Mixin(AbstractRenderContext.class)
public abstract class Mixin_AbstractRenderContext {
	@Inject(
			method = "bufferQuad(Lnet/fabricmc/fabric/impl/client/indigo/renderer/mesh/MutableQuadViewImpl;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;addVertex(FFFIFFIIFFF)V",
					shift = At.Shift.AFTER
			)
	)
	private void encodeTerrainMaterial(MutableQuadViewImpl quad, VertexConsumer vertexConsumer, CallbackInfo ci) {
		TerrainMaterial material = FrappeQuadView.of(quad).as(QV_ExtTerrainMaterial.class).frappe$terrainMaterial();
		vertexConsumer.setUv1(MochaIndigoEncodingFormat.TERRAIN_MATERIAL_2_INDEX.get(material), 42);
	}
}
