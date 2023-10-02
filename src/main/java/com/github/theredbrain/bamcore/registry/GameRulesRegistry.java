package com.github.theredbrain.bamcore.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GameRulesRegistry {
    public static final GameRules.Key<GameRules.BooleanRule> CLEAR_ENDER_CHEST_ON_DEATH =
            GameRuleRegistry.register("clearEnderChestOnDeath", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> DESTROY_DROPPED_ITEMS_ON_DEATH =
            GameRuleRegistry.register("destroyDroppedItemsOnDeath", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> RESET_ADVANCEMENTS_ON_DEATH =
            GameRuleRegistry.register("resetAdvancementsOnDeath", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> RESET_RECIPES_ON_DEATH =
            GameRuleRegistry.register("resetRecipesOnDeath", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> CAN_SET_SPAWN_ON_BEDS =
            GameRuleRegistry.register("canSetSpawnOnBeds", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> CAN_SET_SPAWN_ON_RESPAWN_ANCHOR =
            GameRuleRegistry.register("canSetSpawnOnRespawnAnchor", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
    public static void init() {}
}
