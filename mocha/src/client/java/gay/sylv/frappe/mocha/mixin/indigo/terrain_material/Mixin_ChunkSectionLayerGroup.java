package gay.sylv.frappe.mocha.mixin.indigo.terrain_material;

import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_CUTOUT;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_OPAQUE_CUTOUT;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_OPAQUE_SOLID;
import static gay.sylv.frappe.mocha.impl.indigo.terrain_material.IndigoTerrainMaterialExtension.MOCHA_SOLID;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;

@Mixin(ChunkSectionLayerGroup.class)
public abstract class Mixin_ChunkSectionLayerGroup {
	@Unique
	private static int offset;

	@Mutable
	@Shadow
	@Final
	private static ChunkSectionLayerGroup[] $VALUES;

	@Shadow
	@Final
	public static ChunkSectionLayerGroup OPAQUE;

	@Shadow
	@Final
	public static ChunkSectionLayerGroup TRANSLUCENT;

	@Definition(id = "ordinal", local = @Local(type = int.class, argsOnly = true, ordinal = 0))
	@Expression("ordinal")
	@ModifyExpressionValue(method = "<init>", at = @At("MIXINEXTRAS:EXPRESSION"))
	private static int overrideOrdinal(int original, @Local(argsOnly = true) String name) {
		if (name.equals("CUTOUT") || name.equals("TRANSLUCENT")) {
			offset++;
		}

		return original + offset;
	}

	@SuppressWarnings("CheckStyle")
	@Invoker(value = "<init>")
	private static ChunkSectionLayerGroup init(String name, int ordinal, ChunkSectionLayer... layers) {
		throw new UnsupportedOperationException("@Invoker in Mixin");
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void onClinit(CallbackInfo ci) {
		offset = 0;
		MOCHA_OPAQUE_SOLID = init("MOCHA_OPAQUE_SOLID", 0, MOCHA_SOLID);
		MOCHA_OPAQUE_CUTOUT = init("MOCHA_OPAQUE_CUTOUT", 2, MOCHA_CUTOUT);

		$VALUES = new ChunkSectionLayerGroup[]{MOCHA_OPAQUE_SOLID, OPAQUE, MOCHA_OPAQUE_CUTOUT, TRANSLUCENT};
	}
}
