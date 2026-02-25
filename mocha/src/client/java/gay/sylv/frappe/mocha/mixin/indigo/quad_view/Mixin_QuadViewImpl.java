package gay.sylv.frappe.mocha.mixin.indigo.quad_view;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;

import gay.sylv.frappe.api.ext.quad_view.FrappeQuadView;

@SuppressWarnings("UnstableApiUsage")
@Mixin(QuadViewImpl.class)
public abstract class Mixin_QuadViewImpl implements FrappeQuadView<MutableQuadViewImpl> {
}
