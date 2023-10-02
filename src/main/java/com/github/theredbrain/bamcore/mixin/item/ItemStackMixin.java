package com.github.theredbrain.bamcore.mixin.item;

import com.github.theredbrain.bamcore.api.item.CustomArmorItem;
import com.github.theredbrain.bamcore.api.item.CustomDyeableArmorItem;
import com.google.common.collect.*;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = ItemStack.class, priority = 950)
public abstract class ItemStackMixin {

    @Shadow
    public Item getItem() {
        throw new AssertionError();
    }

    @Shadow
    public abstract boolean hasNbt();

    @Shadow
    private static boolean isSectionVisible(int flags, ItemStack.TooltipSection tooltipSection) {
        throw new AssertionError();
    }

    @Shadow
    private @Nullable NbtCompound nbt;

    @Shadow
    private static Collection<Text> parseBlockTag(String tag) {
        throw new AssertionError();
    }

    @Shadow
    public static void appendEnchantments(List<Text> tooltip, NbtList enchantments) {
        throw new AssertionError();
    }

    @Shadow
    private int getHideFlags() {
        throw new AssertionError();
    }

    @Shadow
    public boolean hasCustomName() {
        throw new AssertionError();
    }

    @Shadow
    public boolean isOf(Item item) {
        throw new AssertionError();
    }

    @Shadow
    public NbtList getEnchantments() {
        throw new AssertionError();
    }

    @Shadow
    public Text getName() {
        throw new AssertionError();
    }

    @Shadow
    public Rarity getRarity() {
        throw new AssertionError();
    }

    @Shadow
    public boolean isDamaged() {
        throw new AssertionError();
    }

