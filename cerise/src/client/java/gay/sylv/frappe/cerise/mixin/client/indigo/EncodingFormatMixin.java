package gay.sylv.frappe.cerise.mixin.client.indigo;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.EncodingFormat;

import gay.sylv.frappe.cerise.impl.indigo.CeriseIndigoEncodingFormat;

@SuppressWarnings("UnstableApiUsage")
@Mixin(EncodingFormat.class)
public abstract class EncodingFormatMixin {
	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(value = "CONSTANT", args = "intValue=4", ordinal = 0)
	)
	private static int setVertexX(int original) {
		return original + CeriseIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(value = "CONSTANT", args = "intValue=5", ordinal = 0)
	)
	private static int setVertexY(int original) {
		return original + CeriseIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(value = "CONSTANT", args = "intValue=6", ordinal = 0)
	)
	private static int setVertexZ(int original) {
		return original + CeriseIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(value = "CONSTANT", args = "intValue=7", ordinal = 0)
	)
	private static int setVertexColor(int original) {
		return original + CeriseIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(value = "CONSTANT", args = "intValue=8", ordinal = 0)
	)
	private static int setVertexU(int original) {
		return original + CeriseIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@Definition(
			id = "VERTEX_U",
			field = "Lnet/fabricmc/fabric/impl/client/indigo/renderer/mesh/EncodingFormat;VERTEX_U:I"
	)
	@Expression("VERTEX_U + 1")
	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private static int setVertexV(int original) {
		return original + CeriseIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(value = "CONSTANT", args = "intValue=10", ordinal = 0)
	)
	private static int setVertexLightmap(int original) {
		return original + CeriseIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(value = "CONSTANT", args = "intValue=11", ordinal = 0)
	)
	private static int setVertexNormal(int original) {
		return original + CeriseIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}

	@Definition(
			id = "QUAD_STRIDE",
			field = "Lnet/fabricmc/fabric/impl/client/indigo/renderer/mesh/EncodingFormat;QUAD_STRIDE:I"
	)
	@Expression("4 + QUAD_STRIDE")
	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private static int setTotalStride(int original) {
		return original + CeriseIndigoEncodingFormat.DELTA_HEADER_STRIDE;
	}
}
