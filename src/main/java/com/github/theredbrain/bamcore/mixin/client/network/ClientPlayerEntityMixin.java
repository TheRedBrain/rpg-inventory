package com.github.theredbrain.bamcore.mixin.client.network;

import com.github.theredbrain.bamcore.block.entity.*;
import com.github.theredbrain.bamcore.client.gui.screen.ingame.*;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements DuckPlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }


    /**
     * @author TheRedBrain
     * @reason convenience, might redo later
     */
    @Overwrite
    private boolean canSprint() {
        return this.hasVehicle() || this.bamcore$getStamina() > 0 || this.getAbilities().allowFlying;
    }

    @Override
    public void bamcore$openHousingScreen() {
        HousingBlockBlockEntity housingBlockBlockEntity = null;
        if (this.client.getServer() != null && this.client.world != null && this.client.world.getBlockEntity(ComponentsRegistry.CURRENT_HOUSING_BLOCK_POS.get(this).getValue()) instanceof HousingBlockBlockEntity housingBlockBlockEntity1) {
            housingBlockBlockEntity = housingBlockBlockEntity1;
        }
        int currentPermissionLevel;
        if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_OWNER_EFFECT) && housingBlockBlockEntity != null) {
            currentPermissionLevel = 0;
        } else if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_CO_OWNER_EFFECT) && housingBlockBlockEntity != null) {
            currentPermissionLevel = 1;
        } else if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_TRUSTED_EFFECT) && housingBlockBlockEntity != null) {
            currentPermissionLevel = 2;
        } else if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_GUEST_EFFECT) && housingBlockBlockEntity != null) {
            currentPermissionLevel = 3;
        } else if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_STRANGER_EFFECT) && housingBlockBlockEntity != null) {
            currentPermissionLevel = 4;
        } else if (this.isCreative()) {
            currentPermissionLevel = 5;
        } else {
            this.sendMessage(Text.translatable("gui.housing_screen.not_in_a_house"), true);
            return;
        }
        this.client.setScreen(new HousingScreen(housingBlockBlockEntity, currentPermissionLevel, this.isCreative()));
    }

    @Override
    public void bamcore$openJigsawPlacerBlockScreen(JigsawPlacerBlockBlockEntity jigsawPlacerBlock) {
        this.client.setScreen(new JigsawPlacerBlockScreen(jigsawPlacerBlock));
    }

    @Override
    public void bamcore$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockBlockEntity redstoneTriggerBlock) {
        this.client.setScreen(new RedstoneTriggerBlockScreen(redstoneTriggerBlock));
    }

    @Override
    public void bamcore$openRelayTriggerBlockScreen(RelayTriggerBlockBlockEntity relayTriggerBlock) {
        this.client.setScreen(new RelayTriggerBlockScreen(relayTriggerBlock));
    }

    @Override
    public void bamcore$openResetTriggerBlockScreen(ResetTriggerBlockEntity resetTriggerBlock) {
        this.client.setScreen(new ResetTriggerBlockScreen(resetTriggerBlock));
    }

    @Override
    public void bamcore$openDelayTriggerBlockScreen(DelayTriggerBlockBlockEntity delayTriggerBlock) {
        this.client.setScreen(new DelayTriggerBlockScreen(delayTriggerBlock));
    }

    @Override
    public void bamcore$openUseRelayBlockScreen(UseRelayBlockEntity useRelayBlock) {
        this.client.setScreen(new UseRelayBlockScreen(useRelayBlock));
    }

    @Override
    public void bamcore$openTriggeredSpawnerBlockScreen(TriggeredSpawnerBlockEntity triggeredSpawnerBlock) {
        this.client.setScreen(new TriggeredSpawnerBlockScreen(triggeredSpawnerBlock));
    }

    @Override
    public void bamcore$openMimicBlockScreen(MimicBlockEntity mimicBlock) {
        this.client.setScreen(new MimicBlockScreen(mimicBlock));
    }
}
