package gay.sylv.conduit.impl.renderer.mesh;

import static java.lang.Integer.bitCount;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

/**
 * The header format for both quads and triangles.
 */
public final class PrimitiveHeaders {
	public static final int HEADER_FIELDS = 0;
	public static final int HEADER_FIELDS_2 = 1;
	public static final int HEADER_VERTEX_EXTENSIONS = 2;
	public static final int HEADER_STRIDE = 3;

	public static final HeaderField<VertexFormat.Mode> PRIMITIVE_MODE = fieldEnum(VertexFormat.Mode.class);
	public static final HeaderField<TextureAtlas> ATLAS = field(
			8,
			PrimitiveObjectsImpl::getAtlas,
			PrimitiveObjectsImpl::getAtlasIndex
	);
	public static final HeaderField<RenderPipeline> PIPELINE = field(
			8,
			PrimitiveObjectsImpl::getPipeline,
			PrimitiveObjectsImpl::getPipelineIndex
	);
	public static final HeaderField<Boolean> EMISSIVE = fieldBoolean();
	public static final HeaderField<Direction> NOMINAL_FACE = field(
			Direction.values(),
			Direction::from3DDataValue,
			Direction::get3DDataValue
	);
	public static final HeaderField<Direction> CULL_FACE = field(
			Direction.values(),
			Direction::from3DDataValue,
			Direction::get3DDataValue
	);
	public static final HeaderField<Integer> NORMAL_FLAGS = fieldInt(4);

	private PrimitiveHeaders() {
	}

	private static <T extends Enum<T>> HeaderField<T> fieldEnum(Class<T> clazz) {
		return field(clazz.getEnumConstants(), data -> clazz.getEnumConstants()[data], Enum::ordinal);
	}

	private static HeaderField<Boolean> fieldBoolean() {
		return field(
				1,
				data -> data >= 1,
				value -> value ? 1 : 0
		);
	}

	private static HeaderField<Integer> fieldInt(int bitLength) {
		return field(
				bitLength,
				data -> data,
				value -> value
		);
	}

	private static <T> HeaderField<T> field(int bitLength, Function<Integer, T> getter, Function<T, Integer> setter) {
		HeaderField<?> lastHeaderField = HeaderField.FIELDS.getLast();
		int lastFieldOffset = lastHeaderField != null ? lastHeaderField.bitOffset + lastHeaderField.bitLength : 0;
		HeaderField<T> headerField = new HeaderField<>(bitLength, lastFieldOffset, ((1 << bitLength) - 1) << lastFieldOffset, getter, setter);
		HeaderField.FIELDS.add(headerField);
		return headerField;
	}

	private static <T> HeaderField<T> field(Object[] variants, Function<Integer, T> getter, Function<T, Integer> setter) {
		return field(Mth.ceillog2(variants.length), getter, setter);
	}

	public record HeaderField<T>(int bitLength, int bitOffset, int mask, Function<Integer, T> getter, Function<T, Integer> setter) {
		private static final List<HeaderField<?>> FIELDS = new ArrayList<>();

		public T get(int[] data) {
			return getter.apply((data[HEADER_FIELDS + this.byteOffset()] & this.mask) >>> this.bitOffset);
		}

		public void set(int[] data, T value) {
			data[HEADER_FIELDS + this.byteOffset()] =
					(data[HEADER_FIELDS + this.byteOffset()] ^ this.mask) | this.setter.apply(value);
		}

		public int byteOffset() {
			return this.bitOffset / Integer.SIZE;
		}
	}
}
