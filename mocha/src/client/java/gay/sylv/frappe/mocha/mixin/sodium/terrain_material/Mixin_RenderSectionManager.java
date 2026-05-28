package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import gay.sylv.frappe.mocha.impl.sodium.vertex.format.ComplexChunkVertex;

// FIXME: either compartmentalize the vertex formats for each format or bring back quad pulling
@Mixin(RenderSectionManager.class)
public abstract class Mixin_RenderSectionManager {
	@Definition(
			id = "COMPACT",
			field = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkMeshFormats;COMPACT:Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexType;"
	)
	@Expression("COMPACT")
	@ModifyExpressionValue(
			method = "<init>", at = @At(
			value = "MIXINEXTRAS:EXPRESSION"
			))
	private static ChunkVertexType useExtendedFormat(ChunkVertexType original) {
		return ComplexChunkVertex.INSTANCE;
	}
}
