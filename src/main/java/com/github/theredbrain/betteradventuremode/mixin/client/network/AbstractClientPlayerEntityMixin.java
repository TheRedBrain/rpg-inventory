package com.github.theredbrain.betteradventuremode.mixin.client.network;

import com.github.theredbrain.betteradventuremode.entity.IRenderEquippedTrinkets;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity implements DuckPlayerEntityMixin, IRenderEquippedTrinkets {

    @Shadow
    protected PlayerListEntry getPlayerListEntry() {
        throw new AssertionError();
    }

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Override
    public boolean betteradventuremode$isAdventure() {
        PlayerListEntry playerListEntry = this.getPlayerListEntry();
        return playerListEntry != null && playerListEntry.getGameMode() == GameMode.ADVENTURE;
    }

    public boolean isMainHandItemSheathed() {
        return this.hasStatusEffect(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT);
    }

    public boolean isOffHandItemSheathed() {
        return this.hasStatusEffect(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT) || this.hasStatusEffect(StatusEffectsRegistry.TWO_HANDED_EFFECT);
    }

    public ItemStack getMainHandItemStack() {
        return ((DuckPlayerInventoryMixin) (this.getInventory())).betteradventuremode$getMainHand();
    }

    public ItemStack getOffHandItemStack() {
        return ((DuckPlayerInventoryMixin) (this.getInventory())).betteradventuremode$getOffHand();
    }
}
