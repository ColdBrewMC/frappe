package gay.sylv.conduit.api.renderer.mesh;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadTransform;

public interface PrimitiveTransform extends QuadTransform {
	/// @deprecated Use [#transform(MutablePrimitiveView)]
	@Override
	@Deprecated
	default boolean transform(MutableQuadView quad) {
		return this.transform((MutablePrimitiveView) quad);
	}

	boolean transform(MutablePrimitiveView primitive);
}
