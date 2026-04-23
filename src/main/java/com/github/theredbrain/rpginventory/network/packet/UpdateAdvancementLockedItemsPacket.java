package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateAdvancementLockedItemsPacket() implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<UpdateAdvancementLockedItemsPacket> PACKET_ID = new CustomPacketPayload.Type<>(RPGInventory.identifier("update_advancement_locked_items"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateAdvancementLockedItemsPacket> PACKET_CODEC = StreamCodec.ofMember(UpdateAdvancementLockedItemsPacket::write, UpdateAdvancementLockedItemsPacket::new);

	public UpdateAdvancementLockedItemsPacket(RegistryFriendlyByteBuf registryByteBuf) {
		this();
	}

	private void write(RegistryFriendlyByteBuf registryByteBuf) {
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return PACKET_ID;
	}
}