/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.impl.ext.fabric_renderer;

import gay.sylv.frappe.api.base.extension.RendererExtension;
import gay.sylv.frappe.api.base.extension.RendererExtensionManager;
import gay.sylv.frappe.impl.base.extension.ExtensionRegistryImpl;

public final class RendererExtensionManagerImpl implements RendererExtensionManager {
	public static final RendererExtensionManagerImpl INSTANCE = new RendererExtensionManagerImpl();

	@Override
	public boolean frappe$isExtensionLoaded(String id) {
		return ExtensionRegistryImpl.isLoaded(id);
	}

	@Override
	public <T extends RendererExtension> boolean frappe$isExtensionLoaded(Class<T> clazz) {
		return frappe$isExtensionLoaded(ExtensionRegistryImpl.getId(clazz));
	}

	@Override
	public RendererExtension frappe$getExtension(String id) {
		return ExtensionRegistryImpl.getExtensionOrThrow(id);
	}

	@Override
	public <T extends RendererExtension> T frappe$getExtension(Class<T> clazz) {
		//noinspection unchecked // Type T will always be the type T of Class<T> of parameter clazz.
		return (T) frappe$getExtension(ExtensionRegistryImpl.getImplId(clazz));
	}
}
