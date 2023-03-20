package com.github.theredbrain.rpgmod.mixin.client;

import com.github.theredbrain.rpgmod.client.gui.screen.ingame.AdventureInventoryScreen;
import com.github.theredbrain.rpgmod.client.network.DuckClientPlayerInteractionManagerMixin;
import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgmod.network.packet.c2s.play.CustomPlayerActionC2SPacket;
import com.github.theredbrain.rpgmod.registry.StatusEffectsRegistry;
import com.github.theredbrain.rpgmod.util.KeyBindings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.toast.TutorialToast;
import net.minecraft.client.tutorial.TutorialManager;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Final
    private static Text SOCIAL_INTERACTIONS_NOT_AVAILABLE;

    @Shadow
    @Final
    public WorldRenderer worldRenderer;

    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Shadow
    @Final
    public InGameHud inGameHud;

    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    @Final
    public Mouse mouse;

    @Shadow
    @Final
    private TutorialManager tutorialManager;

    @Shadow
    private TutorialToast socialInteractionsToast;

    @Shadow
    public ClientPlayerInteractionManager interactionManager;

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    private int itemUseCooldown;

    @Shadow
    public Screen currentScreen;

    @Shadow
    private Overlay overlay;

    @Shadow
    @Final
    private NarratorManager narratorManager;

    @Shadow
    private void openChatScreen(String text) {
        throw new AssertionError();
    }

    @Shadow
    public void setScreen(@Nullable Screen screen) {
        throw new AssertionError();
    }

    @Shadow
    private void handleBlockBreaking(boolean breaking) {
        throw new AssertionError();
    }

    @Shadow
    private boolean doAttack() {
        throw new AssertionError();
    }

    @Shadow
    @Final
    private void doItemUse() {
        throw new AssertionError();
    }

    @Shadow
    private boolean isConnectedToServer() {
        throw new AssertionError();
    }

    @Shadow
    public ClientPlayNetworkHandler getNetworkHandler() {
        throw new AssertionError();
    }

    @Shadow
    private void doItemPick() {
        throw new AssertionError();
    }

    @Shadow
    public Entity getCameraEntity() {
        throw new AssertionError();
    }

    @Shadow @Nullable public ClientWorld world;

    /**
     * @author TheRedBrain
     * @reason check
     */
    @Overwrite
    private void handleInputEvents() {
        while (this.options.togglePerspectiveKey.wasPressed()) {
            Perspective perspective = this.options.getPerspective();
            this.options.setPerspective(this.options.getPerspective().next());
            if (perspective.isFirstPerson() != this.options.getPerspective().isFirstPerson()) {
                this.gameRenderer.onCameraEntitySet(this.options.getPerspective().isFirstPerson() ? this.getCameraEntity() : null);
            }
            this.worldRenderer.scheduleTerrainUpdate();
        }
        while (this.options.smoothCameraKey.wasPressed()) {
            this.options.smoothCameraEnabled = !this.options.smoothCameraEnabled;
        }
        for (int i = 0; i < 9; ++i) {
            boolean bl = this.options.saveToolbarActivatorKey.isPressed();
            boolean bl2 = this.options.loadToolbarActivatorKey.isPressed();
            if (!this.options.hotbarKeys[i].wasPressed()) continue;
            if (this.player.isSpectator()) {
                this.inGameHud.getSpectatorHud().selectSlot(i);
                continue;
            }
            if (this.player.isCreative() && this.currentScreen == null && (bl2 || bl)) {
                CreativeInventoryScreen.onHotbarKeyPress(((MinecraftClient) (Object) this), i, bl2, bl);
                continue;
            }
            this.player.getInventory().selectedSlot = i;
        }
        while (this.options.socialInteractionsKey.wasPressed()) {
            if (!this.isConnectedToServer()) {
                this.player.sendMessage(SOCIAL_INTERACTIONS_NOT_AVAILABLE, true);
                this.narratorManager.narrate(SOCIAL_INTERACTIONS_NOT_AVAILABLE);
                continue;
            }
            if (this.socialInteractionsToast != null) {
                this.tutorialManager.remove(this.socialInteractionsToast);
                this.socialInteractionsToast = null;
            }
            this.setScreen(new SocialInteractionsScreen());
        }
        while (this.options.inventoryKey.wasPressed()) {
            if (this.interactionManager.hasRidingInventory()) {
                this.player.openRidingInventory();
                continue;
            }
            this.tutorialManager.onInventoryOpened();
            this.setScreen(new AdventureInventoryScreen(this.player));
        }
        while (this.options.advancementsKey.wasPressed()) {
            this.setScreen(new AdvancementsScreen(this.player.networkHandler.getAdvancementHandler()));
        }
        while (this.options.swapHandsKey.wasPressed()) {
            if (this.player.isSpectator() || (((DuckPlayerEntityMixin)this.player).isAdventure() && !(this.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)))) continue;
            this.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
        }
        while (KeyBindings.swapMainHand.wasPressed()) {
            if (this.player.isSpectator()) continue;
            this.getNetworkHandler().sendPacket(new CustomPlayerActionC2SPacket(CustomPlayerActionC2SPacket.Action.SWAP_MAINHAND_ITEMS, BlockPos.ORIGIN, Direction.DOWN));
        }
        while (KeyBindings.swapOffHand.wasPressed()) {
            if (this.player.isSpectator()) continue;
            this.getNetworkHandler().sendPacket(new CustomPlayerActionC2SPacket(CustomPlayerActionC2SPacket.Action.SWAP_OFFHAND_ITEMS, BlockPos.ORIGIN, Direction.DOWN));
        }
        while (this.options.dropKey.wasPressed() && !((DuckPlayerEntityMixin)this.player).isAdventure()) {
            if (this.player.isSpectator() || !this.player.dropSelectedItem(Screen.hasControlDown())) continue;
            this.player.swingHand(Hand.MAIN_HAND);
        }
        while (this.options.chatKey.wasPressed()) {
            this.openChatScreen("");
        }
        if (this.currentScreen == null && this.overlay == null && this.options.commandKey.wasPressed()) {
            this.openChatScreen("/");
        }
        boolean bl3 = false;
        if (this.player.isUsingItem()) {
            if (!this.options.useKey.isPressed()) {
                this.interactionManager.stopUsingItem(this.player);
            }
            while (this.options.attackKey.wasPressed()) {
            }
            while (this.options.useKey.wasPressed()) {
            }
            while (this.options.pickItemKey.wasPressed()) {
            }
        } else {
            while (this.options.attackKey.wasPressed()) {
                bl3 |= this.doAttack();
            }
            while (this.options.useKey.wasPressed()) {
                this.doItemUse();
            }
            while (this.options.pickItemKey.wasPressed()) {
                this.doItemPick();
            }
        }
        if (this.options.useKey.isPressed() && this.itemUseCooldown == 0 && !this.player.isUsingItem()) {
            this.doItemUse();
        }
        if (KeyBindings.consumeItem.isPressed() && this.itemUseCooldown == 0 && !this.player.isUsingItem() && ((DuckPlayerEntityMixin)this.player).isAdventure()) {
            this.doItemConsume();
        }
        this.handleBlockBreaking(this.currentScreen == null && !bl3 && this.options.attackKey.isPressed() && this.mouse.isCursorLocked());
//        while (this.options.swapHandsKey.wasPressed()) {
//            if (this.player.isSpectator()) continue;
//            this.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
//        }
    }

    private void doItemConsume() {
        if (this.interactionManager.isBreakingBlock()) {
            return;
        }
        this.itemUseCooldown = 4;
        if (this.player.isRiding()) {
            return;
        }
        ActionResult actionResult3;
        ItemStack itemStack = this.player.getInventory().main.get(this.player.getInventory().selectedSlot);
        if (itemStack.isEmpty() || !(actionResult3 = ((DuckClientPlayerInteractionManagerMixin)this.interactionManager).consumeItem(this.player, this.world, itemStack)).isAccepted()) return; // TODO
        if (actionResult3.shouldSwingHand()) {
            this.player.swingHand(Hand.MAIN_HAND);
        }
        this.gameRenderer.firstPersonRenderer.resetEquipProgress(Hand.MAIN_HAND);
//        this.getNetworkHandler().sendPacket(new CustomPlayerActionC2SPacket(CustomPlayerActionC2SPacket.Action.CONSUME_ITEM, BlockPos.ORIGIN, Direction.DOWN));
    }
}
