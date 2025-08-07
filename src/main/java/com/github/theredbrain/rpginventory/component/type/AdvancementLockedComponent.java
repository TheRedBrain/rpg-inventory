package com.github.theredbrain.rpginventory.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record AdvancementLockedComponent(String unlock_advancement, String lock_advancement, String tooltip_text) {
	public static final Codec<AdvancementLockedComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.STRING.fieldOf("unlock_advancement").forGetter(AdvancementLockedComponent::unlock_advancement),
							Codec.STRING.fieldOf("lock_advancement").forGetter(AdvancementLockedComponent::lock_advancement),
							Codec.STRING.fieldOf("tooltip_text").forGetter(AdvancementLockedComponent::tooltip_text)
					)
					.apply(instance, AdvancementLockedComponent::new)
	);
	public static final PacketCodec<RegistryByteBuf, AdvancementLockedComponent> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.STRING,
			AdvancementLockedComponent::unlock_advancement,
			PacketCodecs.STRING,
			AdvancementLockedComponent::lock_advancement,
			PacketCodecs.STRING,
			AdvancementLockedComponent::tooltip_text,
			AdvancementLockedComponent::new
	);
}
