package com.github.theredbrain.rpginventory.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.List;

public record ExclusiveEquipmentComponent(
		List<String> exclusive_equipment_groups
) {
	public static final Codec<ExclusiveEquipmentComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.STRING.listOf().fieldOf("exclusive_equipment_groups").forGetter(ExclusiveEquipmentComponent::exclusive_equipment_groups)
					)
					.apply(instance, ExclusiveEquipmentComponent::new)
	);
	public static final PacketCodec<RegistryByteBuf, ExclusiveEquipmentComponent> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.STRING.collect(PacketCodecs.toList()),
			ExclusiveEquipmentComponent::exclusive_equipment_groups,
			ExclusiveEquipmentComponent::new
	);
}
