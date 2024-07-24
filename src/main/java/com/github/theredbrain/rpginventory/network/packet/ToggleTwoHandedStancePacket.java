package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record ToggleTwoHandedStancePacket() implements CustomPayload {
	public static final CustomPayload.Id<ToggleTwoHandedStancePacket> PACKET_ID = new CustomPayload.Id<>(RPGInventory.identifier("toggle_two_handed_stance"));
	public static final PacketCodec<RegistryByteBuf, ToggleTwoHandedStancePacket> PACKET_CODEC = PacketCodec.of(ToggleTwoHandedStancePacket::write, ToggleTwoHandedStancePacket::new);

	public ToggleTwoHandedStancePacket(RegistryByteBuf registryByteBuf) {
		this();
	}

	private void write(RegistryByteBuf registryByteBuf) {
	}

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
