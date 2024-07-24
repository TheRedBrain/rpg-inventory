package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SheathedWeaponsPacket(int id, boolean mainHand, boolean isSheathed) implements CustomPayload {
	public static final CustomPayload.Id<SheathedWeaponsPacket> PACKET_ID = new CustomPayload.Id<>(RPGInventory.identifier("sheathed_weapons"));
	public static final PacketCodec<RegistryByteBuf, SheathedWeaponsPacket> PACKET_CODEC = PacketCodec.of(SheathedWeaponsPacket::write, SheathedWeaponsPacket::new);

	public SheathedWeaponsPacket(RegistryByteBuf registryByteBuf) {
		this(registryByteBuf.readInt(), registryByteBuf.readBoolean(), registryByteBuf.readBoolean());
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeInt(id);
		registryByteBuf.writeBoolean(mainHand);
		registryByteBuf.writeBoolean(isSheathed);
	}

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
