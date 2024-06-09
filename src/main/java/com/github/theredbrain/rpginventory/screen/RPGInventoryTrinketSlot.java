package com.github.theredbrain.rpginventory.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.TrinketsClient;
import dev.emi.trinkets.api.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class RPGInventoryTrinketSlot extends Slot implements TrinketSlot {
    private final SlotGroup group;
    private final SlotType type;
    private final boolean alwaysVisible;
    private final int slotOffset;
    private final TrinketInventory trinketInventory;

    public RPGInventoryTrinketSlot(TrinketInventory inventory, int index, int x, int y, SlotGroup group, SlotType type, int slotOffset,
                                   boolean alwaysVisible) {
        super(inventory, index, x, y);
        this.group = group;
        this.type = type;
        this.slotOffset = slotOffset;
        this.alwaysVisible = alwaysVisible;
        this.trinketInventory = inventory;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
        boolean bl2 = true;
        if (livingEntity.getServer() != null) {
            bl2 = livingEntity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
        }
        StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
        boolean hasCivilisationEffect = civilisation_status_effect != null && livingEntity.hasStatusEffect(civilisation_status_effect);

        StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
        boolean hasWildernessEffect = wilderness_status_effect != null && livingEntity.hasStatusEffect(wilderness_status_effect);

        return TrinketSlot.canInsert(stack, new SlotReference(trinketInventory, slotOffset), trinketInventory.getComponent().getEntity()) && (hasCivilisationEffect || !(livingEntity instanceof PlayerEntity playerEntity) || playerEntity.isCreative() || (bl2 && !hasWildernessEffect));
    }

    @Override
    public boolean canTakeItems(PlayerEntity player) {
        ItemStack stack = this.getStack();

        StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
        boolean hasCivilisationEffect = civilisation_status_effect != null && player.hasStatusEffect(civilisation_status_effect);

        StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
        boolean hasWildernessEffect = wilderness_status_effect != null && player.hasStatusEffect(wilderness_status_effect);

        return TrinketsApi.getTrinket(stack.getItem()).canUnequip(stack, new SlotReference(trinketInventory, slotOffset), player) && (hasCivilisationEffect || (player.getServer() == null || player.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT) && !hasWildernessEffect) || player.isCreative());
    }

    @Override
    public boolean isEnabled() {
        boolean bl = isTrinketFocused();
        if (alwaysVisible) {
            bl = true;
            if (x < 0) {
                if (trinketInventory.getComponent().getEntity().getWorld().isClient) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    Screen s = client.currentScreen;
                    if (s instanceof InventoryScreen screen) {
                        if (screen.getRecipeBookWidget().isOpen()) {
                            bl = false;
                        }
                    }
                }
            }
        }
        return super.isEnabled() && bl
                && !((Objects.equals(this.group.getName(), "spell_slot_1") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 1)
                || (Objects.equals(this.group.getName(), "spell_slot_2") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 2)
                || (Objects.equals(this.group.getName(), "spell_slot_3") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 3)
                || (Objects.equals(this.group.getName(), "spell_slot_4") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 4)
                || (Objects.equals(this.group.getName(), "spell_slot_5") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 5)
                || (Objects.equals(this.group.getName(), "spell_slot_6") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 6)
                || (Objects.equals(this.group.getName(), "spell_slot_7") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 7)
                || (Objects.equals(this.group.getName(), "spell_slot_8") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 8)
        );
    }

    @Override
    public boolean isTrinketFocused() {
        if (TrinketsClient.activeGroup == group) {
            return slotOffset == 0 || TrinketsClient.activeType == type;
        } else if (TrinketsClient.quickMoveGroup == group) {
            return slotOffset == 0 || TrinketsClient.quickMoveType == type && TrinketsClient.quickMoveTimer > 0;
        }
        return false;
    }

    @Override
    public Identifier getBackgroundIdentifier() {
        return type.getIcon();
    }

    @Override
    public SlotType getType() {
        return type;
    }
}
