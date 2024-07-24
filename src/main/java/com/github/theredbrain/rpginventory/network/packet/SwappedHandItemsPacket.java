package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SwappedHandItemsPacket(int id, boolean mainHand) implements CustomPayload {
	public static final CustomPayload.Id<SwappedHandItemsPacket> PACKET_ID = new CustomPayload.Id<>(RPGInventory.identifier("swapped_hand_items"));
	public static final PacketCodec<RegistryByteBuf, SwappedHandItemsPacket> PACKET_CODEC = PacketCodec.of(SwappedHandItemsPacket::write, SwappedHandItemsPacket::new);

	public SwappedHandItemsPacket(RegistryByteBuf registryByteBuf) {
		this(registryByteBuf.readInt(), registryByteBuf.readBoolean());
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeInt(id);
		registryByteBuf.writeBoolean(mainHand);
	}

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
