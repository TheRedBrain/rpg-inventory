package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ToggleTwoHandedStancePacket() implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ToggleTwoHandedStancePacket> PACKET_ID = new CustomPacketPayload.Type<>(RPGInventory.identifier("toggle_two_handed_stance"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ToggleTwoHandedStancePacket> PACKET_CODEC = StreamCodec.ofMember(ToggleTwoHandedStancePacket::write, ToggleTwoHandedStancePacket::new);

	public ToggleTwoHandedStancePacket(RegistryFriendlyByteBuf registryByteBuf) {
		this();
	}

	private void write(RegistryFriendlyByteBuf registryByteBuf) {
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return PACKET_ID;
	}
}
