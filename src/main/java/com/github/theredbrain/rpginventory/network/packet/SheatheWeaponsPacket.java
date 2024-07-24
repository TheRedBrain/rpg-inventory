package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SheatheWeaponsPacket() implements CustomPayload {
	public static final CustomPayload.Id<SheatheWeaponsPacket> PACKET_ID = new CustomPayload.Id<>(RPGInventory.identifier("sheathe_weapons"));
	public static final PacketCodec<RegistryByteBuf, SheatheWeaponsPacket> PACKET_CODEC = PacketCodec.of(SheatheWeaponsPacket::write, SheatheWeaponsPacket::new);

	public SheatheWeaponsPacket(RegistryByteBuf registryByteBuf) {
		this();
	}

	private void write(RegistryByteBuf registryByteBuf) {
	}

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
