package com.github.theredbrain.betteradventuremode.mixin.client.network;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.client.DuckMinecraftClientMixin;
import com.github.theredbrain.betteradventuremode.client.input.DuckKeyboardInputMixin;
import com.github.theredbrain.betteradventuremode.client.network.message.DuckMessageHandlerMixin;
import com.github.theredbrain.betteradventuremode.data.Dialogue;
import com.github.theredbrain.betteradventuremode.block.entity.*;
import com.github.theredbrain.betteradventuremode.client.gui.screen.ingame.*;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.ComponentsRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import com.mojang.authlib.GameProfile;
import net.bettercombat.BetterCombat;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.bettercombat.config.ServerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientPlayerEntity.class,priority = 950)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements DuckPlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;
    @Shadow public Input input;

    @Shadow public boolean shouldSlowDown() {
        throw new AssertionError();
    }

    @Shadow public abstract boolean isUsingItem();

    @Shadow public abstract float getPitch(float tickDelta);

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V",
                    shift = At.Shift.AFTER
            )
    )
    public void tick(CallbackInfo ci) {
        if (BetterAdventureMode.serverConfig.allow_360_degree_third_person && !this.isCreative()) {
            StatusEffect first_person_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(BetterAdventureMode.serverConfig.first_person_status_effect));
            if ((first_person_status_effect != null && this.hasStatusEffect(first_person_status_effect))) {
                this.client.options.setPerspective(Perspective.FIRST_PERSON);
            } else if (BetterAdventureMode.serverConfig.disable_first_person) {
                this.client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
            StatusEffect allow_pitch_changes_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(BetterAdventureMode.serverConfig.allow_pitch_changes_status_effect));
            if (BetterAdventureMode.serverConfig.allow_360_degree_third_person && BetterAdventureMode.serverConfig.disable_player_pitch_changes && this.getPitch() != BetterAdventureMode.serverConfig.default_player_pitch && !(allow_pitch_changes_status_effect != null && this.hasStatusEffect(allow_pitch_changes_status_effect)) && !(this.isUsingItem() && this.getActiveItem().isIn(Tags.ENABLES_CHANGING_PITCH_ON_USING))) {
                this.setPitch(Float.min(Float.max(BetterAdventureMode.serverConfig.default_player_pitch, -90.0F), 90.0F));
            }
        }
    }

    @ModifyArgs(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setPitch(F)V", ordinal = 0))
    public void modify_args_betterAdventureMode$init(Args args) {
        args.set(0, Float.min(Float.max(BetterAdventureMode.serverConfig.default_player_pitch, -90.0F), 90.0F));
    }

    @Inject(
            method = "tickMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/input/Input;tick(ZF)V",
                    shift = At.Shift.AFTER
            )
    )
    public void betterAdventureMode$tickMovement(CallbackInfo ci) {
        boolean betterThirdPersonEnabled = BetterAdventureModeClient.clientConfig.enable_360_degree_third_person && BetterAdventureMode.serverConfig.allow_360_degree_third_person && this.client.options.getPerspective() != Perspective.FIRST_PERSON && !this.isCreative();
        boolean isUsingRotationLockingItem = this.isUsingItem() && (this.activeItemStack.isIn(Tags.ROTATE_PLAYER_ON_USING) || BetterAdventureModeClient.clientConfig.using_items_towards_camera_direction);
        boolean isWeaponSwingInProgress = ((MinecraftClient_BetterCombat) this.client).isWeaponSwingInProgress();
        boolean arePlayerYawChangesDisabledByAttacking = BetterAdventureMode.serverConfig.disable_player_yaw_changes_during_attacks && isWeaponSwingInProgress;
        ServerConfig config = BetterCombat.config;
        double multiplier = Math.min(Math.max((double)config.movement_speed_while_attacking, 0.0), 1.0);
        boolean isMovementPenaltyIgnored = this.getStackInHand(((DuckMinecraftClientMixin)this.client).betteradventuremode$getCurrentAttackHand()).isIn(Tags.IGNORES_ATTACK_MOVEMENT_PENALTY) && isWeaponSwingInProgress;
        if (betterThirdPersonEnabled && !arePlayerYawChangesDisabledByAttacking) {
            if (isUsingRotationLockingItem) {
                this.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw);
            }
            if (this.input.pressingForward) {
                this.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw);
            } else if (this.input.pressingBack && !isUsingRotationLockingItem) {
                this.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw + 180);
                this.input.pressingBack = false;
                this.input.pressingForward = true;
                boolean bl = this.input.pressingLeft;
                this.input.pressingLeft = this.input.pressingRight;
                this.input.pressingRight = bl;
            } else if (this.input.pressingLeft && !isUsingRotationLockingItem) {
                this.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw - 90);
                this.input.pressingLeft = false;
                this.input.pressingForward = true;
            } else if (this.input.pressingRight && !isUsingRotationLockingItem) {
                this.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw + 90);
                this.input.pressingRight = false;
                this.input.pressingForward = true;
            }
            ((DuckKeyboardInputMixin)this.input).betterAdventureMode$updateMovement(this.shouldSlowDown(), MathHelper.clamp(0.3F + EnchantmentHelper.getSwiftSneakSpeedBoost(this), 0.0F, 1.0F));
        }
        if (multiplier != 1.0 && !isMovementPenaltyIgnored) {
            ClientPlayerEntity clientPlayer = (ClientPlayerEntity) (Object) this;
            if (!clientPlayer.hasVehicle() || config.movement_speed_effected_while_mounting) {
                MinecraftClient_BetterCombat client = (MinecraftClient_BetterCombat)MinecraftClient.getInstance();
                float swingProgress = client.getSwingProgress();
                if ((double)swingProgress < 1/*0.98*/) {
                    if (config.movement_speed_applied_smoothly) {
                        double p2 = 0.0;
                        if ((double)swingProgress <= 0.5) {
                            p2 = net.bettercombat.utils.MathHelper.easeOutCubic((double)(swingProgress * 2.0F));
                        } else {
                            p2 = net.bettercombat.utils.MathHelper.easeOutCubic(1.0 - ((double)swingProgress - 0.5) * 2.0);
                        }

                        multiplier = (double)((float)(1.0 - (1.0 - multiplier) * p2));
                    }

                    Input var10000 = clientPlayer.input;
                    var10000.movementForward = (float)((double)var10000.movementForward * multiplier);
                    var10000 = clientPlayer.input;
                    var10000.movementSideways = (float)((double)var10000.movementSideways * multiplier);
                }

            }
        }
    }
    
    /**
     * @author TheRedBrain
     * @reason convenience, might redo later
     */
    @Overwrite
    private boolean canSprint() {
        return this.hasVehicle() || ((float)this.getHungerManager().getFoodLevel() > 6.0F && !BetterAdventureMode.serverConfig.disable_vanilla_food_system) || ((DuckLivingEntityMixin)this).betteradventuremode$getStamina() > 0 || this.getAbilities().allowFlying;
    }

    /**
     * @author TheRedBrain
     * @reason convenience, might redo later
     */
    @Overwrite
    public void applyDamage(DamageSource source, float amount) {
        super.applyDamage(source, amount);
    }

    @Override
    public void betteradventuremode$sendAnnouncement(Text announcement) {
        ((DuckMessageHandlerMixin)this.client.getMessageHandler()).betteradventuremode$onAnnouncement(announcement);
    }

    @Override
    public void betteradventuremode$openHousingScreen() {
        HousingBlockEntity housingBlockEntity = null;
        if (this.client.getServer() != null && this.client.world != null && this.client.world.getBlockEntity(ComponentsRegistry.CURRENT_HOUSING_BLOCK_POS.get(this).getValue()) instanceof HousingBlockEntity housingBlockEntity1) {
            housingBlockEntity = housingBlockEntity1;
        }
        int currentPermissionLevel;
        if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_OWNER_EFFECT) && housingBlockEntity != null) {
            currentPermissionLevel = 0;
        } else if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_CO_OWNER_EFFECT) && housingBlockEntity != null) {
            currentPermissionLevel = 1;
        } else if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_TRUSTED_EFFECT) && housingBlockEntity != null) {
            currentPermissionLevel = 2;
        } else if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_GUEST_EFFECT) && housingBlockEntity != null) {
            currentPermissionLevel = 3;
        } else if (this.hasStatusEffect(StatusEffectsRegistry.HOUSING_STRANGER_EFFECT) && housingBlockEntity != null) {
            currentPermissionLevel = 4;
        } else if (this.isCreative()) {
            currentPermissionLevel = 5;
        } else {
            this.sendMessage(Text.translatable("gui.housing_screen.not_in_a_house"), true);
            return;
        }
        this.client.setScreen(new HousingScreen(housingBlockEntity, currentPermissionLevel, this.isCreative()));
    }

    @Override
    public void betteradventuremode$openJigsawPlacerBlockScreen(JigsawPlacerBlockEntity jigsawPlacerBlock) {
        this.client.setScreen(new JigsawPlacerBlockScreen(jigsawPlacerBlock));
    }

    @Override
    public void betteradventuremode$openRedstoneTriggerBlockScreen(RedstoneTriggerBlockEntity redstoneTriggerBlock) {
        this.client.setScreen(new RedstoneTriggerBlockScreen(redstoneTriggerBlock));
    }

    @Override
    public void betteradventuremode$openRelayTriggerBlockScreen(RelayTriggerBlockEntity relayTriggerBlock) {
        this.client.setScreen(new RelayTriggerBlockScreen(relayTriggerBlock));
    }

    @Override
    public void betteradventuremode$openTriggeredCounterBlockScreen(TriggeredCounterBlockEntity triggeredCounterBlock) {
        this.client.setScreen(new TriggeredCounterBlockScreen(triggeredCounterBlock));
    }

    @Override
    public void betteradventuremode$openResetTriggerBlockScreen(ResetTriggerBlockEntity resetTriggerBlock) {
        this.client.setScreen(new ResetTriggerBlockScreen(resetTriggerBlock));
    }

    @Override
    public void betteradventuremode$openDelayTriggerBlockScreen(DelayTriggerBlockEntity delayTriggerBlock) {
        this.client.setScreen(new DelayTriggerBlockScreen(delayTriggerBlock));
    }

    @Override
    public void betteradventuremode$openUseRelayBlockScreen(UseRelayBlockEntity useRelayBlock) {
        this.client.setScreen(new UseRelayBlockScreen(useRelayBlock));
    }

    @Override
    public void betteradventuremode$openTriggeredSpawnerBlockScreen(TriggeredSpawnerBlockEntity triggeredSpawnerBlock) {
        this.client.setScreen(new TriggeredSpawnerBlockScreen(triggeredSpawnerBlock));
    }

    @Override
    public void betteradventuremode$openMimicBlockScreen(MimicBlockEntity mimicBlock) {
        this.client.setScreen(new MimicBlockScreen(mimicBlock));
    }

    @Override
    public void betteradventuremode$openLocationControlBlockScreen(LocationControlBlockEntity locationControlBlock) {
        this.client.setScreen(new LocationControlBlockScreen(locationControlBlock));
    }

    @Override
    public void betteradventuremode$openDialogueScreen(DialogueBlockEntity dialogueBlockEntity, @Nullable Dialogue dialogue) {
        this.client.setScreen(new DialogueBlockScreen(dialogueBlockEntity, dialogue, this.isCreativeLevelTwoOp()));
    }

    @Override
    public void betteradventuremode$openEntranceDelegationBlockScreen(EntranceDelegationBlockEntity entranceDelegationBlockEntity) {
        this.client.setScreen(new EntranceDelegationBlockScreen(entranceDelegationBlockEntity));
    }

    @Override
    public void betteradventuremode$openAreaBlockScreen(AreaBlockEntity areaBlockEntity) {
        this.client.setScreen(new AreaBlockScreen(areaBlockEntity));
    }

    @Override
    public void betteradventuremode$openTriggeredAdvancementCheckerBlockScreen(TriggeredAdvancementCheckerBlockEntity triggeredAdvancementCheckerBlock) {
        this.client.setScreen(new TriggeredAdvancementCheckerBlockScreen(triggeredAdvancementCheckerBlock));
    }
}
