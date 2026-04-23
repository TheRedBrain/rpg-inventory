package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SheathedWeaponsPacket(int id, boolean mainHand, boolean isSheathed) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SheathedWeaponsPacket> PACKET_ID = new CustomPacketPayload.Type<>(RPGInventory.identifier("sheathed_weapons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SheathedWeaponsPacket> PACKET_CODEC = StreamCodec.ofMember(SheathedWeaponsPacket::write, SheathedWeaponsPacket::new);

	public SheathedWeaponsPacket(RegistryFriendlyByteBuf registryByteBuf) {
		this(registryByteBuf.readInt(), registryByteBuf.readBoolean(), registryByteBuf.readBoolean());
	}

	private void write(RegistryFriendlyByteBuf registryByteBuf) {
		registryByteBuf.writeInt(id);
		registryByteBuf.writeBoolean(mainHand);
		registryByteBuf.writeBoolean(isSheathed);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return PACKET_ID;
	}
}
