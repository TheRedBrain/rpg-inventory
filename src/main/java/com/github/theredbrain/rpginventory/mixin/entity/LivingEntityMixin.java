package com.github.theredbrain.rpginventory.mixin.entity;

import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import dev.emi.trinkets.api.TrinketEnums;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.event.TrinketDropCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public boolean rpginventory$hasEquipped(Predicate<ItemStack> predicate) {
		if (predicate.test(this.getEquippedStack(EquipmentSlot.MAINHAND))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.OFFHAND))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.FEET))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.LEGS))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.CHEST))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.HEAD))) {
			return true;
		}
		return false;
	}

	@Override
	public int rpginventory$getAmountEquipped(Predicate<ItemStack> predicate) {
		int i = 0;
		if (predicate.test(this.getEquippedStack(EquipmentSlot.MAINHAND))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.OFFHAND))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.FEET))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.LEGS))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.CHEST))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.HEAD))) {
			i += 1;
		}
		return i;
	}

	/**
	 * @author TheRedBrain
	 * @reason inject gamerule destroyDroppedItemsOnDeath into Trinkets drop logic
	 */
	@Overwrite
	public void dropInventory() {
		LivingEntity entity = (LivingEntity) (Object) this;

		boolean keepInv = entity.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY);

		boolean destroyDroppedItems;
		if (entity.getServer() != null && entity instanceof PlayerEntity) {
			destroyDroppedItems = entity.getServer().getGameRules().getBoolean(GameRulesRegistry.DESTROY_DROPPED_ITEMS_ON_DEATH);
		} else {
			destroyDroppedItems = false;
		}
		TrinketsApi.getTrinketComponent(entity).ifPresent(trinkets -> trinkets.forEach((ref, stack) -> {
			if (stack.isEmpty()) {
				return;
			}

			TrinketEnums.DropRule dropRule = TrinketsApi.getTrinket(stack.getItem()).getDropRule(stack, ref, entity);

			dropRule = TrinketDropCallback.EVENT.invoker().drop(dropRule, stack, ref, entity);

			TrinketInventory inventory = ref.inventory();

			if (dropRule == TrinketEnums.DropRule.DEFAULT) {
				dropRule = inventory.getSlotType().getDropRule();
			}

			if (dropRule == TrinketEnums.DropRule.DEFAULT) {
				if (keepInv && entity.getType() == EntityType.PLAYER) {
					dropRule = TrinketEnums.DropRule.KEEP;
				} else {
					if (EnchantmentHelper.hasVanishingCurse(stack) || destroyDroppedItems) {
						dropRule = TrinketEnums.DropRule.DESTROY;
					} else {
						dropRule = TrinketEnums.DropRule.DROP;
					}
				}
			}

			switch (dropRule) {
				case DROP:
					betteradventuremode$dropFromEntity(stack);
					// Fallthrough
				case DESTROY:
					inventory.setStack(ref.index(), ItemStack.EMPTY);
					break;
				default:
					break;
			}
		}));
	}

	@Unique
	private void betteradventuremode$dropFromEntity(ItemStack stack) {
		ItemEntity entity = dropStack(stack);
		// Mimic player drop behavior for only players
		if (entity != null && ((Entity) this) instanceof PlayerEntity) {
			entity.setPos(entity.getX(), this.getEyeY() - 0.3, entity.getZ());
			entity.setPickupDelay(40);
			float magnitude = this.random.nextFloat() * 0.5f;
			float angle = this.random.nextFloat() * ((float) Math.PI * 2);
			entity.setVelocity(-MathHelper.sin(angle) * magnitude, 0.2f, MathHelper.cos(angle) * magnitude);
		}
	}
}
