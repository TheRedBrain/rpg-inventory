package com.github.theredbrain.rpginventory.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ExclusiveEquipmentComponent(
		List<String> exclusive_equipment_groups
) {
	public static final Codec<ExclusiveEquipmentComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.STRING.listOf().fieldOf("exclusive_equipment_groups").forGetter(ExclusiveEquipmentComponent::exclusive_equipment_groups)
					)
					.apply(instance, ExclusiveEquipmentComponent::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, ExclusiveEquipmentComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
			ExclusiveEquipmentComponent::exclusive_equipment_groups,
			ExclusiveEquipmentComponent::new
	);
}
