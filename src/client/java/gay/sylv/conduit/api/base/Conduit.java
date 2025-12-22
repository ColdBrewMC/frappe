package gay.sylv.conduit.api.base;

import gay.sylv.conduit.impl.base.ConduitInitializer;

/// Conduit's main class for configuration.
public final class Conduit {
	private Conduit() {
	}

	/// Completely disables Conduit and its renderer.
	///
	/// This is especially useful in cases where using another
	/// renderer like Sodium is desired.
	///
	/// This does nothing after early initialization and should
	/// not be called then.
	public static void disable() {
		ConduitInitializer.disabled = true;
	}

	/// Whether Conduit is enabled.
	///
	/// **Note:** this method will be inaccurate before and during
	/// early initialization.
	public static boolean isDisabled() {
		return ConduitInitializer.disabled;
	}
}
