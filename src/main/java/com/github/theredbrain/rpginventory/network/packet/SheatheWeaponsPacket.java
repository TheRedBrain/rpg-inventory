package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SheatheWeaponsPacket() implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SheatheWeaponsPacket> PACKET_ID = new CustomPacketPayload.Type<>(RPGInventory.identifier("sheathe_weapons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SheatheWeaponsPacket> PACKET_CODEC = StreamCodec.ofMember(SheatheWeaponsPacket::write, SheatheWeaponsPacket::new);

	public SheatheWeaponsPacket(RegistryFriendlyByteBuf registryByteBuf) {
		this();
	}

	private void write(RegistryFriendlyByteBuf registryByteBuf) {
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return PACKET_ID;
	}
}
