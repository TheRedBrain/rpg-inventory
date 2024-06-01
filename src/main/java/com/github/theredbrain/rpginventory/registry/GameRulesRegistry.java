package com.github.theredbrain.rpginventory.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GameRulesRegistry {
    public static final GameRules.Key<GameRules.BooleanRule> CAN_CHANGE_EQUIPMENT =
            GameRuleRegistry.register("canChangeEquipment", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> DESTROY_DROPPED_ITEMS_ON_DEATH =
            GameRuleRegistry.register("destroyDroppedItemsOnDeath", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    public static void init() {}
}
