package com.github.theredbrain.betteradventuremode.mixin.client;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.client.DuckMinecraftClientMixin;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.network.packet.AttackStaminaCostPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.bettercombat.BetterCombat;
import net.bettercombat.PlatformClient;
import net.bettercombat.api.AttackHand;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.api.client.BetterCombatClientEvents;
import net.bettercombat.client.BetterCombatClient;
import net.bettercombat.client.BetterCombatKeybindings;
import net.bettercombat.client.animation.PlayerAttackAnimatable;
import net.bettercombat.client.collision.TargetFinder;
import net.bettercombat.config.ClientConfigWrapper;
import net.bettercombat.logic.AnimatedHand;
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.PlayerAttackProperties;
import net.bettercombat.logic.WeaponRegistry;
import net.bettercombat.mixin.client.MinecraftClientAccessor;
import net.bettercombat.network.Packets;
import net.bettercombat.utils.PatternMatching;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClient_BetterCombatReplacementMixin implements MinecraftClient_BetterCombat, DuckMinecraftClientMixin {
    @Shadow
    public ClientWorld world;
    @Shadow
    public @Nullable ClientPlayerEntity player;
    @Shadow
    private int itemUseCooldown;
    @Shadow
    @Final
    public TextRenderer textRenderer;
    @Shadow
    public int attackCooldown;
    @Shadow @Final public GameOptions options;
    @Unique
    private boolean isHoldingAttackInput = false;
    @Unique
    private boolean isHarvesting = false;
    @Unique
    private String textToRender = null;
    @Unique
    private int textFade = 0;
    @Unique
    private ItemStack upswingStack;
    @Unique
    private ItemStack lastAttacedWithItemStack;
    @Unique
    private int upswingTicks = 0;
    @Unique
    private int lastAttacked = 1000;
    @Unique
    private float lastSwingDuration = 0.0F;
    @Unique
    private int comboReset = 0;
    @Unique
    private List<Entity> targetsInReach = null;

    public MinecraftClient_BetterCombatReplacementMixin() {
    }

    @Unique
    private MinecraftClient thisClient() {
        return (MinecraftClient) (Object) this;
    }

    @Inject(
            method = {"<init>"},
            at = {@At("TAIL")}
    )
    private void postInit(RunArgs args, CallbackInfo ci) {
        this.setupTextRenderer();
    }

    @Inject(
            method = {"disconnect(Lnet/minecraft/client/gui/screen/Screen;)V"},
            at = {@At("TAIL")}
    )
    private void disconnect_TAIL(Screen screen, CallbackInfo ci) {
        BetterCombatClient.ENABLED = false;
    }

    @Unique
    private void setupTextRenderer() {
        HudRenderCallback.EVENT.register((context, f) -> {
            if (this.textToRender != null && !this.textToRender.isEmpty()) {
                MinecraftClient client = MinecraftClient.getInstance();
                TextRenderer textRenderer = client.inGameHud.getTextRenderer();
                int scaledWidth = client.getWindow().getScaledWidth();
                int scaledHeight = client.getWindow().getScaledHeight();
                int i = textRenderer.getWidth(this.textToRender);
                int j = (scaledWidth - i) / 2;
                int k = scaledHeight - 59 - 14;
                if (!client.interactionManager.hasStatusBars()) {
                    k += 14;
                }

                int l;
                if ((l = (int)((float)this.textFade * 256.0F / 10.0F)) > 255) {
                    l = 255;
                }

                if (l > 0) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int var10001 = j - 2;
                    int var10002 = k - 2;
                    int var10003 = j + i + 2;
                    Objects.requireNonNull(textRenderer);
                    context.fill(var10001, var10002, var10003, k + 9 + 2, client.options.getTextBackgroundColor(0));
                    context.drawTextWithShadow(textRenderer, this.textToRender, j, k, 16777215 + (l << 24));
                    RenderSystem.disableBlend();
                }
            }

            if (this.textFade <= 0) {
                this.textToRender = null;
            }

        });
    }

    @Inject(
            method = {"doAttack"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void pre_doAttack(CallbackInfoReturnable<Boolean> info) {
        if (BetterCombatClient.ENABLED) {
            MinecraftClient client = this.thisClient();
            ItemStack mainHandStack = client.player.getMainHandStack();
            WeaponAttributes attributes = WeaponRegistry.getAttributes(mainHandStack);
            if (attributes != null && attributes.attacks() != null) {
                if (this.isTargetingMineableBlock() || this.isHarvesting) {
                    this.isHarvesting = true;
                    return;
                }

                this.startUpswing(attributes);
                info.setReturnValue(false);
                info.cancel();
            }

        }
    }

//    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;setPerspective(Lnet/minecraft/client/option/Perspective;)V", ordinal = 0, shift = At.Shift.AFTER))
//    private void handleInputEvents(CallbackInfo ci) {
//        if (BetterAdventureModeClient.clientConfig.enable_360_degree_third_person && BetterAdventureMode.serverConfig.allow_360_degree_third_person && BetterAdventureMode.serverConfig.disable_first_person && this.options.getPerspective() == Perspective.FIRST_PERSON && this.player != null && !this.player.isCreative()) {
//            this.options.setPerspective(Perspective.THIRD_PERSON_BACK);
//        }
//    }

    @Inject(
            method = {"handleBlockBreaking"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void pre_handleBlockBreaking(boolean bl, CallbackInfo ci) {
        if (BetterCombatClient.ENABLED) {
            MinecraftClient client = this.thisClient();
            ItemStack mainHandStack = client.player.getMainHandStack();
            WeaponAttributes attributes = WeaponRegistry.getAttributes(mainHandStack);
            if (attributes != null && attributes.attacks() != null) {
                boolean isPressed = client.options.attackKey.isPressed();
                if (isPressed && !this.isHoldingAttackInput) {
                    if (this.isTargetingMineableBlock() || this.isHarvesting) {
                        this.isHarvesting = true;
                        return;
                    }

                    ci.cancel();
                }

                if (BetterCombatClient.config.isHoldToAttackEnabled && !BetterAdventureMode.serverConfig.disable_better_combat_hold_to_attack && isPressed) {
                    this.isHoldingAttackInput = true;
                    this.startUpswing(attributes);
                    ci.cancel();
                } else {
                    this.isHarvesting = false;
                    this.isHoldingAttackInput = false;
                }
            }

        }
    }

    @Inject(
            method = {"doItemUse"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void pre_doItemUse(CallbackInfo ci) {
        if (BetterCombatClient.ENABLED) {
            AttackHand hand = this.getCurrentHand();
            if (hand != null) {
                double upswingRate = hand.upswingRate();
                if (this.upswingTicks > 0 || (double)this.player.getAttackCooldownProgress(0.0F) < 1.0 - upswingRate) {
                    ci.cancel();
                }

            }
        }
    }

    @Unique
    private boolean isTargetingMineableBlock() {
        if (!BetterCombatClient.config.isMiningWithWeaponsEnabled) {
            return false;
        } else {
            String regex = BetterCombatClient.config.mineWithWeaponBlacklist;
            if (regex != null && !regex.isEmpty()) {
                ItemStack itemStack = this.player.getMainHandStack();
                String id = Registries.ITEM.getId(itemStack.getItem()).toString();
                if (PatternMatching.matches(id, regex)) {
                    return false;
                }
            }

            if (BetterCombatClient.config.isAttackInsteadOfMineWhenEnemiesCloseEnabled && this.hasTargetsInReach()) {
                return false;
            } else {
                MinecraftClient client = this.thisClient();
                HitResult crosshairTarget = client.crosshairTarget;
                if (crosshairTarget != null && crosshairTarget.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult)crosshairTarget;
                    BlockPos pos = blockHitResult.getBlockPos();
                    BlockState clicked = this.world.getBlockState(pos);
                    if (!this.shouldSwingThruGrass()) {
                        return true;
                    }

                    if (!clicked.getCollisionShape(this.world, pos).isEmpty() || clicked.getHardness(this.world, pos) != 0.0F) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    @Unique
    private boolean shouldSwingThruGrass() {
        if (!BetterCombatClient.config.isSwingThruGrassEnabled) {
            return false;
        } else {
            String regex = BetterCombatClient.config.swingThruGrassBlacklist;
            if (regex != null && !regex.isEmpty()) {
                ItemStack itemStack = this.player.getMainHandStack();
                String id = Registries.ITEM.getId(itemStack.getItem()).toString();
                return !PatternMatching.matches(id, regex);
            } else {
                return true;
            }
        }
    }

    @Unique
    private void startUpswing(WeaponAttributes attributes) {
        if (!this.player.isRiding()) {
            AttackHand hand = this.getCurrentHand();
            if (hand != null) {
                float upswingRate = (float)hand.upswingRate();
                if (this.upswingTicks <= 0 && this.attackCooldown <= 0 && !this.player.isUsingItem() && !((double)this.player.getAttackCooldownProgress(0.0F) < 1.0 - (double)upswingRate)) {
                    if (((DuckLivingEntityMixin)this.player).betteradventuremode$getStamina() <= 0) {
                        this.player.sendMessage(Text.translatable("hud.message.staminaTooLow"), true);
                        return;
                    }
                    this.player.stopUsingItem();
                    this.lastAttacked = 0;
                    this.upswingStack = this.player.getMainHandStack();
                    float attackCooldownTicksFloat = PlayerAttackHelper.getAttackCooldownTicksCapped(this.player);
                    int attackCooldownTicks = Math.round(attackCooldownTicksFloat);
                    this.comboReset = Math.round(attackCooldownTicksFloat * BetterCombat.config.combo_reset_rate);
                    this.upswingTicks = Math.max(Math.round(attackCooldownTicksFloat * upswingRate), 1);
                    this.lastSwingDuration = attackCooldownTicksFloat;
                    this.itemUseCooldown = attackCooldownTicks;
                    this.setMiningCooldown(attackCooldownTicks);
                    String animationName = hand.attack().animation();
                    boolean isOffHand = hand.isOffHand();
                    AnimatedHand animatedHand = AnimatedHand.from(isOffHand, attributes.isTwoHanded());
                    ((PlayerAttackAnimatable)this.player).playAttackAnimation(animationName, animatedHand, attackCooldownTicksFloat, upswingRate);
                    ClientPlayNetworking.send(Packets.AttackAnimation.ID, (new Packets.AttackAnimation(this.player.getId(), animatedHand, animationName, attackCooldownTicksFloat, upswingRate)).write());
                    ClientPlayNetworking.send(new AttackStaminaCostPacket(this.player.getStackInHand(hand.isOffHand() ? Hand.OFF_HAND : Hand.MAIN_HAND)));
                    BetterCombatClientEvents.ATTACK_START.invoke((handler) -> {
                        handler.onPlayerAttackStart(this.player, hand);
                    });
                }
            }
        }
    }

    @Unique
    private void cancelSwingIfNeeded() {
        if (this.upswingStack != null && !areItemStackEqual(this.player.getMainHandStack(), this.upswingStack)) {
            this.cancelWeaponSwing();
        }
    }

    @Unique
    private void attackFromUpswingIfNeeded() {
        if (this.upswingTicks > 0) {
            --this.upswingTicks;
            if (this.upswingTicks == 0) {
                this.performAttack();
                this.upswingStack = null;
            }
        }

    }

    @Unique
    private void resetComboIfNeeded() {
        if (this.lastAttacked > this.comboReset && this.getComboCount() > 0) {
            this.setComboCount(0);
        }

        if (!PlayerAttackHelper.shouldAttackWithOffHand(this.player, this.getComboCount()) && (this.player.getMainHandStack() == null || this.lastAttacedWithItemStack != null && !this.lastAttacedWithItemStack.getItem().equals(this.player.getMainHandStack().getItem()))) {
            this.setComboCount(0);
        }

    }

    @Unique
    private boolean shouldUpdateTargetsInReach() {
        if (!BetterCombatClient.config.isHighlightCrosshairEnabled && !BetterCombatClient.config.isAttackInsteadOfMineWhenEnemiesCloseEnabled) {
            return false;
        } else {
            return this.targetsInReach == null;
        }
    }

    @Unique
    private void updateTargetsInReach(List<Entity> targets) {
        this.targetsInReach = targets;
    }

    @Unique
    private void updateTargetsIfNeeded() {
        if (this.shouldUpdateTargetsInReach()) {
            AttackHand hand = PlayerAttackHelper.getCurrentAttack(this.player, this.getComboCount());
            WeaponAttributes attributes = WeaponRegistry.getAttributes(this.player.getMainHandStack());
            List<Entity> targets = List.of();
            if (attributes != null && attributes.attacks() != null) {
                targets = TargetFinder.findAttackTargets(this.player, this.getCursorTarget(), hand.attack(), attributes.attackRange());
            }

            this.updateTargetsInReach(targets);
        }

    }

    @Inject(
            method = {"tick"},
            at = {@At("HEAD")}
    )
    private void pre_Tick(CallbackInfo ci) {
        if (this.player != null) {
            this.targetsInReach = null;
            ++this.lastAttacked;
            this.cancelSwingIfNeeded();
            this.attackFromUpswingIfNeeded();
            this.updateTargetsIfNeeded();
            this.resetComboIfNeeded();
        }
    }

    @Inject(
            method = {"tick"},
            at = {@At("TAIL")}
    )
    private void post_Tick(CallbackInfo ci) {
        if (this.player != null) {
            if (BetterCombatKeybindings.toggleMineKeyBinding.wasPressed()) {
                BetterCombatClient.config.isMiningWithWeaponsEnabled = !BetterCombatClient.config.isMiningWithWeaponsEnabled;
                AutoConfig.getConfigHolder(ClientConfigWrapper.class).save();
                this.textToRender = I18n.translate(BetterCombatClient.config.isMiningWithWeaponsEnabled ? "hud.bettercombat.mine_with_weapons_on" : "hud.bettercombat.mine_with_weapons_off", new Object[0]);
                this.textFade = 40;
            }

            if (this.textFade > 0) {
                --this.textFade;
            }

        }
    }

    @Unique
    private void performAttack() {
        if (BetterCombatKeybindings.feintKeyBinding.isPressed()) {
            this.player.resetLastAttackedTicks();
            this.cancelWeaponSwing();
        } else {
            AttackHand hand = this.getCurrentHand();
            if (hand != null) {
                WeaponAttributes.Attack attack = hand.attack();
                double upswingRate = hand.upswingRate();
                if (!((double)this.player.getAttackCooldownProgress(0.0F) < 1.0 - upswingRate)) {
                    Entity cursorTarget = this.getCursorTarget();
                    List<Entity> targets = TargetFinder.findAttackTargets(this.player, cursorTarget, attack, hand.attributes().attackRange());
                    this.updateTargetsInReach(targets);
                    if (targets.size() == 0) {
                        PlatformClient.onEmptyLeftClick(this.player);
                    }

                    ClientPlayNetworking.send(Packets.C2S_AttackRequest.ID, (new Packets.C2S_AttackRequest(this.getComboCount(), this.player.isSneaking(), this.player.getInventory().selectedSlot, targets)).write());
                    Iterator var7 = targets.iterator();

                    while(var7.hasNext()) {
                        Entity target = (Entity)var7.next();
                        this.player.attack(target);
                    }

                    this.player.resetLastAttackedTicks();
                    BetterCombatClientEvents.ATTACK_HIT.invoke((handler) -> {
                        handler.onPlayerAttackStart(this.player, hand, targets, cursorTarget);
                    });
                    this.setComboCount(this.getComboCount() + 1);
                    if (!hand.isOffHand()) {
                        this.lastAttacedWithItemStack = hand.itemStack();
                    }

                }
            }
        }
    }

    @Unique
    private AttackHand getCurrentHand() {
        return PlayerAttackHelper.getCurrentAttack(this.player, this.getComboCount());
    }

    @Override
    public Hand betteradventuremode$getCurrentAttackHand() {
        return getCurrentHand() != null && getCurrentHand().isOffHand() ? Hand.OFF_HAND : Hand.MAIN_HAND;
    }

    @Unique
    private void setComboCount(int comboCount) {
        ((PlayerAttackProperties)this.player).setComboCount(comboCount);
    }

    @Unique
    private static boolean areItemStackEqual(ItemStack left, ItemStack right) {
        if (left == null && right == null) {
            return true;
        } else {
            return left != null && right != null ? ItemStack.areEqual(left, right) : false;
        }
    }

    @Unique
    private void setMiningCooldown(int ticks) {
        MinecraftClient client = this.thisClient();
        ((MinecraftClientAccessor)client).setAttackCooldown(ticks);
    }

    @Unique
    private void cancelWeaponSwing() {
        int downWind = (int)Math.round((double)PlayerAttackHelper.getAttackCooldownTicksCapped(this.player) * (1.0 - 0.5 * (double)BetterCombat.config.upswing_multiplier));
        ((PlayerAttackAnimatable)this.player).stopAttackAnimation((float)downWind);
        ClientPlayNetworking.send(Packets.AttackAnimation.ID, Packets.AttackAnimation.stop(this.player.getId(), downWind).write());
        this.upswingStack = null;
        this.itemUseCooldown = 0;
        this.setMiningCooldown(0);
    }

    public int getComboCount() {
        return ((PlayerAttackProperties)this.player).getComboCount();
    }

    public boolean hasTargetsInReach() {
        return this.targetsInReach != null && !this.targetsInReach.isEmpty();
    }

    public float getSwingProgress() {
        return !((float)this.lastAttacked > this.lastSwingDuration) && !(this.lastSwingDuration <= 0.0F) ? (float)this.lastAttacked / this.lastSwingDuration : 1.0F;
    }

    public int getUpswingTicks() {
        return this.upswingTicks;
    }

    public void cancelUpswing() {
        if (this.upswingTicks > 0) {
            this.cancelWeaponSwing();
        }

    }
}
