package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class ToggleTwoHandedStancePacket implements FabricPacket {
	public static final PacketType<ToggleTwoHandedStancePacket> TYPE = PacketType.create(
			RPGInventory.identifier("toggle_two_handed_stance"),
			ToggleTwoHandedStancePacket::new
	);

	public ToggleTwoHandedStancePacket() {
	}

	public ToggleTwoHandedStancePacket(PacketByteBuf buf) {
		this();
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	@Override
	public void write(PacketByteBuf buf) {
	}

}
