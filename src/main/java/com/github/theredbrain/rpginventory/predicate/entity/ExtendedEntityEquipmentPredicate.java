package com.github.theredbrain.rpginventory.predicate.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.predicate.ComponentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.village.raid.Raid;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ExtendedEntityEquipmentPredicate(
		Optional<ItemPredicate> head,
		Optional<ItemPredicate> chest,
		Optional<ItemPredicate> legs,
		Optional<ItemPredicate> feet,
		Optional<ItemPredicate> body,
		Optional<ItemPredicate> mainhand,
		Optional<ItemPredicate> offhand
) {
	public static final Codec<ExtendedEntityEquipmentPredicate> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							ItemPredicate.CODEC.optionalFieldOf("head").forGetter(ExtendedEntityEquipmentPredicate::head),
							ItemPredicate.CODEC.optionalFieldOf("chest").forGetter(ExtendedEntityEquipmentPredicate::chest),
							ItemPredicate.CODEC.optionalFieldOf("legs").forGetter(ExtendedEntityEquipmentPredicate::legs),
							ItemPredicate.CODEC.optionalFieldOf("feet").forGetter(ExtendedEntityEquipmentPredicate::feet),
							ItemPredicate.CODEC.optionalFieldOf("body").forGetter(ExtendedEntityEquipmentPredicate::body),
							ItemPredicate.CODEC.optionalFieldOf("mainhand").forGetter(ExtendedEntityEquipmentPredicate::mainhand),
							ItemPredicate.CODEC.optionalFieldOf("offhand").forGetter(ExtendedEntityEquipmentPredicate::offhand)
					)
					.apply(instance, ExtendedEntityEquipmentPredicate::new)
	);

	public boolean test(@Nullable Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			if (this.head.isPresent() && !((ItemPredicate)this.head.get()).test(livingEntity.getEquippedStack(EquipmentSlot.HEAD))) {
				return false;
			} else if (this.chest.isPresent() && !((ItemPredicate)this.chest.get()).test(livingEntity.getEquippedStack(EquipmentSlot.CHEST))) {
				return false;
			} else if (this.legs.isPresent() && !((ItemPredicate)this.legs.get()).test(livingEntity.getEquippedStack(EquipmentSlot.LEGS))) {
				return false;
			} else if (this.feet.isPresent() && !((ItemPredicate)this.feet.get()).test(livingEntity.getEquippedStack(EquipmentSlot.FEET))) {
				return false;
			} else if (this.body.isPresent() && !((ItemPredicate)this.body.get()).test(livingEntity.getEquippedStack(EquipmentSlot.BODY))) {
				return false;
			} else {
				return this.mainhand.isPresent() && !((ItemPredicate)this.mainhand.get()).test(livingEntity.getEquippedStack(EquipmentSlot.MAINHAND))
						? false
						: !this.offhand.isPresent() || ((ItemPredicate)this.offhand.get()).test(livingEntity.getEquippedStack(EquipmentSlot.OFFHAND));
			}
		} else {
			return false;
		}
	}

	public static class Builder {
		private Optional<ItemPredicate> head = Optional.empty();
		private Optional<ItemPredicate> chest = Optional.empty();
		private Optional<ItemPredicate> legs = Optional.empty();
		private Optional<ItemPredicate> feet = Optional.empty();
		private Optional<ItemPredicate> body = Optional.empty();
		private Optional<ItemPredicate> mainhand = Optional.empty();
		private Optional<ItemPredicate> offhand = Optional.empty();

		public static ExtendedEntityEquipmentPredicate.Builder create() {
			return new ExtendedEntityEquipmentPredicate.Builder();
		}

		public ExtendedEntityEquipmentPredicate.Builder head(ItemPredicate.Builder item) {
			this.head = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder chest(ItemPredicate.Builder item) {
			this.chest = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder legs(ItemPredicate.Builder item) {
			this.legs = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder feet(ItemPredicate.Builder item) {
			this.feet = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder body(ItemPredicate.Builder item) {
			this.body = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder mainhand(ItemPredicate.Builder item) {
			this.mainhand = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder offhand(ItemPredicate.Builder item) {
			this.offhand = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate build() {
			return new ExtendedEntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.body, this.mainhand, this.offhand);
		}
	}
}
