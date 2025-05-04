package com.github.theredbrain.rpginventory.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpginventory.screen.slot.CustomArmorSlot;
import com.github.theredbrain.rpginventory.screen.slot.MannequinSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class MannequinScreenHandler extends ScreenHandler {
	private static final Identifier EMPTY_HAND_SLOT = Identifier.ofVanilla("item/empty_slot_sword");
	private static int EQUIPMENT_SLOTS_START;
	private static int MANNEQUIN_SLOTS_START;
	private final Inventory inventory;
	private final PlayerInventory playerInventory;
	private final World world;
	private static Map<EquipmentSlot, Identifier> EMPTY_ARMOR_SLOT_TEXTURES;
	private static EquipmentSlot[] EQUIPMENT_SLOT_ORDER;
	private static List<List<Text>> ARMOR_SLOT_TOOLTIPS;
	private final PlayerEntity owner;

	public MannequinScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(MannequinBlockEntity.INVENTORY_SIZE));
	}

	public MannequinScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(ScreenHandlerTypesRegistry.MANNEQUIN_SCREEN_HANDLER, syncId);
		checkSize(inventory, MannequinBlockEntity.INVENTORY_SIZE);
		this.inventory = inventory;
		this.playerInventory = playerInventory;
		this.owner = playerInventory.player;
		this.world = playerInventory.player.getWorld();
		inventory.onOpen(playerInventory.player);

		// 0 - 26
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 147 + i * 18));
			}
		}

		// 27 - 35
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 205));
		}

		// 36 - 39
		EQUIPMENT_SLOTS_START = 36;
		for (int i = 0; i < 4; i++) {
			EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
			this.addSlot(new CustomArmorSlot(playerInventory, owner, equipmentSlot, 39 - i, 8, 17 + i * 18, EMPTY_ARMOR_SLOT_TEXTURES.get(equipmentSlot), ARMOR_SLOT_TOOLTIPS.get(i)));
//			this.addSlot(new Slot(playerInventory, 39 - i, 98, 17 + i * 18) {
//
//				@Override
//				public void setStack(ItemStack stack, ItemStack previousStack) {
//					owner.onEquipStack(equipmentSlot, previousStack, stack);
//					super.setStack(stack, previousStack);
//				}
//
//				@Override
//				public int getMaxItemCount() {
//					return 1;
//				}
//
//				@Override
//				public boolean canInsert(ItemStack stack) {
//					boolean bl = true;
//					if (owner.getServer() != null) {
//						bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
//					}
//
//					Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
//					boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());
//
//					Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
//					boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());
//
//					boolean isOwned = ItemUtils.isOwnedByPlayer(stack, owner.getGameProfile());
//					boolean isCreative = owner.isCreative();
//
//					return (equipmentSlot == owner.getPreferredEquipmentSlot(stack) || rpginventory$isOfEquipmentTag(stack, equipmentSlot)) && isOwned && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
//				}
//
//				@Override
//				public boolean canTakeItems(PlayerEntity playerEntity) {
//					boolean bl = true;
//					if (owner.getServer() != null) {
//						bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
//					}
//
//					Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
//					boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());
//
//					Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
//					boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());
//
//					boolean isCreative = playerEntity.isCreative();
//
//					ItemStack itemStack = this.getStack();
//					return (
//							!itemStack.isEmpty()
//							&& !isCreative
//							&& EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE)
//							? false
//							: super.canTakeItems(playerEntity)
//					) && !this.getStack().contains(RPGInventory.LOAD_OUT_ITEM) && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
//				}
//
//				public Pair<Identifier, Identifier> getBackgroundSprite() {
//					return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, identifier);
//				}
//			})
		}
		// 40
		this.addSlot(new CustomArmorSlot(playerInventory, owner, EquipmentSlot.OFFHAND, 40, 44, 53, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT, List.of(Text.translatable("slot.tooltip.offhand"))));

