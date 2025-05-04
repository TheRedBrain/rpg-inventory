package com.github.theredbrain.rpginventory;

import com.github.theredbrain.inventorysizeattributes.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.registry.BlockRegistry;
import com.github.theredbrain.rpginventory.registry.EntityRegistry;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.ItemComponentRegistry;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.github.theredbrain.rpginventory.registry.PredicateRegistry;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpginventory.registry.ServerPacketRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPGInventory implements ModInitializer {
	public static final String MOD_ID = "rpginventory";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static RegistryEntry<EntityAttribute> ACTIVE_SPELL_SLOT_AMOUNT;

	public static ComponentType<Unit> BOUNDS_TO_PLAYER;
	public static ComponentType<ProfileComponent> PLAYER_BOUND;
	public static ComponentType<Unit> SAVES_CRAFTING_PLAYER;
	public static ComponentType<ProfileComponent> PLAYER_CRAFTED;
	/*
	 * Equipped items with this component can't be unequipped manually and don't drop on death. They are kept or vanish instead.
	 * Interacting with a 'mannequin' equips items with this component. Slots have to be either empty or contain a stack with this component for that to happen.
	 *
	 * Mannequins have a second interaction that removes all equipped items with this component.
	 */
	public static ComponentType<Unit> LOAD_OUT_ITEM;

	public static final TagKey<StatusEffect> PREVENTS_MANNEQUIN_INTERACTION = TagKey.of(RegistryKeys.STATUS_EFFECT, identifier("prevents_mannequin_interaction"));
	public static final TagKey<StatusEffect> PREVENTS_MANNEQUIN_SLOT_INTERACTION = TagKey.of(RegistryKeys.STATUS_EFFECT, identifier("prevents_mannequin_slot_interaction"));

	public static final boolean isRPGCraftingLoaded = FabricLoader.getInstance().isModLoaded("rpgcrafting");
	public static final boolean isBackpackAttributeLoaded = FabricLoader.getInstance().isModLoaded("backpackattribute");
	public static final boolean isFoodOverhaulLoaded = FabricLoader.getInstance().isModLoaded("foodoverhaul");
	public static final boolean isStaminaAttributesLoaded = FabricLoader.getInstance().isModLoaded("staminaattributes");
	public static final boolean isInventorySizeAttributesLoaded = FabricLoader.getInstance().isModLoaded("inventorysizeattributes");

	public static int getActiveInventorySize(PlayerEntity player) {
		return isInventorySizeAttributesLoaded ? ((DuckPlayerEntityMixin) player).inventorysizeattributes$getActiveInventorySlotAmount() : 27;
	}

	public static int getActiveHotbarSize(PlayerEntity player) {
		return isInventorySizeAttributesLoaded ? ((DuckPlayerEntityMixin) player).inventorysizeattributes$getActiveHotbarSlotAmount() : 9;
	}

	public static final boolean isPlayerAttributeScreenLoaded = FabricLoader.getInstance().isModLoaded("playerattributescreen");

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
		PredicateRegistry.init();
		ScreenHandlerTypesRegistry.registerAll();
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
