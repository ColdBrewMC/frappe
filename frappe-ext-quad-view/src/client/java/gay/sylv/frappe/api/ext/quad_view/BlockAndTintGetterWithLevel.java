/*
 * Frappé
 * Copyright (C) 2026 Sylv
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package gay.sylv.frappe.api.ext.quad_view;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;

/**
 * This interface is used to get the {@link ClientLevel} from a {@link BlockAndTintGetter}
 * if available. Note that users may implement the methods in this interface when
 * constructing a fake {@link ClientLevel} object or similar.
 *
 * <p>This interface is transitively and automatically injected on {@link BlockAndTintGetter}.
 */
public interface BlockAndTintGetterWithLevel {
	/// Prefer using the more specific methods when possible.
	///
	/// @return the [ClientLevel] associated with this object
	/// or `null` if none is present.
	default @Nullable ClientLevel frappe$getClientLevel() {
		if (this instanceof ClientLevel clientLevel) {
			return clientLevel;
		}

		return null;
	}

	/// @return the [AttachmentTarget] associated with this object
	/// or `null` if none is present.
	default @Nullable AttachmentTarget frappe$getAttachmentTarget() {
		return this.frappe$getClientLevel();
	}

	/// @return the [AttachmentTarget] associated with this object
	/// at the specified position or `null` if none is present.
	default @Nullable AttachmentTarget frappe$getAttachmentTargetChunkAt(BlockPos blockPos) {
		return this.frappe$getChunkAt(blockPos);
	}

	/// @return the [AttachmentTarget] associated with this object
	/// at the specified position or `null` if none is present.
	default @Nullable AttachmentTarget frappe$getAttachmentTargetChunk(int chunkX, int chunkZ) {
		return this.frappe$getChunk(chunkX, chunkZ);
	}

	/// @return the [ChunkAccess] associated with this object
	/// at the specified position or `null` if none is present.
	default @Nullable ChunkAccess frappe$getChunkAt(BlockPos blockPos) {
		ClientLevel clientLevel = this.frappe$getClientLevel();

		if (clientLevel != null) {
			return clientLevel.getChunkAt(blockPos);
		}

		return null;
	}

	/// @return the [ChunkAccess] associated with this object
	/// at the specified position or `null` if none is present.
	default @Nullable ChunkAccess frappe$getChunk(int chunkX, int chunkZ) {
		ClientLevel clientLevel = this.frappe$getClientLevel();

		if (clientLevel != null) {
			return clientLevel.getChunk(chunkX, chunkZ);
		}

		return null;
	}
}
