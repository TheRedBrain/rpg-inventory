package com.github.theredbrain.bamcore.screen.slot;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

public interface BetterAdventureModeSlotExtension {

    void bamcore$setTakeItemOverride(boolean canAlwaysTakeItem);

    boolean bamcore$getTakeItemOverride();

    void bamcore$setInsertItemOverride(boolean canAlwaysInsertItem);

    boolean bamcore$getInsertItemOverride();

    TagKey<Item> bamcore$getInsertOnlyItemsOfTag();

    void bamcore$setInsertOnlyItemsOfTag(TagKey<Item> bamcore$insertOnlyItemsOfTag);
}
