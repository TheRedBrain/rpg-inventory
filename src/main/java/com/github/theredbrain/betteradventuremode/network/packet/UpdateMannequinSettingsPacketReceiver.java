package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class UpdateMannequinSettingsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateMannequinSettingsPacket> {
    @Override
    public void receive(UpdateMannequinSettingsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        int mannequinEntityId = packet.mannequinEntityId;

        boolean isLeftHanded = packet.isLeftHanded;
        boolean isPushable = packet.isPushable;
        boolean isSneaking = packet.isSneaking;
        boolean isUsingItem = packet.isUsingItem;
        boolean isBaby = packet.isBaby;
        boolean hasVisualFire = packet.hasVisualFire;
        boolean isAffectedByPistons = packet.isAffectedByPistons;
        boolean hasNoGravity = packet.hasNoGravity;
        boolean isCustomNameVisible = packet.isCustomNameVisible;
        String customName = packet.customName;
        String textureIdentifierString = packet.textureIdentifierString;
        String sheathedWeaponMode = packet.sheathedWeaponMode;
        float entityYaw = packet.entityYaw;
        Vec3d entityPos = packet.entityPos;

        World world = player.getWorld();

        Entity entity = world.getEntityById(mannequinEntityId);

        if (entity instanceof MannequinEntity mannequinEntity) {
            mannequinEntity.setIsLeftHanded(isLeftHanded);
            mannequinEntity.setIsPushable(isPushable);
            mannequinEntity.setIsSneaking(isSneaking);
            mannequinEntity.setIsUsingItem(isUsingItem);
            mannequinEntity.setIsBaby(isBaby);
            mannequinEntity.setHasVisualFire(hasVisualFire);
            mannequinEntity.setIsAffectedByPistons(isAffectedByPistons);
            mannequinEntity.setNoGravity(hasNoGravity);
            mannequinEntity.setCustomNameVisible(isCustomNameVisible);
            mannequinEntity.setCustomNameString(customName);
            mannequinEntity.setTextureIdentifierString(textureIdentifierString);
            mannequinEntity.setSheathedWeaponMode(MannequinEntity.SheathedWeaponMode.byName(sheathedWeaponMode).orElse(MannequinEntity.SheathedWeaponMode.NONE));
            mannequinEntity.bodyYaw = entityYaw;
            mannequinEntity.headYaw = entityYaw;
            mannequinEntity.refreshPositionAndAngles(entityPos.getX(), entityPos.getY(), entityPos.getZ(), entityYaw, 0.0f);
        }
    }
}
