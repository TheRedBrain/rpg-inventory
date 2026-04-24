package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;

public class DataAttachmentRegistry {
	public static AttachmentType<Boolean> IS_HAND_STACK_SHEATHED;
	public static AttachmentType<Boolean> IS_OFFHAND_STACK_SHEATHED;
	public static AttachmentType<Integer> OLD_ACTIVE_SPELL_SLOT_AMOUNT;
	public static AttachmentType<Boolean> SHOULD_EJECT_EXCLUSIVE_EQUIPMENT;

	public static AttachmentType<Boolean> IS_HAND_SLOT_OVERHAUL_ACTIVE;
	public static AttachmentType<Boolean> ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE;

	public static void init() {
	}

	static {
		IS_HAND_STACK_SHEATHED = AttachmentRegistry.create(RPGInventory.identifier("is_hand_stack_sheathed"), builder -> builder
				.persistent(Codec.BOOL)
				.syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
		);
		IS_OFFHAND_STACK_SHEATHED = AttachmentRegistry.create(RPGInventory.identifier("is_offhand_stack_sheathed"), builder -> builder
				.persistent(Codec.BOOL)
				.syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
		);
		OLD_ACTIVE_SPELL_SLOT_AMOUNT = AttachmentRegistry.create(RPGInventory.identifier("old_active_spell_slot_amount"), builder -> builder
				.persistent(Codec.INT)
				.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
		);
		SHOULD_EJECT_EXCLUSIVE_EQUIPMENT = AttachmentRegistry.create(RPGInventory.identifier("should_eject_exclusive_equipment"), builder -> builder
				.persistent(Codec.BOOL)
				.syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
		);
		IS_HAND_SLOT_OVERHAUL_ACTIVE = AttachmentRegistry.create(RPGInventory.identifier("is_hand_slot_overhaul_active"), builder -> builder
				.persistent(Codec.BOOL)
				.syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
		);
		ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE = AttachmentRegistry.create(RPGInventory.identifier("are_alternative_hand_slots_active"), builder -> builder
				.persistent(Codec.BOOL)
				.syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
		);
	}
}
