package com.github.theredbrain.rpgmod.mixin.client.network;

import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgmod.screen.AdventureInventoryScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

//    @Shadow
//    private ClientWorld world;

//    /**
//     * @author TheRedBrain
//     * @reason use custom MxtAbstractPlayerScreenHandler and MxtPlayerInventory
//     */
//    @Overwrite
//    public void onUpdateSelectedSlot(UpdateSelectedSlotS2CPacket packet) {
//        NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler) (Object) this, this.client);
//        if (MxtPlayerInventory.isValidHotbarIndex(packet.getSlot())) {
//            ((DuckPlayerEntityMixin)this.client.player).getMxtPlayerInventory().selectedSlot = packet.getSlot();
//        }
//    }

//    private static ItemStack getActiveTotemOfUndying(PlayerEntity player) {
//        for (Hand hand : Hand.values()) {
//            ItemStack itemStack = player.getStackInHand(hand);
//            if (!itemStack.isOf(Items.TOTEM_OF_UNDYING)) continue;
//            return itemStack;
//        }
//        return new ItemStack(Items.TOTEM_OF_UNDYING);
//    }

//    @Override
//    public void onOpenHorseScreen(OpenHorseScreenS2CPacket packet) {
//        NetworkThreadUtils.forceMainThread(packet, this, this.client);
//        Entity entity = this.world.getEntityById(packet.getHorseId());
//        if (entity instanceof AbstractHorseEntity) {
//            ClientPlayerEntity clientPlayerEntity = this.client.player;
//            AbstractHorseEntity abstractHorseEntity = (AbstractHorseEntity)entity;
//            SimpleInventory simpleInventory = new SimpleInventory(packet.getSlotCount());
//            HorseScreenHandler horseScreenHandler = new HorseScreenHandler(packet.getSyncId(), clientPlayerEntity.getInventory(), simpleInventory, abstractHorseEntity);
//            clientPlayerEntity.currentScreenHandler = horseScreenHandler;
//            this.client.setScreen(new HorseScreen(horseScreenHandler, clientPlayerEntity.getInventory(), abstractHorseEntity));
//        }
//    }

    /**
     * @author TheRedBrain
     * @reason use AdventureInventoryScreenHandler if in adventure mode
     */
    @Overwrite
    public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet) {
        NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler) (Object) this, this.client);
        ClientPlayerEntity playerEntity = this.client.player;
        ItemStack itemStack = packet.getItemStack();
        int i = packet.getSlot();
        this.client.getTutorialManager().onSlotUpdate(itemStack);
        if (packet.getSyncId() == ScreenHandlerSlotUpdateS2CPacket.UPDATE_CURSOR_SYNC_ID) {
            if (!(this.client.currentScreen instanceof CreativeInventoryScreen)) {
                playerEntity.currentScreenHandler.setCursorStack(itemStack);
            }
        } else if (packet.getSyncId() == ScreenHandlerSlotUpdateS2CPacket.UPDATE_PLAYER_INVENTORY_SYNC_ID) {
            playerEntity.getInventory().setStack(i, itemStack);
        } else {
            boolean bl = false;
            Screen screen = this.client.currentScreen;
            if (screen instanceof CreativeInventoryScreen) {
                CreativeInventoryScreen creativeInventoryScreen = (CreativeInventoryScreen)screen;
                boolean bl2 = bl = !creativeInventoryScreen.isInventoryTabSelected();
            }

            if (packet.getSyncId() == 0 && AdventureInventoryScreenHandler.isInHotbar(i)) {
                ItemStack itemStack2;
                if (!itemStack.isEmpty() && ((itemStack2 = ((DuckPlayerEntityMixin)playerEntity).getAdventureInventoryScreenHandler().getSlot(i).getStack()).isEmpty() || itemStack2.getCount() < itemStack.getCount())) {
                    itemStack.setBobbingAnimationTime(5);
                }
                ((DuckPlayerEntityMixin)playerEntity).getAdventureInventoryScreenHandler().setStackInSlot(i, packet.getRevision(), itemStack);
            } else if (!(packet.getSyncId() != playerEntity.currentScreenHandler.syncId || packet.getSyncId() == 0 && bl)) {
                playerEntity.currentScreenHandler.setStackInSlot(i, packet.getRevision(), itemStack);
            }
        }
    }

    /**
     * @author TheRedBrain
     * @reason use AdventureInventoryScreenHandler if in adventure mode
     */
    @Overwrite
    public void onInventory(InventoryS2CPacket packet) {
        NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler) (Object) this, this.client);
        ClientPlayerEntity playerEntity = this.client.player;
        if (packet.getSyncId() == 0) {
            ((DuckPlayerEntityMixin)playerEntity).getAdventureInventoryScreenHandler().updateSlotStacks(packet.getRevision(), packet.getContents(), packet.getCursorStack());
        } else if (packet.getSyncId() == playerEntity.currentScreenHandler.syncId) {
            playerEntity.currentScreenHandler.updateSlotStacks(packet.getRevision(), packet.getContents(), packet.getCursorStack());
        }
    }

