package gay.sylv.conduit.impl.renderer.mesh;

import java.util.function.Consumer;

import org.jetbrains.annotations.Range;

import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;

public class MutableMeshImpl implements MutableMesh {
	@Override
	public QuadEmitter emitter() {
		return null;
	}

	@Override
	public void forEachMutable(Consumer<? super MutableQuadView> action) {

	}

	@Override
	public Mesh immutableCopy() {
		return null;
	}

	@Override
	public void clear() {

	}

	@Override
	public @Range(from = 0L, to = 2147483647L) int size() {
		return 0;
	}

	@Override
	public void forEach(Consumer<? super QuadView> action) {

	}

	@Override
	public void outputTo(QuadEmitter emitter) {

	}
}
