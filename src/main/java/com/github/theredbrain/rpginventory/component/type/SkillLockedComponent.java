package com.github.theredbrain.rpginventory.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record SkillLockedComponent(String category, String skill, String tooltipText) {
	public static final Codec<SkillLockedComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.STRING.fieldOf("category").forGetter(SkillLockedComponent::category),
							Codec.STRING.fieldOf("skill").forGetter(SkillLockedComponent::skill),
							Codec.STRING.fieldOf("tooltipText").forGetter(SkillLockedComponent::tooltipText)
					)
					.apply(instance, SkillLockedComponent::new)
	);
	public static final PacketCodec<RegistryByteBuf, SkillLockedComponent> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.STRING,
			SkillLockedComponent::category,
			PacketCodecs.STRING,
			SkillLockedComponent::skill,
			PacketCodecs.STRING,
			SkillLockedComponent::tooltipText,
			SkillLockedComponent::new
	);
}
