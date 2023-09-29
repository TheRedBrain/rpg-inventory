package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public class ItemGroupRegistry {
    public static final RegistryKey<ItemGroup> BAM_BLOCK = RegistryKey.of(RegistryKeys.ITEM_GROUP, BetterAdventureModCore.identifier("bam_block"));
    public static final RegistryKey<ItemGroup> BAM_EQUIPMENT = RegistryKey.of(RegistryKeys.ITEM_GROUP, BetterAdventureModCore.identifier("bam_equipment"));
    public static final RegistryKey<ItemGroup> BAM_FOOD = RegistryKey.of(RegistryKeys.ITEM_GROUP, BetterAdventureModCore.identifier("bam_food"));

//    public static final ItemGroup RPG_ITEM = FabricItemGroup.builder()
//            .icon(() -> new ItemStack(ItemRegistry.ZWEIHANDER))
//            .displayName(Text.translatable("itemGroup.rpgmod.rpg_item"))
//            .build();
//    public static final ItemGroup RPG_FOOD = FabricItemGroup.builder()
//            .icon(() -> new ItemStack(ItemRegistry.ZWEIHANDER))
//            .displayName(Text.translatable("itemGroup.rpgmod.rpg_food"))
//            .build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, BAM_BLOCK, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ItemRegistry.ZWEIHANDER))
                .displayName(Text.translatable("itemGroup.bamcore.bam_block"))
                .build());
        Registry.register(Registries.ITEM_GROUP, BAM_EQUIPMENT, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ItemRegistry.ZWEIHANDER))
                .displayName(Text.translatable("itemGroup.bamcore.bam_equipment"))
                .build());
        Registry.register(Registries.ITEM_GROUP, BAM_FOOD, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ItemRegistry.ZWEIHANDER))
                .displayName(Text.translatable("itemGroup.bamcore.bam_food"))
                .build());
//        Registry.register(Registries.ITEM_GROUP, RPGMod.identifier("rpg_item"), RPG_ITEM);
//        Registry.register(Registries.ITEM_GROUP, RPGMod.identifier("rpg_food"), RPG_FOOD);
//        registerItemsToItemGroups();
    }

//    public static void registerItemsToItemGroups() {
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register(content -> {
//            content.add(BlockRegistry.TELEPORTER_BLOCK);
//            content.add(BlockRegistry.AREA_FILLER_BLOCK);
//            content.add(BlockRegistry.STRUCTURE_PLACER_BLOCK);
//            content.add(BlockRegistry.REDSTONE_TRIGGER_BLOCK);
//            content.add(BlockRegistry.RELAY_TRIGGER_BLOCK);
//            content.add(BlockRegistry.DELAY_TRIGGER_BLOCK);
//            content.add(BlockRegistry.CHUNK_LOADER_BLOCK);
//        });
//        ItemGroupEvents.modifyEntriesEvent(RPG_BLOCK).register(content -> {
//            content.add(BlockRegistry.INTERACTIVE_STONE_BLOCK);
//            content.add(BlockRegistry.INTERACTIVE_OAK_LOG);
//            content.add(BlockRegistry.INTERACTIVE_BERRY_BUSH_BLOCK);
//            content.add(BlockRegistry.INTERACTIVE_BROWN_MUSHROOM_BLOCK);
//            content.add(BlockRegistry.INTERACTIVE_RED_MUSHROOM_BLOCK);
//            content.add(BlockRegistry.INTERACTIVE_CHICKEN_MEAL_BLOCK);
//        });
//        ItemGroupEvents.modifyEntriesEvent(RPG_ITEM).register(content -> {
////            content.add(LEATHER_GLOVES);
////            content.add(LEATHER_SHOULDERS);
//            content.add(ItemRegistry.CHAINMAIL_GLOVES);
//            content.add(ItemRegistry.CHAINMAIL_SHOULDERS);
//            content.add(ItemRegistry.DIAMOND_GLOVES);
//            content.add(ItemRegistry.DIAMOND_SHOULDERS);
//            content.add(ItemRegistry.GOLD_GLOVES);
//            content.add(ItemRegistry.GOLD_SHOULDERS);
//            content.add(ItemRegistry.IRON_GLOVES);
//            content.add(ItemRegistry.IRON_SHOULDERS);
//            content.add(ItemRegistry.NETHERITE_GLOVES);
//            content.add(ItemRegistry.NETHERITE_SHOULDERS);
//            content.add(ItemRegistry.ZWEIHANDER);
////            content.add(IRON_RING);
//        });
//        ItemGroupEvents.modifyEntriesEvent(RPG_FOOD).register(content -> {
//            content.add(ItemRegistry.BERRY_FOOD);
//            content.add(ItemRegistry.BROWN_MUSHROOM_FOOD);
//            content.add(ItemRegistry.RED_MUSHROOM_FOOD);
//        });
//    }
}
