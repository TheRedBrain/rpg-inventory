package com.github.theredbrain.rpginventory.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record AdvancementLockedComponent(
		String unlock_advancement,
		String lock_advancement,
		String not_unlocked_tooltip_text,
		String tooltip_text,
		String locked_tooltip_text,
		int status
) {
	public static final Codec<AdvancementLockedComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.STRING.optionalFieldOf("unlock_advancement", "").forGetter(AdvancementLockedComponent::unlock_advancement),
							Codec.STRING.optionalFieldOf("lock_advancement", "").forGetter(AdvancementLockedComponent::lock_advancement),
							Codec.STRING.optionalFieldOf("not_unlocked_tooltip_text", "").forGetter(AdvancementLockedComponent::not_unlocked_tooltip_text),
							Codec.STRING.optionalFieldOf("tooltip_text", "").forGetter(AdvancementLockedComponent::tooltip_text),
							Codec.STRING.optionalFieldOf("locked_tooltip_text", "").forGetter(AdvancementLockedComponent::locked_tooltip_text),
							Codec.INT.optionalFieldOf("status", 0).forGetter(AdvancementLockedComponent::status)
					)
					.apply(instance, AdvancementLockedComponent::new)
	);
	public static final PacketCodec<RegistryByteBuf, AdvancementLockedComponent> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.STRING,
			AdvancementLockedComponent::unlock_advancement,
			PacketCodecs.STRING,
			AdvancementLockedComponent::lock_advancement,
			PacketCodecs.STRING,
			AdvancementLockedComponent::not_unlocked_tooltip_text,
			PacketCodecs.STRING,
			AdvancementLockedComponent::tooltip_text,
			PacketCodecs.STRING,
			AdvancementLockedComponent::locked_tooltip_text,
			PacketCodecs.INTEGER,
			AdvancementLockedComponent::status,
			AdvancementLockedComponent::new
	);
}
