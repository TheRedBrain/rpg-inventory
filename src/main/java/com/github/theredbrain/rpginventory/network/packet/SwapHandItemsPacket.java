package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SwapHandItemsPacket(boolean mainHand, boolean offHand) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SwapHandItemsPacket> PACKET_ID = new CustomPacketPayload.Type<>(RPGInventory.identifier("swap_hand_items"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SwapHandItemsPacket> PACKET_CODEC = StreamCodec.ofMember(SwapHandItemsPacket::write, SwapHandItemsPacket::new);

	public SwapHandItemsPacket(RegistryFriendlyByteBuf registryByteBuf) {
		this(
				registryByteBuf.readBoolean(),
				registryByteBuf.readBoolean()
		);
	}

	private void write(RegistryFriendlyByteBuf registryByteBuf) {
		registryByteBuf.writeBoolean(mainHand);
		registryByteBuf.writeBoolean(offHand);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return PACKET_ID;
	}
}
