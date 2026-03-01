package gay.sylv.frappe.mocha.test;

import java.nio.ByteBuffer;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import org.joml.Matrix4fc;

import net.minecraft.client.renderer.DynamicUniformStorage;

public class TestDynamicUniforms implements AutoCloseable {
	private static final int TRANSFORMS_SIZE = new Std140SizeCalculator()
			.putMat4f()
			.get();
	private final DynamicUniformStorage<MochaTestDynamicTransforms> transforms = new DynamicUniformStorage<>(
			"Mocha Test Dynamic Transforms UBO",
			TRANSFORMS_SIZE,
			2
	);

	public void reset() {
		this.transforms.endFrame();
	}

	@Override
	public void close() {
		this.transforms.close();
	}

	public GpuBufferSlice writeTransform(Matrix4fc textureMatrix) {
		return this.transforms.writeUniform(new MochaTestDynamicTransforms(textureMatrix));
	}

	public record MochaTestDynamicTransforms(Matrix4fc textureMatrix) implements DynamicUniformStorage.DynamicUniform {
		@Override
		public void write(ByteBuffer byteBuffer) {
			Std140Builder.intoBuffer(byteBuffer)
					.putMat4f(textureMatrix);
		}
	}
}
