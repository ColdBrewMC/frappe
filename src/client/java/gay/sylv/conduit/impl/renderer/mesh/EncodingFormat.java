package gay.sylv.conduit.impl.renderer.mesh;

import net.minecraft.util.Mth;

public final class EncodingFormat {
	private EncodingFormat() {
	}

	private static int bitLength(int possibleValues) {
		return Mth.ceillog2(possibleValues);
	}

	private record Field(int possibleValues, int bitLength, int mask) {
	}
}
