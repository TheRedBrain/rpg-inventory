package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record UpdateAdvancementLockedItemsPacket() implements CustomPayload {
	public static final CustomPayload.Id<UpdateAdvancementLockedItemsPacket> PACKET_ID = new CustomPayload.Id<>(RPGInventory.identifier("update_advancement_locked_items"));
	public static final PacketCodec<RegistryByteBuf, UpdateAdvancementLockedItemsPacket> PACKET_CODEC = PacketCodec.of(UpdateAdvancementLockedItemsPacket::write, UpdateAdvancementLockedItemsPacket::new);

	public UpdateAdvancementLockedItemsPacket(RegistryByteBuf registryByteBuf) {
		this();
	}

	private void write(RegistryByteBuf registryByteBuf) {
	}

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}