//		this.addSlot(new CustomArmorSlot(playerInventory, owner, EquipmentSlot.MAINHAND, 41, 26, 53, EMPTY_HAND_SLOT, List.of(Text.translatable("slot.tooltip.hand"))));


		// 41 - 44
		MANNEQUIN_SLOTS_START = 41;
		for (int i = 0; i < 4; i++) {
			EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
			this.addSlot(new MannequinSlot(inventory, owner, equipmentSlot, i, 90 + 8, 17 + i * 18, EMPTY_ARMOR_SLOT_TEXTURES.get(equipmentSlot), ARMOR_SLOT_TOOLTIPS.get(i)));
//			this.addSlot(new Slot(playerInventory, 39 - i, 98, 17 + i * 18) {
//
//				@Override
//				public void setStack(ItemStack stack, ItemStack previousStack) {
//					owner.onEquipStack(equipmentSlot, previousStack, stack);
//					super.setStack(stack, previousStack);
//				}
//
//				@Override
//				public int getMaxItemCount() {
//					return 1;
//				}
//
//				@Override
//				public boolean canInsert(ItemStack stack) {
//					boolean bl = true;
//					if (owner.getServer() != null) {
//						bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
//					}
//
//					Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
//					boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());
//
//					Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
//					boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());
//
//					boolean isOwned = ItemUtils.isOwnedByPlayer(stack, owner.getGameProfile());
//					boolean isCreative = owner.isCreative();
//
//					return (equipmentSlot == owner.getPreferredEquipmentSlot(stack) || rpginventory$isOfEquipmentTag(stack, equipmentSlot)) && isOwned && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
//				}
//
//				@Override
//				public boolean canTakeItems(PlayerEntity playerEntity) {
//					boolean bl = true;
//					if (owner.getServer() != null) {
//						bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
//					}
//
//					Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
//					boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());
//
//					Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
//					boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());
//
//					boolean isCreative = playerEntity.isCreative();
//
//					ItemStack itemStack = this.getStack();
//					return (
//							!itemStack.isEmpty()
//							&& !isCreative
//							&& EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE)
//							? false
//							: super.canTakeItems(playerEntity)
//					) && !this.getStack().contains(RPGInventory.LOAD_OUT_ITEM) && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
//				}
//
//				public Pair<Identifier, Identifier> getBackgroundSprite() {
//					return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, identifier);
//				}
//			});
		}
		// 45
		this.addSlot(new MannequinSlot(inventory, owner, EquipmentSlot.OFFHAND, 4, 90 + 44, 53, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT, List.of(Text.translatable("slot.tooltip.offhand"))));
//		this.addSlot(new MannequinSlot(inventory, owner, EquipmentSlot.MAINHAND, 5, 90 + 26, 53, EMPTY_HAND_SLOT, List.of(Text.translatable("slot.tooltip.hand"))));

