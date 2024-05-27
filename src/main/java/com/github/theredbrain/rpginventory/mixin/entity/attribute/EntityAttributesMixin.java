package com.github.theredbrain.rpginventory.mixin.entity.attribute;

import com.github.theredbrain.rpginventory.RPGInventory;
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
        RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT = register(RPGInventory.MOD_ID + ":generic.active_spell_slot_amount", new ClampedEntityAttribute("attribute.name.generic.active_spell_slot_amount", 0.0, 0.0, 8.0).setTracked(true));
    }
}
