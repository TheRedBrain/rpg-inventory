package com.github.theredbrain.rpginventory;

import com.github.theredbrain.rpginventory.compat.BetterCombatExtensionCompat;
import com.github.theredbrain.rpginventory.compat.InventorySizeAttributesCompat;
import com.github.theredbrain.rpginventory.compat.SpellEngineCompat;
import com.github.theredbrain.rpginventory.compat.StaminaAttributesCompat;
import com.github.theredbrain.rpginventory.compat.TrinketsCompat;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.registry.BlockRegistry;
import com.github.theredbrain.rpginventory.registry.EntityRegistry;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.ItemComponentRegistry;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpginventory.registry.ServerPacketRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class RPGInventory implements ModInitializer {
	public static final String MOD_ID = "rpginventory";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static RegistryEntry<EntityAttribute> ACTIVE_SPELL_SLOT_AMOUNT;

	public static ComponentType<Unit> BOUNDS_TO_PLAYER;
	public static ComponentType<ProfileComponent> PLAYER_BOUND;
	public static ComponentType<Unit> SAVES_CRAFTING_PLAYER;
	public static ComponentType<ProfileComponent> PLAYER_CRAFTED;
	public static ComponentType<AdvancementLockedComponent> ADVANCEMENT_LOCKED;
	/*
	 * Equipped items with this component can't be unequipped manually and don't drop on death. They are kept or vanish instead.
	 * Interacting with a 'mannequin' equips items with this component. Slots have to be either empty or contain a stack with this component for that to happen.
	 *
	 * Mannequins have a second interaction that removes all equipped items with this component.
	 */
	public static ComponentType<Unit> LOAD_OUT_ITEM;

	public static final boolean isRPGCraftingLoaded = FabricLoader.getInstance().isModLoaded("rpgcrafting");
	public static final boolean isBackpackAttributeLoaded = FabricLoader.getInstance().isModLoaded("backpackattribute");
	public static final boolean isFoodOverhaulLoaded = FabricLoader.getInstance().isModLoaded("foodoverhaul");
	public static final boolean isStaminaAttributesLoaded = FabricLoader.getInstance().isModLoaded("staminaattributes");
	public static final boolean isInventorySizeAttributesLoaded = FabricLoader.getInstance().isModLoaded("inventorysizeattributes");
	public static final boolean isSpellEngineLoaded = FabricLoader.getInstance().isModLoaded("spell_engine");
	public static final boolean isPlayerAttributeScreenLoaded = FabricLoader.getInstance().isModLoaded("playerattributescreen");
	public static final boolean isBetterCombatExtensionLoaded = FabricLoader.getInstance().isModLoaded("bettercombatextension");
	public static final boolean isBetterCombatLoaded = FabricLoader.getInstance().isModLoaded("bettercombat");
	public static final boolean isTrinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
	public static final boolean isPufferfishsSkillsLoaded = FabricLoader.getInstance().isModLoaded("puffish_skills");

	public static int getActiveInventorySize(PlayerEntity player) {
		return isInventorySizeAttributesLoaded ? InventorySizeAttributesCompat.getActiveInventorySize(player) : 27;
	}

	public static int getActiveHotbarSize(PlayerEntity player) {
		return isInventorySizeAttributesLoaded ? InventorySizeAttributesCompat.getActiveHotbarSize(player) : 9;
	}

	public static boolean isHandSlotOverhaulActive() {
		boolean bl = true;
		if (isBetterCombatExtensionLoaded) {
			bl = BetterCombatExtensionCompat.isAlternativeHandSwapAlgorithmActive();
		} else if (isBetterCombatLoaded) {
			bl = false;
		}
		return bl && SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
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
		GameRulesRegistry.init();
		ScreenHandlerTypesRegistry.registerAll();

		// Compatibility
		if (isSpellEngineLoaded) {
			SpellEngineCompat.init();
		}
		if (isTrinketsLoaded) {
			TrinketsCompat.init();
		}
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
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
