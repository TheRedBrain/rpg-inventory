package com.github.theredbrain.betteradventuremode.mixin.item;

import com.github.theredbrain.betteradventuremode.api.util.BetterAdventureModCoreItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolItem.class)
public abstract class ToolItemMixin extends Item {

    @Nullable
    @Unique
    private String translationKeyBroken;

    public ToolItemMixin(Settings settings) {
        super(settings);
    }

    /**
     * Gets or creates the translation key of this item when it is not protecting.
     */
    @Unique
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
        return BetterAdventureModCoreItemUtils.isUsable(stack) ? this.getTranslationKey() : this.getOrCreateTranslationKeyBroken();
    }
}
