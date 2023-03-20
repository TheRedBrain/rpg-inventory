package com.github.theredbrain.rpgmod.mixin.item;

import com.github.theredbrain.rpgmod.item.CustomArmorItem;
import com.github.theredbrain.rpgmod.item.DuckToolItemMixin;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ToolItem.class)
public abstract class ToolItemMixin extends Item implements DuckToolItemMixin {

    @Nullable
    private String translationKeyBroken;

    public ToolItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1;
    }

    /**
     * Gets or creates the translation key of this item when it is not protecting.
     */
    private String getOrCreateTranslationKeyBroken() {
        if (this.translationKeyBroken == null) {
            this.translationKeyBroken = Util.createTranslationKey("item", new Identifier(Registries.ITEM.getId(this).getNamespace() + ":" + Registries.ITEM.getId(this).getPath() + "_broken"));
        }
        return this.translationKeyBroken;
    }

    /**
     * Gets the translation key of this item using the provided item stack for context.
     */
    @Override
    public String getTranslationKey(ItemStack stack) {
        return ((DuckToolItemMixin)stack.getItem()).isUsable(stack) ? this.getTranslationKey() : this.getOrCreateTranslationKeyBroken();
    }
}
