package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.mixin.item.TemporaryItemAccessor;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

// TODO remove when data-driven items are in vanilla
public class TemporaryItemModifications implements ITemporaryItemModifications {
    @Override
    public void betteradventuremode$setFoodComponent(Item item, FoodComponent foodComponent) {
        ((TemporaryItemAccessor) item).betteradventuremode$setFoodComponent(foodComponent);
    }

    public static void betteradventuremode$applyFoodComponentModifications() {

        // modified vanilla items
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.APPLE, AdventureFoodComponents.APPLE);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.BAKED_POTATO, AdventureFoodComponents.BAKED_POTATO);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.BEEF, AdventureFoodComponents.BEEF);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.BEETROOT, AdventureFoodComponents.BEETROOT);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.BEETROOT_SOUP, AdventureFoodComponents.BEETROOT_SOUP);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.BREAD, AdventureFoodComponents.BREAD);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.CARROT, AdventureFoodComponents.CARROT);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.CHICKEN, AdventureFoodComponents.CHICKEN);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.CHORUS_FRUIT, AdventureFoodComponents.CHORUS_FRUIT);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COD, AdventureFoodComponents.COD);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COOKED_BEEF, AdventureFoodComponents.COOKED_BEEF);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COOKED_CHICKEN, AdventureFoodComponents.COOKED_CHICKEN);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COOKED_COD, AdventureFoodComponents.COOKED_COD);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COOKED_MUTTON, AdventureFoodComponents.COOKED_MUTTON);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COOKED_PORKCHOP, AdventureFoodComponents.COOKED_PORKCHOP);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COOKED_RABBIT, AdventureFoodComponents.COOKED_RABBIT);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COOKED_SALMON, AdventureFoodComponents.COOKED_SALMON);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COOKIE, AdventureFoodComponents.COOKIE);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.DRIED_KELP, AdventureFoodComponents.DRIED_KELP);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.ENCHANTED_GOLDEN_APPLE, AdventureFoodComponents.ENCHANTED_GOLDEN_APPLE);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.GOLDEN_APPLE, AdventureFoodComponents.GOLDEN_APPLE);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.GOLDEN_CARROT, AdventureFoodComponents.GOLDEN_CARROT);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.HONEY_BOTTLE, AdventureFoodComponents.HONEY_BOTTLE);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.MELON_SLICE, AdventureFoodComponents.MELON_SLICE);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.MUSHROOM_STEW, AdventureFoodComponents.MUSHROOM_STEW);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.MUTTON, AdventureFoodComponents.MUTTON);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.POISONOUS_POTATO, AdventureFoodComponents.POISONOUS_POTATO);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.PORKCHOP, AdventureFoodComponents.PORKCHOP);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.POTATO, AdventureFoodComponents.POTATO);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.PUFFERFISH, AdventureFoodComponents.PUFFERFISH);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.PUMPKIN_PIE, AdventureFoodComponents.PUMPKIN_PIE);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.RABBIT, AdventureFoodComponents.RABBIT);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.RABBIT_STEW, AdventureFoodComponents.RABBIT_STEW);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.ROTTEN_FLESH, AdventureFoodComponents.ROTTEN_FLESH);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.SALMON, AdventureFoodComponents.SALMON);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.SPIDER_EYE, AdventureFoodComponents.SPIDER_EYE);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.SUSPICIOUS_STEW, AdventureFoodComponents.SUSPICIOUS_STEW);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.SWEET_BERRIES, AdventureFoodComponents.SWEET_BERRIES);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.GLOW_BERRIES, AdventureFoodComponents.GLOW_BERRIES);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.TROPICAL_FISH, AdventureFoodComponents.TROPICAL_FISH);

        // additional vanilla items
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.BROWN_MUSHROOM, AdventureFoodComponents.BROWN_MUSHROOM);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.COCOA_BEANS, AdventureFoodComponents.COCOA_BEANS);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.FERMENTED_SPIDER_EYE, AdventureFoodComponents.FERMENTED_SPIDER_EYE);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.RED_MUSHROOM, AdventureFoodComponents.RED_MUSHROOM);
        ITemporaryItemModifications.INSTANCE.betteradventuremode$setFoodComponent(Items.SUGAR, AdventureFoodComponents.SUGAR);
    }
}
