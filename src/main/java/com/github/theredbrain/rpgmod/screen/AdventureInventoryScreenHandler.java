package com.github.theredbrain.rpgmod.screen;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlotType;
import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgmod.item.AccessoryRingItem;
import com.github.theredbrain.rpgmod.registry.StatusEffectsRegistry;
import com.github.theredbrain.rpgmod.registry.Tags;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class AdventureInventoryScreenHandler extends ScreenHandler {
    public static final Identifier BLOCK_ATLAS_TEXTURE = new Identifier("textures/atlas/blocks.png");
    public static final Identifier EMPTY_BELT_SLOT_TEXTURE = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_belt");
    public static final Identifier EMPTY_BOOTS_SLOT_TEXTURE = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_boots");
    public static final Identifier EMPTY_CHESTPLATE_SLOT_TEXTURE = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_chestplate");
    public static final Identifier EMPTY_GLOVES_SLOT_TEXTURE = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_gloves");
    public static final Identifier EMPTY_HELMET_SLOT_TEXTURE = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_helmet");
    public static final Identifier EMPTY_LEGGINGS_SLOT_TEXTURE = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_leggings");
    public static final Identifier EMPTY_NECKLACE_SLOT_TEXTURE = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_necklace");
    public static final Identifier EMPTY_RING_SLOT_TEXTURE = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_ring_2");
    public static final Identifier EMPTY_OFFHAND_ARMOR_SLOT = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_shield");
    public static final Identifier EMPTY_SHOULDERS_SLOT_TEXTURE = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_shoulders");
    public static final Identifier EMPTY_MAINHAND_ARMOR_SLOT = new Identifier(RPGMod.MOD_ID, "item/empty_armor_slot_sword");
    public final boolean onServer;
    private final PlayerEntity owner;

    public AdventureInventoryScreenHandler(PlayerInventory inventory, boolean onServer, final PlayerEntity owner) {
        super(null, 0);
        int i;
        this.onServer = onServer;
        this.owner = owner;
        // hotbar
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 160) {

                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.isIn(Tags.ADVENTURE_HOTBAR_ITEMS) || !(((DuckPlayerEntityMixin)owner).isAdventure()) || owner.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT);
                }
            });
        }
        // main inventory
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + (i + 1) * 9, 8 + j * 18, 102 + i * 18));
            }
        }
        // boots slot
        this.addSlot(new Slot(inventory, 36, 8, 80){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.FEET, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (EquipmentSlot.FEET == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_BOOTS_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_BOOTS_SLOT_TEXTURE);
            }
        });
        // leggings slot
        this.addSlot(new Slot(inventory, 37, 8, 62){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.LEGS, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (EquipmentSlot.LEGS == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_LEGGINGS_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_LEGGINGS_SLOT_TEXTURE);
            }
        });
        // chestplate slot
        this.addSlot(new Slot(inventory, 38, 8, 44){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.CHEST, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (EquipmentSlot.CHEST == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_CHESTPLATE_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_CHESTPLATE_SLOT_TEXTURE);
            }
        });
        // helmet slot
        this.addSlot(new Slot(inventory, 39, 8, 26){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.HEAD, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (EquipmentSlot.HEAD == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_HELMET_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_HELMET_SLOT_TEXTURE);
            }
        });
        // gloves slot
        this.addSlot(new Slot(inventory, 40, 77, 62){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(ExtendedEquipmentSlot.GLOVES, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (ExtendedEquipmentSlot.GLOVES == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.GLOVES_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_GLOVES_SLOT_TEXTURE);
            }
        });
        // shoulders slot
        this.addSlot(new Slot(inventory, 41, 77, 44){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(ExtendedEquipmentSlot.SHOULDERS, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (ExtendedEquipmentSlot.SHOULDERS == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.SHOULDERS_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_SHOULDERS_SLOT_TEXTURE);
            }
        });
        // first ring slot
        this.addSlot(new Slot(inventory, 42, 33, 8){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(ExtendedEquipmentSlot.FIRST_RING, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.RING_ITEMS) && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_RING_SLOT_TEXTURE);
            }
        });
        // second ring slot
        this.addSlot(new Slot(inventory, 43, 52, 8){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(ExtendedEquipmentSlot.SECOND_RING, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.RING_ITEMS) && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_RING_SLOT_TEXTURE);
            }
        });
        // necklace slot
        this.addSlot(new Slot(inventory, 44, 77, 26){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(ExtendedEquipmentSlot.NECKLACE, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.NECKLACE_ITEMS) && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_NECKLACE_SLOT_TEXTURE);
            }
        });
        // belt slot
        this.addSlot(new Slot(inventory, 45, 77, 80){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(ExtendedEquipmentSlot.BELT, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.BELT_ITEMS) && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_BELT_SLOT_TEXTURE);
            }
        });
        // mainhand slot
        this.addSlot(new Slot(inventory, 46, 98, 62){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.MAINHAND, itemStack, stack);
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_MAINHAND_ARMOR_SLOT);
            }
        });
        // offhand slot
        this.addSlot(new Slot(inventory, 47, 98, 81){

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.OFFHAND, itemStack, stack);
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_OFFHAND_ARMOR_SLOT);
            }
        });
        // alternative mainhand slot
        this.addSlot(new Slot(inventory, 48, 116, 62){

            @Override
            public boolean canInsert(ItemStack stack) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_MAINHAND_ARMOR_SLOT);
            }
        });
        // alternative offhand slot
        this.addSlot(new Slot(inventory, 49, 116, 81){

            @Override
            public boolean canInsert(ItemStack stack) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_OFFHAND_ARMOR_SLOT);
            }
        });
    }

    public static boolean isInHotbar(int slot) {
        return slot >= 0 && slot < 9;// || slot == 45;
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        boolean bl = super.insertItem(stack, startIndex, endIndex, fromLast);
        RPGMod.LOGGER.info("insertItem: " + bl);
        return bl;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            int i;
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
//            if (itemStack.getItem() instanceof AccessoryRingItem) {
//
//            }
//            if (slot == 0) {
//                if (!this.insertItem(itemStack2, 9, 45, true)) {
//                    return ItemStack.EMPTY;
//                }
//                slot2.onQuickTransfer(itemStack2, itemStack);
//            } else
            if ((slot >= 36 && slot < 50 ? !this.insertItem(itemStack2, 0, 36, false)
                    : (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR && !((Slot)this.slots.get(36 + equipmentSlot.getEntitySlotId())).hasStack() ? !this.insertItem(itemStack2, i = 36 + equipmentSlot.getEntitySlotId(), i + 1, false)
                    : (equipmentSlot.getType() == ExtendedEquipmentSlotType.ACCESSORY && !((Slot)this.slots.get(42 + equipmentSlot.getEntitySlotId())).hasStack() ? !this.insertItem(itemStack2, i = 42 + equipmentSlot.getEntitySlotId(), i + 1, false)
                    : (itemStack.getItem() instanceof AccessoryRingItem && !((Slot)this.slots.get(42)).hasStack() ? !this.insertItem(itemStack2, 42, 43, false)
                    : (itemStack.getItem() instanceof AccessoryRingItem && !((Slot)this.slots.get(43)).hasStack() ? !this.insertItem(itemStack2, 43, 44, false)
                    : (equipmentSlot.getType() == EquipmentSlot.Type.HAND && !((Slot)this.slots.get(46 + equipmentSlot.getEntitySlotId())).hasStack() ? !this.insertItem(itemStack2, i = 46 + equipmentSlot.getEntitySlotId(), i + 1, false)
                    : (slot >= 9 && slot < 36 ? !this.insertItem(itemStack2, 0, 9, false)
                    : (slot >= 0 && slot < 9 ? !this.insertItem(itemStack2, 9, 36, false)
                    : !this.insertItem(itemStack2, 0, 36, false)))))))))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot2.onTakeItem(player, itemStack2);
//            if (slot == 0) {
//                player.dropItem(itemStack2, false);
//            }
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
