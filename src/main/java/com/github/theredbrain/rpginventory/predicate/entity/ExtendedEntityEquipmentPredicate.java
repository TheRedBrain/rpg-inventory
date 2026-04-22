package com.github.theredbrain.rpginventory.predicate.entity;

import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.predicate.item.ItemPredicate;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ExtendedEntityEquipmentPredicate(
		Optional<ItemPredicate> necklace,
		Optional<ItemPredicate> shoulders,
		Optional<ItemPredicate> gloves,
		Optional<ItemPredicate> belt,
		Optional<ItemPredicate> relic,
		Optional<ItemPredicate> class_item,
		Optional<ItemPredicate> spell_1,
		Optional<ItemPredicate> spell_2,
		Optional<ItemPredicate> spell_3,
		Optional<ItemPredicate> spell_4,
		Optional<ItemPredicate> spell_5,
		Optional<ItemPredicate> spell_6,
		Optional<ItemPredicate> spell_7,
		Optional<ItemPredicate> spell_8,
		Optional<ItemPredicate> ring_1,
		Optional<ItemPredicate> ring_2
) {
	public static final Codec<ExtendedEntityEquipmentPredicate> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							ItemPredicate.CODEC.optionalFieldOf("necklace").forGetter(ExtendedEntityEquipmentPredicate::necklace),
							ItemPredicate.CODEC.optionalFieldOf("shoulders").forGetter(ExtendedEntityEquipmentPredicate::shoulders),
							ItemPredicate.CODEC.optionalFieldOf("gloves").forGetter(ExtendedEntityEquipmentPredicate::gloves),
							ItemPredicate.CODEC.optionalFieldOf("belt").forGetter(ExtendedEntityEquipmentPredicate::belt),
							ItemPredicate.CODEC.optionalFieldOf("relic").forGetter(ExtendedEntityEquipmentPredicate::relic),
							ItemPredicate.CODEC.optionalFieldOf("class_item").forGetter(ExtendedEntityEquipmentPredicate::class_item),
							ItemPredicate.CODEC.optionalFieldOf("spell_1").forGetter(ExtendedEntityEquipmentPredicate::spell_1),
							ItemPredicate.CODEC.optionalFieldOf("spell_2").forGetter(ExtendedEntityEquipmentPredicate::spell_2),
							ItemPredicate.CODEC.optionalFieldOf("spell_3").forGetter(ExtendedEntityEquipmentPredicate::spell_3),
							ItemPredicate.CODEC.optionalFieldOf("spell_4").forGetter(ExtendedEntityEquipmentPredicate::spell_4),
							ItemPredicate.CODEC.optionalFieldOf("spell_5").forGetter(ExtendedEntityEquipmentPredicate::spell_5),
							ItemPredicate.CODEC.optionalFieldOf("spell_6").forGetter(ExtendedEntityEquipmentPredicate::spell_6),
							ItemPredicate.CODEC.optionalFieldOf("spell_7").forGetter(ExtendedEntityEquipmentPredicate::spell_7),
							ItemPredicate.CODEC.optionalFieldOf("spell_8").forGetter(ExtendedEntityEquipmentPredicate::spell_8),
							ItemPredicate.CODEC.optionalFieldOf("ring_1").forGetter(ExtendedEntityEquipmentPredicate::ring_1),
							ItemPredicate.CODEC.optionalFieldOf("ring_2").forGetter(ExtendedEntityEquipmentPredicate::ring_2)
					)
					.apply(instance, ExtendedEntityEquipmentPredicate::new)
	);

	public boolean test(@Nullable Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			if (this.necklace.isPresent() && !this.necklace.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.NECKLACE))) {
				return false;
			} else if (this.shoulders.isPresent() && !this.shoulders.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SHOULDERS))) {
				return false;
			} else if (this.gloves.isPresent() && !this.gloves.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.GLOVES))) {
				return false;
			} else if (this.belt.isPresent() && !this.belt.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.BELT))) {
				return false;
			} else if (this.relic.isPresent() && !this.relic.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.RELIC))) {
				return false;
			} else if (this.class_item.isPresent() && !this.class_item.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.CLASS_ITEM))) {
				return false;
			} else if (this.spell_1.isPresent() && !this.spell_1.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_1))) {
				return false;
			} else if (this.spell_2.isPresent() && !this.spell_2.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_2))) {
				return false;
			} else if (this.spell_3.isPresent() && !this.spell_3.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_3))) {
				return false;
			} else if (this.spell_4.isPresent() && !this.spell_4.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_4))) {
				return false;
			} else if (this.spell_5.isPresent() && !this.spell_5.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_5))) {
				return false;
			} else if (this.spell_6.isPresent() && !this.spell_6.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_6))) {
				return false;
			} else if (this.spell_7.isPresent() && !this.spell_7.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_7))) {
				return false;
			} else if (this.spell_8.isPresent() && !this.spell_8.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_8))) {
				return false;
			} else if (this.ring_1.isPresent() && !this.ring_1.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.RING_1))) {
				return false;
			} else if (this.ring_2.isPresent() && !this.ring_2.get().test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.RING_2))) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static class Builder {
		private Optional<ItemPredicate> necklace = Optional.empty();
		private Optional<ItemPredicate> shoulders = Optional.empty();
		private Optional<ItemPredicate> gloves = Optional.empty();
		private Optional<ItemPredicate> belt = Optional.empty();
		private Optional<ItemPredicate> relic = Optional.empty();
		private Optional<ItemPredicate> class_item = Optional.empty();
		private Optional<ItemPredicate> spell_1 = Optional.empty();
		private Optional<ItemPredicate> spell_2 = Optional.empty();
		private Optional<ItemPredicate> spell_3 = Optional.empty();
		private Optional<ItemPredicate> spell_4 = Optional.empty();
		private Optional<ItemPredicate> spell_5 = Optional.empty();
		private Optional<ItemPredicate> spell_6 = Optional.empty();
		private Optional<ItemPredicate> spell_7 = Optional.empty();
		private Optional<ItemPredicate> spell_8 = Optional.empty();
		private Optional<ItemPredicate> ring_1 = Optional.empty();
		private Optional<ItemPredicate> ring_2 = Optional.empty();

		public static ExtendedEntityEquipmentPredicate.Builder create() {
			return new ExtendedEntityEquipmentPredicate.Builder();
		}

		public ExtendedEntityEquipmentPredicate.Builder necklace(ItemPredicate.Builder item) {
			this.necklace = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder shoulders(ItemPredicate.Builder item) {
			this.shoulders = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder gloves(ItemPredicate.Builder item) {
			this.gloves = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder belt(ItemPredicate.Builder item) {
			this.belt = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder relic(ItemPredicate.Builder item) {
			this.relic = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder class_item(ItemPredicate.Builder item) {
			this.class_item = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder spell_1(ItemPredicate.Builder item) {
			this.spell_1 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder spell_2(ItemPredicate.Builder item) {
			this.spell_2 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder spell_3(ItemPredicate.Builder item) {
			this.spell_3 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder spell_4(ItemPredicate.Builder item) {
			this.spell_4 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder spell_5(ItemPredicate.Builder item) {
			this.spell_5 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder spell_6(ItemPredicate.Builder item) {
			this.spell_6 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder spell_7(ItemPredicate.Builder item) {
			this.spell_7 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder spell_8(ItemPredicate.Builder item) {
			this.spell_8 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder ring_1(ItemPredicate.Builder item) {
			this.ring_1 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate.Builder ring_2(ItemPredicate.Builder item) {
			this.ring_2 = Optional.of(item.build());
			return this;
		}

		public ExtendedEntityEquipmentPredicate build() {
			return new ExtendedEntityEquipmentPredicate(this.necklace, this.shoulders, this.gloves, this.belt, this.relic, this.class_item, this.spell_1, this.spell_2, this.spell_3, this.spell_4, this.spell_5, this.spell_6, this.spell_7, this.spell_8, this.ring_1, this.ring_2);
		}
	}
}
