package com.github.theredbrain.betteradventuremode.mixin.item;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.item.BasicWeaponItem;
import com.github.theredbrain.betteradventuremode.item.BasicShieldItem;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import com.google.common.collect.*;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public Item getItem() {
        throw new AssertionError();
    }

    @Shadow
    public Text getName() {
        throw new AssertionError();
    }

    @Shadow
    public int getDamage() {
        throw new AssertionError();
    }

    @Shadow public abstract boolean isDamageable();

    @Shadow public abstract void setDamage(int damage);

    @Shadow public abstract int getMaxDamage();

    @Shadow public abstract boolean canPlaceOn(Registry<Block> blockRegistry, CachedBlockPosition pos);

    /**
     * @author TheRedBrain
     * @reason TODO
     */
    @Overwrite
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        BlockPos blockPos = context.getBlockPos();

        CachedBlockPosition cachedBlockPosition = new CachedBlockPosition(context.getWorld(), blockPos, false);
        if (playerEntity != null && !playerEntity.getAbilities().allowModifyWorld && !playerEntity.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)/* && !bl*/ && !this.canPlaceOn(context.getWorld().getRegistryManager().get(RegistryKeys.BLOCK), cachedBlockPosition)) {
            return ActionResult.PASS;
        }
        Item item = this.getItem();
        ActionResult actionResult = item.useOnBlock(context);
        if (playerEntity != null && actionResult.shouldIncrementStat()) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(item));
        }
        return actionResult;
    }

    /**
     * @author TheRedBrain
     * @reason TODO
     */
    @Overwrite
    public boolean damage(int amount, Random random, @Nullable ServerPlayerEntity player) {
        boolean unbreaking_level_3_item_equipped = false;
        if (player != null) {
            Predicate<ItemStack> predicate = stack -> stack.isIn(Tags.GRANTS_UNBREAKING_LEVEL_3);
            Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
            if (trinkets.isPresent()) {
                unbreaking_level_3_item_equipped = trinkets.get().isEquipped(predicate);
            }
            unbreaking_level_3_item_equipped = unbreaking_level_3_item_equipped || ((DuckLivingEntityMixin)player).betteradventuremode$hasEquipped(predicate);

        }

        int i;
        if (!this.isDamageable()) {
            return false;
        }
        if (amount > 0) {
            i = unbreaking_level_3_item_equipped ? 3 : EnchantmentHelper.getLevel(Enchantments.UNBREAKING, (ItemStack) (Object) this);
            int j = 0;
            for (int k = 0; i > 0 && k < amount; ++k) {
                if (!UnbreakingEnchantment.shouldPreventDamage((ItemStack) (Object) this, i, random)) continue;
                ++j;
            }
            if ((amount -= j) <= 0) {
                return false;
            }
        }
        if (player != null && amount != 0) {
            Criteria.ITEM_DURABILITY_CHANGED.trigger(player, (ItemStack) (Object) this, this.getDamage() + amount);
        }
        i = this.getDamage() + amount;
        this.setDamage(i);
        return i >= this.getMaxDamage();
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        if (this.getItem() instanceof BasicWeaponItem && !(ItemUtils.isUsable((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
        if (this.getItem() instanceof BasicShieldItem && !(ItemUtils.isUsable((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
    }
}