//		this.addSlot(new Slot(inventory, 0, 8, 17) {
//
//			public boolean canTakeItems(PlayerEntity playerEntity) {
//				return true; // TODO player has status effect
//			}
//
//			public Pair<Identifier, Identifier> getBackgroundSprite() {
//				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_HELMET_SLOT_TEXTURE);
//			}
//		});
//
//		this.addSlot(new Slot(inventory, 1, 8, 35) {
//
//			public boolean canTakeItems(PlayerEntity playerEntity) {
//				return true; // TODO player has status effect
//			}
//
//			public Pair<Identifier, Identifier> getBackgroundSprite() {
//				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE);
//			}
//		});
//
//		this.addSlot(new Slot(inventory, 2, 8, 53) {
//
//			public boolean canTakeItems(PlayerEntity playerEntity) {
//				return true; // TODO player has status effect
//			}
//
//			public Pair<Identifier, Identifier> getBackgroundSprite() {
//				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_LEGGINGS_SLOT_TEXTURE);
//			}
//		});
//
//		this.addSlot(new Slot(inventory, 3, 8, 71) {
//
//			public boolean canTakeItems(PlayerEntity playerEntity) {
//				return true; // TODO player has status effect
//			}
//
//			public Pair<Identifier, Identifier> getBackgroundSprite() {
//				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_BOOTS_SLOT_TEXTURE);
//			}
//		});

	}

	public PlayerInventory getPlayerInventory() {
		return this.playerInventory;
	}

	private void equip(PlayerEntity player) {

//		RPGInventory.LOGGER.info("equip");

		for (int i = 0; i < MannequinBlockEntity.INVENTORY_SIZE; i++) {
			ItemStack equipmentStack = this.slots.get(EQUIPMENT_SLOTS_START + i).getStack();
			ItemStack mannequinStack = this.slots.get(MANNEQUIN_SLOTS_START + i).getStack();

			if (!mannequinStack.isEmpty() && (equipmentStack.isEmpty() || equipmentStack.contains(RPGInventory.LOAD_OUT_ITEM))) {
				ItemStack newStack = mannequinStack.copy();
				newStack.set(RPGInventory.LOAD_OUT_ITEM, Unit.INSTANCE);
				this.slots.get(EQUIPMENT_SLOTS_START + i).setStack(newStack);
			}
		}

	}

	private void unequip(PlayerEntity player) {

//		RPGInventory.LOGGER.info("unequip");

		for (int i = 0; i < MannequinBlockEntity.INVENTORY_SIZE; i++) {
			ItemStack itemStack = this.slots.get(EQUIPMENT_SLOTS_START + i).getStack();
			if (!itemStack.isEmpty() && itemStack.contains(RPGInventory.LOAD_OUT_ITEM)) {
				this.slots.get(EQUIPMENT_SLOTS_START + i).setStack(ItemStack.EMPTY);
			}
		}

	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
//		ItemStack itemStack = this.inventory.getStack(0);
//		ItemStack itemStack2 = this.inventory.getStack(1);
//		ServerConfig serverConfig = RPGEnchanting.SERVER_CONFIG;
//		if (itemStack.isEmpty()) {
//			return false;
//		}
		if (id == 1) {

			this.equip(player);

//			MutablePair<RegistryEntry.Reference<Enchantment>, Integer> newEnchantment = this.current_prefix_enchantments.get(id);
//
//			int experience_cost_amount = this.existing_enchantment_costs[0] + (int) Math.max(0, Math.floor(newEnchantment.getLeft().value().getMaxPower(newEnchantment.getRight()) * serverConfig.new_enchantment_exp_cost_multiplier.get()));
//			if (player.experienceLevel < experience_cost_amount && !player.isInCreativeMode()) {
//				return false;
//			}
//			int item_cost_amount = this.existing_enchantment_costs[1] + (int) Math.max(0, Math.floor(newEnchantment.getLeft().value().getAnvilCost() * newEnchantment.getRight() * serverConfig.new_enchantment_item_cost_multiplier.get()));
//			if ((!itemStack2.isOf(Registries.ITEM.get(serverConfig.prefix_item_cost.get())) || itemStack2.getCount() < item_cost_amount) && !player.isInCreativeMode()) {
//				return false;
//			}
//			player.applyEnchantmentCosts(itemStack, experience_cost_amount);
//			ItemEnchantmentsComponent.Builder itemEnchantmentsComponentBuilder = new ItemEnchantmentsComponent.Builder(itemStack.getEnchantments());
//			if (this.existing_prefix_enchantment != null) {
//				itemEnchantmentsComponentBuilder.set(this.existing_prefix_enchantment.getLeft(), 0);
//			}
//			itemEnchantmentsComponentBuilder.add(newEnchantment.getLeft(), newEnchantment.getRight());
//			itemStack.set(DataComponentTypes.ENCHANTMENTS, itemEnchantmentsComponentBuilder.build().withShowInTooltip(false));
//			itemStack.set(RPGEnchanting.SHOW_ENCHANTMENT_NAME_ADDITIONS, Unit.INSTANCE);
//			if (serverConfig.enable_enchanted_by_player_component_application.get()) {
//				itemStack.set(RPGEnchanting.PLAYER_ENCHANTED, new ProfileComponent(player.getGameProfile()));
//			}
//
//			itemStack2.decrementUnlessCreative(item_cost_amount, player);
//			if (itemStack2.isEmpty()) {
//				this.inventory.setStack(1, ItemStack.EMPTY);
//			}
//
//			player.incrementStat(Stats.ENCHANT_ITEM);
//			if (player instanceof ServerPlayerEntity) {
//				Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity) player, itemStack, experience_cost_amount);
//			}
//
//			this.inventory.markDirty();
//			this.onContentChanged(this.inventory);
//
//			world.playSound(null, this.blockPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);

		} else if (id == 2) {

			this.unequip(player);

//			MutablePair<RegistryEntry.Reference<Enchantment>, Integer> newEnchantment = this.current_suffix_enchantments.get(id - first_threshold);
//
//			int experience_cost_amount = this.existing_enchantment_costs[2] + (int) Math.max(0, Math.floor(newEnchantment.getLeft().value().getMaxPower(newEnchantment.getRight()) * serverConfig.new_enchantment_exp_cost_multiplier.get()));
//			if (player.experienceLevel < experience_cost_amount && !player.isInCreativeMode()) {
//				return false;
//			}
//			int item_cost_amount = this.existing_enchantment_costs[3] + (int) Math.max(0, Math.floor(newEnchantment.getLeft().value().getAnvilCost() * newEnchantment.getRight() * serverConfig.new_enchantment_item_cost_multiplier.get()));
//			if ((!itemStack2.isOf(Registries.ITEM.get(serverConfig.suffix_item_cost.get())) || itemStack2.getCount() < item_cost_amount) && !player.isInCreativeMode()) {
//				return false;
//			}
//			player.applyEnchantmentCosts(itemStack, experience_cost_amount);
//
//			ItemEnchantmentsComponent.Builder itemEnchantmentsComponentBuilder = new ItemEnchantmentsComponent.Builder(itemStack.getEnchantments());
//			if (this.existing_suffix_enchantment != null) {
//				itemEnchantmentsComponentBuilder.set(this.existing_suffix_enchantment.getLeft(), 0);
//			}
//			itemEnchantmentsComponentBuilder.add(newEnchantment.getLeft(), newEnchantment.getRight());
//			itemStack.set(DataComponentTypes.ENCHANTMENTS, itemEnchantmentsComponentBuilder.build().withShowInTooltip(false));
//			itemStack.set(RPGEnchanting.SHOW_ENCHANTMENT_NAME_ADDITIONS, Unit.INSTANCE);
//			if (serverConfig.enable_enchanted_by_player_component_application.get()) {
//				itemStack.set(RPGEnchanting.PLAYER_ENCHANTED, new ProfileComponent(player.getGameProfile()));
//			}
//
//			itemStack2.decrementUnlessCreative(item_cost_amount, player);
//			if (itemStack2.isEmpty()) {
//				this.inventory.setStack(1, ItemStack.EMPTY);
//			}
//
//			player.incrementStat(Stats.ENCHANT_ITEM);
//			if (player instanceof ServerPlayerEntity) {
//				Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity) player, itemStack, experience_cost_amount);
//			}
//
//			this.inventory.markDirty();
//			this.onContentChanged(this.inventory);
//
//			world.playSound(null, this.blockPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);

		} else {
			Util.error(player.getName() + " pressed invalid button id: " + id);
			return false;
		}
		return true;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		ItemStack itemStack = ItemStack.EMPTY;
//		Slot slot2 = this.slots.get(slot);
//		if (slot2 != null && slot2.hasStack()) {
//			ItemStack itemStack2 = slot2.getStack();
//			itemStack = itemStack2.copy();
//			if (slot < 9) {
//				if (!this.insertItem(itemStack2, 9, 45, true)) {
//					return ItemStack.EMPTY;
//				}
//			} else if (!this.insertItem(itemStack2, 0, 9, false)) {
//				return ItemStack.EMPTY;
//			}
//
//			if (itemStack2.isEmpty()) {
//				slot2.setStack(ItemStack.EMPTY);
//			} else {
//				slot2.markDirty();
//			}
//
//			if (itemStack2.getCount() == itemStack.getCount()) {
//				return ItemStack.EMPTY;
//			}
//
//			slot2.onTakeItem(player, itemStack2);
//		}

		return itemStack;
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		this.inventory.onClose(player);
	}

	static {
		EMPTY_ARMOR_SLOT_TEXTURES = Map.of(
				EquipmentSlot.HEAD,
				PlayerScreenHandler.EMPTY_HELMET_SLOT_TEXTURE,
				EquipmentSlot.CHEST,
				PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE,
				EquipmentSlot.LEGS,
				PlayerScreenHandler.EMPTY_LEGGINGS_SLOT_TEXTURE,
				EquipmentSlot.FEET,
				PlayerScreenHandler.EMPTY_BOOTS_SLOT_TEXTURE
		);
		EQUIPMENT_SLOT_ORDER = new EquipmentSlot[]{
				EquipmentSlot.HEAD,
				EquipmentSlot.CHEST,
				EquipmentSlot.LEGS,
				EquipmentSlot.FEET
		};
		ARMOR_SLOT_TOOLTIPS = List.of(
				List.of(Text.translatable("slot.tooltip.head")),
				List.of(Text.translatable("slot.tooltip.chest")),
				List.of(Text.translatable("slot.tooltip.legs")),
				List.of(Text.translatable("slot.tooltip.feet"))
		);
	}
}
