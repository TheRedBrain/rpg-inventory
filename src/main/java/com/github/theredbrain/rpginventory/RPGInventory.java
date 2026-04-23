package com.github.theredbrain.rpginventory;

import com.github.theredbrain.rpginventory.compat.BetterCombatExtensionCompat;
import com.github.theredbrain.rpginventory.compat.HealthRegenerationOverhaulCompat;
import com.github.theredbrain.rpginventory.compat.InventorySizeAttributesCompat;
import com.github.theredbrain.rpginventory.compat.ManaAttributesCompat;
import com.github.theredbrain.rpginventory.compat.OverhauledDamageCompat;
import com.github.theredbrain.rpginventory.compat.ScriptBlocksCompat;
import com.github.theredbrain.rpginventory.compat.SpellEngineCompat;
import com.github.theredbrain.rpginventory.compat.SpellEngineExtensionCompat;
import com.github.theredbrain.rpginventory.compat.StaminaAttributesCompat;
import com.github.theredbrain.rpginventory.compat.TrinketsCompat;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import com.github.theredbrain.rpginventory.component.type.ExclusiveEquipmentComponent;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.registry.BlockRegistry;
import com.github.theredbrain.rpginventory.registry.EntityRegistry;
import com.github.theredbrain.rpginventory.registry.ItemComponentRegistry;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpginventory.registry.ServerPacketRegistry;
import com.github.theredbrain.rpginventory.registry.StatusEffectsRegistry;
import com.github.theredbrain.rpginventory.util.SwapHandAttributesHelper;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.scores.PlayerTeam;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class RPGInventory implements ModInitializer {
	public static final String MOD_ID = "rpginventory";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static Holder<Attribute> ACTIVE_SPELL_SLOT_AMOUNT;

	public static Holder<MobEffect> CIVILISATION;
	public static Holder<MobEffect> KEEP_INVENTORY;
	public static Holder<MobEffect> NEEDS_TWO_HANDING;
	public static Holder<MobEffect> NO_ATTACK_ITEM;
	public static Holder<MobEffect> WILDERNESS;
	public static Holder<MobEffect> PVP;

	public static DataComponentType<Unit> BOUNDS_TO_PLAYER;
	public static DataComponentType<ResolvableProfile> PLAYER_BOUND;
	public static DataComponentType<Unit> SAVES_CRAFTING_PLAYER;
	public static DataComponentType<ResolvableProfile> PLAYER_CRAFTED;
	public static DataComponentType<AdvancementLockedComponent> ADVANCEMENT_LOCKED;
	/*
	 * Equipped items with this component can't be unequipped manually and don't drop on death. They are kept or vanish instead.
	 * Interacting with a 'mannequin' equips items with this component. Slots have to be either empty or contain a stack with this component for that to happen.
	 *
	 * Mannequins have a second interaction that removes all equipped items with this component.
	 */
	public static DataComponentType<Unit> LOAD_OUT_ITEM;
	public static DataComponentType<Unit> IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS;
	public static DataComponentType<Unit> IS_KEPT_ON_DEATH;
	public static DataComponentType<Unit> IS_DESTROYED_ON_DEATH;
	public static DataComponentType<Unit> UNUSABLE_WHEN_LOW_DURABILITY;
	public static DataComponentType<ExclusiveEquipmentComponent> EXCLUSIVE_EQUIPMENT;

	public static final boolean isRPGCraftingLoaded = FabricLoader.getInstance().isModLoaded("rpgcrafting");
	public static final boolean isBackpackAttributeLoaded = FabricLoader.getInstance().isModLoaded("backpackattribute");
	public static final boolean isCombatRollLoaded = FabricLoader.getInstance().isModLoaded("combat_roll");
	public static final boolean isHealthRegenerationOverhaulLoaded = FabricLoader.getInstance().isModLoaded("healthregenerationoverhaul");
	public static final boolean isNumismaticOverhaulLoaded = FabricLoader.getInstance().isModLoaded("numismatic-overhaul");
	public static final boolean isOwoLibLoaded = FabricLoader.getInstance().isModLoaded("owo");
	public static final boolean isManaAttributesLoaded = FabricLoader.getInstance().isModLoaded("manaattributes");
	public static final boolean isStaminaAttributesLoaded = FabricLoader.getInstance().isModLoaded("staminaattributes");
	public static final boolean isInventorySizeAttributesLoaded = FabricLoader.getInstance().isModLoaded("inventorysizeattributes");
	public static final boolean isSpellEngineLoaded = FabricLoader.getInstance().isModLoaded("spell_engine");
	public static final boolean isPlayerAttributeScreenLoaded = FabricLoader.getInstance().isModLoaded("playerattributescreen");
	public static final boolean isBetterCombatExtensionLoaded = FabricLoader.getInstance().isModLoaded("bettercombatextension");
	public static final boolean isSpellEngineExtensionLoaded = FabricLoader.getInstance().isModLoaded("spellengineextension");
	public static final boolean isBetterCombatLoaded = FabricLoader.getInstance().isModLoaded("bettercombat");
	public static final boolean isScriptBlocksLoaded = FabricLoader.getInstance().isModLoaded("scriptblocks");
	public static final boolean isTrinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
	public static final boolean isOverhauledDamageLoaded = FabricLoader.getInstance().isModLoaded("overhauleddamage");

	public static void swapHandAttributes(Player playerEntity, Runnable runnable) {
		if (SERVER_CONFIG.activate_rpg_inventory_screen.get() && SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get()) {
			SwapHandAttributesHelper.swapHandAttributes(playerEntity, runnable);
		}
	}

	public static List<String> getExclusiveEquipmentGroups(ItemStack stack) {
		List<String> list = new ArrayList<>();
		ExclusiveEquipmentComponent exclusiveEquipmentComponent = stack.get(EXCLUSIVE_EQUIPMENT);
		if (exclusiveEquipmentComponent != null) {
			list.addAll(exclusiveEquipmentComponent.exclusive_equipment_groups());
		}
		// TODO Merged Items Compat
		return list;
	}

	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(ServerPlayer serverPlayerEntity) {
		boolean bl = false;
		if (isSpellEngineLoaded) {
			bl = SpellEngineCompat.doesCurrentPlayerStatusPreventHandSlotAction(serverPlayerEntity);
		}
		if (isSpellEngineExtensionLoaded) {
			bl = bl || SpellEngineExtensionCompat.doesCurrentPlayerStatusPreventHandSlotAction(serverPlayerEntity);
		}
		return bl;
	}

	public static int getActiveInventorySize(Player player) {
		return isInventorySizeAttributesLoaded ? InventorySizeAttributesCompat.getActiveInventorySize(player) : 27;
	}

	public static int getActiveHotbarSize(Player player) {
		return isInventorySizeAttributesLoaded ? InventorySizeAttributesCompat.getActiveHotbarSize(player) : 9;
	}

	public static boolean isHandSlotOverhaulActive() {
		boolean bl = true;
		if (isBetterCombatExtensionLoaded) {
			bl = BetterCombatExtensionCompat.isAlternativeHandSwapAlgorithmActive();
		} else if (isBetterCombatLoaded) {
			bl = false;
		}
		return bl && SERVER_CONFIG.activate_rpg_inventory_screen.get() && SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
	}

	public static float getCurrentStamina(LivingEntity livingEntity) {
		float currentStamina = 0.0F;
		if (isStaminaAttributesLoaded) {
			currentStamina = StaminaAttributesCompat.getCurrentStamina(livingEntity);
		}
		return currentStamina;
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		if (isStaminaAttributesLoaded) {
			StaminaAttributesCompat.addStamina(livingEntity, amount);
		}
	}

	public static boolean isTrinketEquipped(LivingEntity livingEntity, Predicate<ItemStack> itemStackPredicate) {
		boolean bl = false;
		if (RPGInventory.isTrinketsLoaded) {
			bl = TrinketsCompat.isTrinketEquipped(livingEntity, itemStackPredicate);
		}
		return bl;
	}

	public static void breakKeepInventoryTrinkets(LivingEntity livingEntity) {
		if (RPGInventory.isTrinketsLoaded) {
			TrinketsCompat.breakKeepInventoryTrinkets(livingEntity);
		}
	}

	public static void resetPlayerStatus(Player playerEntity, boolean endOfBattle) {
		if (RPGInventory.isManaAttributesLoaded) {
			ManaAttributesCompat.resetMana(playerEntity);
		}
		if (RPGInventory.isStaminaAttributesLoaded) {
			StaminaAttributesCompat.resetStamina(playerEntity);
		}
		if (RPGInventory.isOverhauledDamageLoaded) {
			OverhauledDamageCompat.resetPlayerStatus(playerEntity);
		}
		if (RPGInventory.isHealthRegenerationOverhaulLoaded) {
			HealthRegenerationOverhaulCompat.resetHealth(playerEntity);
		} else {
			playerEntity.setHealth(playerEntity.getMaxHealth());
		}
		if (RPGInventory.isSpellEngineLoaded) {
			SpellEngineCompat.resetSpellCooldowns(playerEntity);
		}
		if (RPGInventory.isScriptBlocksLoaded && endOfBattle) {
			ScriptBlocksCompat.setCurrentPVPControllerBlockPosition(playerEntity, Optional.empty());
		}
	}

	public static void addModdedAttributesToEffects() {
		if (isHealthRegenerationOverhaulLoaded) {
			HealthRegenerationOverhaulCompat.addAttributesToStatusEffects();
		}
		if (isManaAttributesLoaded) {
			ManaAttributesCompat.addAttributesToStatusEffects();
		}
		if (isStaminaAttributesLoaded) {
			StaminaAttributesCompat.addAttributesToStatusEffects();
		}
	}

	public static void configureEffects() {
		if (isSpellEngineLoaded) {
			SpellEngineCompat.configureEffects();
		}
	}

	public static LevelData.RespawnData getPVPRespawnPosition(PlayerTeam team, Player playerEntity, boolean endOfBattle) {
		if (playerEntity instanceof ServerPlayer serverPlayerEntity) {
			if (RPGInventory.isScriptBlocksLoaded) {
				return ScriptBlocksCompat.getPVPRespawnPosition(team, serverPlayerEntity, endOfBattle);
			} else {
				ServerPlayer.RespawnConfig respawnConfig = serverPlayerEntity.getRespawnConfig();
				if (respawnConfig != null) {
					return respawnConfig.respawnData();
				}
			}
		}
		return null;
	}

	@Override
	public void onInitialize() {
		LOGGER.info("We are going on an adventure!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);

		// Packets
		ServerPacketRegistry.init();

		// Registry
		BlockRegistry.init();
		EntityRegistry.init();
		ItemComponentRegistry.init();
		ItemRegistry.init();
		ScreenHandlerTypesRegistry.registerAll();
		StatusEffectsRegistry.init();

		// Compatibility
		if (isSpellEngineLoaded) {
			SpellEngineCompat.init();
		}
		if (isTrinketsLoaded) {
			TrinketsCompat.init();
		}
	}

	public static Identifier identifier(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: " + message);
	}

	public static void warn(String message) {
		LOGGER.warn("[" + MOD_ID + "] [warn]: " + message);
	}

	public static void debug(String message) {
		LOGGER.debug("[" + MOD_ID + "] [debug]: " + message);
	}

	public static void error(String message) {
		LOGGER.error("[" + MOD_ID + "] [error]: " + message);
	}
}
