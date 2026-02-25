package gay.sylv.frappe.mocha.impl;

import net.minecraft.resources.Identifier;

public final class Mocha {
	private Mocha() {
	}

	public static Identifier modId(String path) {
		return Identifier.fromNamespaceAndPath("mocha", path);
	}
}
