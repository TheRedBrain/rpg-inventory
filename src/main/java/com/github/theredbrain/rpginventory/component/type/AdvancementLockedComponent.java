package com.github.theredbrain.rpginventory.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

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
	public static final StreamCodec<RegistryFriendlyByteBuf, AdvancementLockedComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			AdvancementLockedComponent::unlock_advancement,
			ByteBufCodecs.STRING_UTF8,
			AdvancementLockedComponent::lock_advancement,
			ByteBufCodecs.STRING_UTF8,
			AdvancementLockedComponent::not_unlocked_tooltip_text,
			ByteBufCodecs.STRING_UTF8,
			AdvancementLockedComponent::tooltip_text,
			ByteBufCodecs.STRING_UTF8,
			AdvancementLockedComponent::locked_tooltip_text,
			ByteBufCodecs.INT,
			AdvancementLockedComponent::status,
			AdvancementLockedComponent::new
	);
}
