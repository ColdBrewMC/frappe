package gay.sylv.frappe.mocha.mixin.sodium.terrain_material;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.NativeImage;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gay.sylv.frappe.mocha.impl.sodium.MochaSodiumUtils;

@Mixin(NativeImage.class)
public abstract class Mixin_NativeImage {
	@Shadow
	@Final
	private NativeImage.Format format;

	@Shadow
	protected abstract void checkAllocated();

	@Shadow
	@Final
	private int width;

	@Shadow
	private long pixels;

	@WrapMethod(method = "setPixel")
	private void allowSetPixelLuminance(
			int x,
			int y,
			int pixel,
			Operation<Void> original
	) {
		if (this.format == NativeImage.Format.LUMINANCE && MochaSodiumUtils.QUAD_EMITTER_BLOCK_POS.isBound()) {
			this.checkAllocated();
			long offset = (x + (long) y * this.width);
			MemoryUtil.memPutInt(this.pixels + offset, pixel);
		} else {
			original.call(x, y, pixel);
		}
	}
}
