package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SwappedHandItemsPacket(int id, boolean mainHand) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SwappedHandItemsPacket> PACKET_ID = new CustomPacketPayload.Type<>(RPGInventory.identifier("swapped_hand_items"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SwappedHandItemsPacket> PACKET_CODEC = StreamCodec.ofMember(SwappedHandItemsPacket::write, SwappedHandItemsPacket::new);

	public SwappedHandItemsPacket(RegistryFriendlyByteBuf registryByteBuf) {
		this(registryByteBuf.readInt(), registryByteBuf.readBoolean());
	}

	private void write(RegistryFriendlyByteBuf registryByteBuf) {
		registryByteBuf.writeInt(id);
		registryByteBuf.writeBoolean(mainHand);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return PACKET_ID;
	}
}
