package com.github.theredbrain.rpginventory.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;

public class GameRulesRegistry {
	public static final GameRules.Key<GameRules.BooleanRule> CAN_CHANGE_EQUIPMENT =
			GameRuleRegistry.register("canChangeEquipment", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	public static final GameRules.Key<GameRules.BooleanRule> DESTROY_DROPPED_ITEMS_ON_DEATH =
			GameRuleRegistry.register("destroyDroppedItemsOnDeath", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
	public static final GameRules.Key<DoubleRule> NATURAL_SPELL_SLOT_AMOUNT =
			GameRuleRegistry.register("naturalSpellSlotAmount", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1.0, 0.0, 1024.0));

	public static void init() {
	}
}
