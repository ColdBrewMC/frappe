package gay.sylv.frappe.mocha.impl.sodium.vertex.format;

import net.caffeinemc.mods.sodium.client.gl.attribute.GlVertexAttributeFormat;
import net.caffeinemc.mods.sodium.client.render.vertex.VertexFormatAttribute;

public final class MochaChunkMeshAttributes {
	public static final VertexFormatAttribute FRAPPE_UV = new VertexFormatAttribute("FRAPPE_UV", GlVertexAttributeFormat.FLOAT, 2, false, false);

	private MochaChunkMeshAttributes() {
	}
}
