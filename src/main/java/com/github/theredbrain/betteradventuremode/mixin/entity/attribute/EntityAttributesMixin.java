package com.github.theredbrain.betteradventuremode.mixin.entity.attribute;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    @Shadow
    private static EntityAttribute register(String id, EntityAttribute attribute) {
        throw new AssertionError();
    }

    static {
        BetterAdventureMode.ACTIVE_SPELL_SLOT_AMOUNT = register(BetterAdventureMode.MOD_ID + ":generic.active_spell_slot_amount", new ClampedEntityAttribute("attribute.name.generic.active_spell_slot_amount", 0.0, 0.0, 8.0).setTracked(true));
        BetterAdventureMode.EQUIPMENT_WEIGHT = register(BetterAdventureMode.MOD_ID + ":generic.equipment_weight", new ClampedEntityAttribute("attribute.name.generic.equipment_weight", 0.0, 0.0, 1024.0).setTracked(true));
        BetterAdventureMode.MAX_EQUIPMENT_WEIGHT = register(BetterAdventureMode.MOD_ID + ":generic.max_equipment_weight", new ClampedEntityAttribute("attribute.name.generic.max_equipment_weight", 10.0, 1.0, 1024.0).setTracked(true));
    }
}
