package gay.sylv.frappe.mocha.mixin.sodium.render_pipeline;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.caffeinemc.mods.sodium.client.render.model.QuadViewImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import gay.sylv.frappe.api.ext.render_pipeline.value.QuadAttribute;
import gay.sylv.frappe.api.ext.render_pipeline.value.VertexAttribute;
import gay.sylv.frappe.mocha.impl.indium.render_pipeline.Ext_QuadData;

@Mixin(QuadViewImpl.class)
public abstract class Mixin_QuadViewImpl implements Ext_QuadData {
	@Unique
	private final Reference2ObjectArrayMap<VertexAttribute<?>, int[]> vertexAttributes = new Reference2ObjectArrayMap<>();
	@Unique
	private final Reference2ObjectArrayMap<QuadAttribute<?>, int[]> quadAttributes = new Reference2ObjectArrayMap<>();

	@Override
	public Map<VertexAttribute<?>, int[]> frappe$vertexAttributes() {
		return this.vertexAttributes;
	}

	@Override
	public Map<QuadAttribute<?>, int[]> frappe$quadAttributes() {
		return this.quadAttributes;
	}
}
