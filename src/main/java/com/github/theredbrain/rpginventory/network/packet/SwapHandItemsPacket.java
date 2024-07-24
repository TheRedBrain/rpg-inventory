package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SwapHandItemsPacket(boolean mainHand) implements CustomPayload {
	public static final CustomPayload.Id<SwapHandItemsPacket> PACKET_ID = new CustomPayload.Id<>(RPGInventory.identifier("swap_hand_items"));
	public static final PacketCodec<RegistryByteBuf, SwapHandItemsPacket> PACKET_CODEC = PacketCodec.of(SwapHandItemsPacket::write, SwapHandItemsPacket::new);

	public SwapHandItemsPacket(RegistryByteBuf registryByteBuf) {
		this(registryByteBuf.readBoolean());
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeBoolean(mainHand);
	}

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