//    @Override
//    public void onEntityEquipmentUpdate(EntityEquipmentUpdateS2CPacket packet) {
//        NetworkThreadUtils.forceMainThread(packet, this, this.client);
//        Entity entity = this.world.getEntityById(packet.getId());
//        if (entity != null) {
//            packet.getEquipmentList().forEach(pair -> entity.equipStack((EquipmentSlot)((Object)((Object)pair.getFirst())), (ItemStack)pair.getSecond()));
//        }
//    }

//    @Override
//    public void onPlayerAbilities(PlayerAbilitiesS2CPacket packet) {
//        NetworkThreadUtils.forceMainThread(packet, this, this.client);
//        ClientPlayerEntity playerEntity = this.client.player;
//        playerEntity.getAbilities().flying = packet.isFlying();
//        playerEntity.getAbilities().creativeMode = packet.isCreativeMode();
//        playerEntity.getAbilities().invulnerable = packet.isInvulnerable();
//        playerEntity.getAbilities().allowFlying = packet.allowFlying();
//        playerEntity.getAbilities().setFlySpeed(packet.getFlySpeed());
//        playerEntity.getAbilities().setWalkSpeed(packet.getWalkSpeed());
//    }

//    public void mxtOnEntityEquipmentUpdate(MxtEntityEquipmentUpdateS2CPacket var1);
//
//    @Override
//    public void mxtOnEntityEquipmentUpdate(MxtEntityEquipmentUpdateS2CPacket packet) {
//        NetworkThreadUtils.forceMainThread(packet, this, this.client);
//        Entity entity = this.world.getEntityById(packet.getId());
//        if (entity != null) {
//            packet.getEquipmentList().forEach(pair -> ((DuckEntityMixin) entity).mxtEquipStack((MxtEquipmentSlot)((Object)((Object)pair.getFirst())), (ItemStack)pair.getSecond()));
//        }
//    }
//
//    public void mxtOnHealthUpdate(MxtHealthUpdateS2CPacket packet) {
//        NetworkThreadUtils.forceMainThread(packet, this, this.client);
//        ((DuckClientPlayerEntityMixin) this.client.player).mxtUpdateHealth(packet.getHealth(), packet.getHealthRegenerationTime(), packet.getHealthEffectiveRegenerationTime(), packet.getHealthRegenerationCounter());
//    }
//
//    public void mxtOnManaUpdate(MxtManaUpdateS2CPacket packet) {
//        NetworkThreadUtils.forceMainThread(packet, this, this.client);
//        ((DuckClientPlayerEntityMixin) this.client.player).mxtUpdateMana(packet.getMana(), packet.getManaRegenerationDelay(), packet.getManaRegenerationCounter());
//    }
}
