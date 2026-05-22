/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.base.extension;

/// The extent of support for a particular renderer extension and its implementations.
///
/// The terms MUST, MUST NOT, SHOULD, SHOULD NOT, and MAY are used as defined in [RFC 2119](https://www.rfc-editor.org/rfc/rfc2119.html).
@MCPFriendly
public enum SupportTier {
	/// An extension so essential that its implementation is required to be considered Frappé compliant.
	/// Core extensions MUST NOT be added or removed outside minor Minecraft versions.
	///
	/// If a core extension is unimplemented in production, Frappé will crash.
	///
	/// All relevant conditions of [#STANDARD] apply unless otherwise specified.
	CORE,
	/// An extension based on a first party or de facto standard with widespread adoption.
	/// Standard extensions MUST have a default implementation in Mocha.
	///
	/// All relevant conditions of [#NON_STANDARD] apply unless otherwise specified.
	STANDARD,
	/// A third party or otherwise non-standard extension with a default Sodium/Indigo implementation in
	/// the mod it's defined in. An extension of this tier MAY have limited adoption, especially when
	/// domain-specific.
	///
	/// In addition to SemVer restrictions, non-standard extensions MUST NOT break API or ABI outside
	/// minor Minecraft versions.
	///
	/// All relevant conditions of [#EXPERIMENTAL] apply unless otherwise specified.
	NON_STANDARD,
	/// A third party or otherwise experimental extension that may be unstable in nature or
	/// have a rapidly evolving API. An extension of this tier MAY make breaking changes
	/// as seen fit. However, all extensions SHOULD reduce breaking changes.
	///
	/// An experimental extension MUST NOT have a default implementation in Mocha.
	/// Thus, default implementations of experimental extensions in Mocha are to
	/// be disabled by default.
	EXPERIMENTAL;

	/// @return `true` if the implementation is unstable or the API is subject to change.
	public boolean isUnstable() {
		return this.equals(EXPERIMENTAL);
	}
}