    @Shadow
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        throw new AssertionError();
    }

    @Shadow
    public int getMaxDamage() {
        throw new AssertionError();
    }

    @Shadow
    public int getDamage() {
        throw new AssertionError();
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
    public void bam$getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        if (this.getItem() instanceof CustomArmorItem && !(((CustomArmorItem)this.getItem()).isProtecting((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
        if (this.getItem() instanceof CustomDyeableArmorItem && !(((CustomDyeableArmorItem)this.getItem()).isProtecting((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public List<Text> getTooltip(@Nullable PlayerEntity player, TooltipContext context) {
        List<Text> list = Lists.newArrayList();
        MutableText mutableText = Text.empty().append(this.getName()).formatted(this.getRarity().formatting);
        if (this.hasCustomName()) {
            mutableText.formatted(Formatting.ITALIC);
        }

        list.add(mutableText);
        if (!context.isAdvanced() && !this.hasCustomName() && this.isOf(Items.FILLED_MAP)) {
            Integer integer = FilledMapItem.getMapId((ItemStack) (Object) this);
            if (integer != null) {
                list.add(Text.literal("#" + integer).formatted(Formatting.GRAY));
            }
        }

        int i = this.getHideFlags();
        if (isSectionVisible(i, ItemStack.TooltipSection.ADDITIONAL)) {
            this.getItem().appendTooltip((ItemStack) (Object) this, player == null ? null : player.getWorld(), list, context);
        }

        int j;
        if (this.hasNbt()) {
            if (isSectionVisible(i, ItemStack.TooltipSection.UPGRADES) && player != null) {
                ArmorTrim.appendTooltip((ItemStack) (Object) this, player.getWorld().getRegistryManager(), list);
            }

            if (isSectionVisible(i, ItemStack.TooltipSection.ENCHANTMENTS)) {
                appendEnchantments(list, this.getEnchantments());
            }

            if (this.nbt.contains("display", NbtElement.COMPOUND_TYPE)) {
                NbtCompound nbtCompound = this.nbt.getCompound("display");
                if (isSectionVisible(i, ItemStack.TooltipSection.DYE) && nbtCompound.contains("color", NbtElement.NUMBER_TYPE)) {
                    if (context.isAdvanced()) {
                        list.add(Text.translatable("item.color", String.format(Locale.ROOT, "#%06X", nbtCompound.getInt("color"))).formatted(Formatting.GRAY));
                    } else {
                        list.add(Text.translatable("item.dyed").formatted(Formatting.GRAY, Formatting.ITALIC));
                    }
                }

                if (nbtCompound.getType("Lore") == NbtElement.LIST_TYPE) {
                    NbtList nbtList = nbtCompound.getList("Lore", NbtElement.STRING_TYPE);

                    for(j = 0; j < nbtList.size(); ++j) {
                        String string = nbtList.getString(j);

                        try {
                            MutableText mutableText2 = Text.Serializer.fromJson(string);
                            if (mutableText2 != null) {
                                list.add(Texts.setStyleIfAbsent(mutableText2, ItemStack.LORE_STYLE));
                            }
                        } catch (Exception var19) {
                            nbtCompound.remove("Lore");
                        }
                    }
                }
            }
        }

        int k;

        //region Credits to Emi
        TrinketsApi.getTrinketComponent(player).ifPresent(comp -> {
            ItemStack self = (ItemStack) (Object) this;
            boolean canEquipAnywhere = true;
            Set<SlotType> slots = Sets.newHashSet();
            Map<SlotType, Multimap<EntityAttribute, EntityAttributeModifier>> modifiers = Maps.newHashMap();
            Multimap<EntityAttribute, EntityAttributeModifier> defaultModifier = null;
            boolean allModifiersSame = true;
            int slotCount = 0;

            for (Map.Entry<String, Map<String, TrinketInventory>> group : comp.getInventory().entrySet()) {
                outer:
                for (Map.Entry<String, TrinketInventory> inventory : group.getValue().entrySet()) {
                    TrinketInventory trinketInventory = inventory.getValue();
                    SlotType slotType = trinketInventory.getSlotType();
                    slotCount++;
                    boolean anywhereButHidden = false;
                    for (int m = 0; m < trinketInventory.size(); m++) {
                        SlotReference ref = new SlotReference(trinketInventory, m);
                        boolean res = TrinketsApi.evaluatePredicateSet(slotType.getTooltipPredicates(), self, ref, player);
                        boolean canInsert = TrinketSlot.canInsert(self, ref, player);
                        if (res && canInsert) {
                            boolean sameTranslationExists = false;
                            for (SlotType t : slots) {
                                if (t.getTranslation().getString().equals(slotType.getTranslation().getString())) {
                                    sameTranslationExists = true;
                                    break;
                                }
                            }
                            if (!sameTranslationExists) {
                                slots.add(slotType);
                            }
                            Trinket trinket = TrinketsApi.getTrinket((self).getItem());

                            Multimap<EntityAttribute, EntityAttributeModifier> map =
                                    trinket.getModifiers(self, ref, player, SlotAttributes.getUuid(ref));

                            if (defaultModifier == null) {
                                defaultModifier = map;
                            } else if (allModifiersSame) {
                                allModifiersSame = areMapsEqual(defaultModifier, map);
                            }

                            boolean duplicate = false;
                            for (var entry : modifiers.entrySet()) {
                                if (entry.getKey().getTranslation().getString().equals(slotType.getTranslation().getString())) {
                                    if (areMapsEqual(entry.getValue(), map)) {
                                        duplicate = true;
                                        break;
                                    }
                                }
                            }

                            if (!duplicate) {
                                modifiers.put(slotType, map);
                            }
                            continue outer;
                        } else if (canInsert) {
                            anywhereButHidden = true;
                        }
                    }
                    if (!anywhereButHidden) {
                        canEquipAnywhere = false;
                    }
                }
            }

            if (canEquipAnywhere && slotCount > 1) {
                list.add(Text.translatable("trinkets.tooltip.slots.any").formatted(Formatting.GRAY));
            } else if (slots.size() > 1) {
                list.add(Text.translatable("trinkets.tooltip.slots.list").formatted(Formatting.GRAY));
                for (SlotType type : slots) {
                    list.add(type.getTranslation().formatted(Formatting.BLUE));
                }
            } else if (slots.size() == 1) {
                // Should only run once
                for (SlotType type : slots) {
                    list.add(Text.translatable("trinkets.tooltip.slots.single",
                            type.getTranslation().formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
                }
            }

            if (modifiers.size() > 0) {
                if (allModifiersSame) {
                    if (defaultModifier != null && !defaultModifier.isEmpty()) {
                        list.add(Text.translatable("trinkets.tooltip.attributes.all").formatted(Formatting.GRAY));
                        addAttributes(list, defaultModifier);
                    }
                } else {
                    for (SlotType type : modifiers.keySet()) {
                        list.add(Text.translatable("trinkets.tooltip.attributes.single",
                                type.getTranslation().formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
                        addAttributes(list, modifiers.get(type));
                    }
                }
            }
        });
        //endregion Credits to Emi

        if (isSectionVisible(i, ItemStack.TooltipSection.MODIFIERS)) {
            EquipmentSlot[] var21 = EquipmentSlot.values();
            k = var21.length;

            for(j = 0; j < k; ++j) {
                EquipmentSlot equipmentSlot = var21[j];
                Multimap<EntityAttribute, EntityAttributeModifier> multimap = this.getAttributeModifiers(equipmentSlot);
                if (!multimap.isEmpty()) {
                    list.add(ScreenTexts.EMPTY);
                    list.add(Text.translatable("item.modifiers." + equipmentSlot.getName()).formatted(Formatting.GRAY));
                    Iterator var11 = multimap.entries().iterator();

                    while(var11.hasNext()) {
                        Map.Entry<EntityAttribute, EntityAttributeModifier> entry = (Map.Entry)var11.next();
                        EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier)entry.getValue();
                        double d = entityAttributeModifier.getValue();
                        boolean bl = false;
                        if (player != null) {
                            if (entityAttributeModifier.getId() == Item.ATTACK_DAMAGE_MODIFIER_ID) {
                                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                                d += (double) EnchantmentHelper.getAttackDamage((ItemStack) (Object) this, EntityGroup.DEFAULT);
                                bl = true;
                            } else if (entityAttributeModifier.getId() == Item.ATTACK_SPEED_MODIFIER_ID) {
                                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
                                bl = true;
                            }
                        }

                        double e;
                        if (entityAttributeModifier.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && entityAttributeModifier.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
                            if (((EntityAttribute)entry.getKey()).equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
                                e = d * 10.0;
                            } else {
                                e = d;
                            }
                        } else {
                            e = d * 100.0;
                        }

                        if (bl) {
                            list.add(ScreenTexts.space().append((Text)Text.translatable("attribute.modifier.equals." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(((EntityAttribute)entry.getKey()).getTranslationKey()))).formatted(Formatting.DARK_GREEN));
                        } else if (d > 0.0) {
                            list.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(((EntityAttribute)entry.getKey()).getTranslationKey())).formatted(Formatting.BLUE));
                        } else if (d < 0.0) {
                            e *= -1.0;
                            list.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(((EntityAttribute)entry.getKey()).getTranslationKey())).formatted(Formatting.RED));
                        }
                    }
                }
            }
        }

        if (this.hasNbt()) {
            if (isSectionVisible(i, ItemStack.TooltipSection.UNBREAKABLE) && this.nbt.getBoolean("Unbreakable")) {
                list.add(Text.translatable("item.unbreakable").formatted(Formatting.BLUE));
            }

            NbtList nbtList2;
            if (isSectionVisible(i, ItemStack.TooltipSection.CAN_DESTROY) && this.nbt.contains("CanDestroy", NbtElement.LIST_TYPE)) {
                nbtList2 = this.nbt.getList("CanDestroy", NbtElement.STRING_TYPE);
                if (!nbtList2.isEmpty()) {
                    list.add(ScreenTexts.EMPTY);
                    list.add(Text.translatable("item.canBreak").formatted(Formatting.GRAY));

                    for(k = 0; k < nbtList2.size(); ++k) {
                        list.addAll(parseBlockTag(nbtList2.getString(k)));
                    }
                }
            }

            if (isSectionVisible(i, ItemStack.TooltipSection.CAN_PLACE) && this.nbt.contains("CanPlaceOn", NbtElement.LIST_TYPE)) {
                nbtList2 = this.nbt.getList("CanPlaceOn", NbtElement.STRING_TYPE);
                if (!nbtList2.isEmpty()) {
                    list.add(ScreenTexts.EMPTY);
                    list.add(Text.translatable("item.canPlace").formatted(Formatting.GRAY));

                    for(k = 0; k < nbtList2.size(); ++k) {
                        list.addAll(parseBlockTag(nbtList2.getString(k)));
                    }
                }
            }
        }

        if (context.isAdvanced()) {
            if (this.isDamaged()) {
                list.add(Text.translatable("item.durability", this.getMaxDamage() - this.getDamage(), this.getMaxDamage()));
            }

            list.add(Text.literal(Registries.ITEM.getId(this.getItem()).toString()).formatted(Formatting.DARK_GRAY));
            if (this.hasNbt()) {
                list.add(Text.translatable("item.nbt_tags", this.nbt.getKeys().size()).formatted(Formatting.DARK_GRAY));
            }
        }

        if (player != null && !this.getItem().isEnabled(player.getWorld().getEnabledFeatures())) {
            list.add(ItemStack.DISABLED_TEXT);
        }

        return list;
    }

    //region Credits to Emi
    @Unique
    private void addAttributes(List<Text> list, Multimap<EntityAttribute, EntityAttributeModifier> map) {
        if (!map.isEmpty()) {
            for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entries()) {
                EntityAttribute attribute = entry.getKey();
                EntityAttributeModifier modifier = entry.getValue();
                double g = modifier.getValue();

                if (modifier.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && modifier.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
                    if (entry.getKey().equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
                        g *= 10.0D;
                    }
                } else {
                    g *= 100.0D;
                }

                Text text = Text.translatable(attribute.getTranslationKey());
                if (attribute instanceof SlotAttributes.SlotEntityAttribute) {
                    text = Text.translatable("trinkets.tooltip.attributes.slots", text);
                }
                if (g > 0.0D) {
                    list.add(Text.translatable("attribute.modifier.plus." + modifier.getOperation().getId(),
                            ItemStack.MODIFIER_FORMAT.format(g), text).formatted(Formatting.BLUE));
                } else if (g < 0.0D) {
                    g *= -1.0D;
                    list.add(Text.translatable("attribute.modifier.take." + modifier.getOperation().getId(),
                            ItemStack.MODIFIER_FORMAT.format(g), text).formatted(Formatting.RED));
                }
            }
        }
    }

    // `equals` doesn't test thoroughly
    @Unique
    private boolean areMapsEqual(Multimap<EntityAttribute, EntityAttributeModifier> map1, Multimap<EntityAttribute, EntityAttributeModifier> map2) {
        if (map1.size() != map2.size()) {
            return false;
        } else {
            for (EntityAttribute attribute : map1.keySet()) {
                if (!map2.containsKey(attribute)) {
                    return false;
                }

                Collection<EntityAttributeModifier> col1 = map1.get(attribute);
                Collection<EntityAttributeModifier> col2 = map2.get(attribute);

                if (col1.size() != col2.size()) {
                    return false;
                }
                // removed because this bloated up my tooltips
//                else {
//                    Iterator<EntityAttributeModifier> iter = col2.iterator();
//
//                    for (EntityAttributeModifier modifier : col1) {
//                        EntityAttributeModifier eam = iter.next();
//
//                        if (!modifier.toNbt().equals(eam.toNbt())) {
//                            return false;
//                        }
//                    }
//                }
            }
        }
        return true;
    }
    //endregion Credits to Emi
}
