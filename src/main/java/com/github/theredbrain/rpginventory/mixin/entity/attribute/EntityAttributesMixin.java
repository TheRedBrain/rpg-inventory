package com.github.theredbrain.rpginventory.mixin.entity.attribute;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Attributes.class)
public class EntityAttributesMixin {
	static {
		RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, RPGInventory.identifier("generic.active_spell_slot_amount"), new RangedAttribute("attribute.name.generic.active_spell_slot_amount", 0.0, 0.0, 8.0).setSyncable(true));
	}
}
