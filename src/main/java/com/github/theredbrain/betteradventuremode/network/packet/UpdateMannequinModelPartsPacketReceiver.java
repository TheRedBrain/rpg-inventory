package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.api.util.MathUtils;
import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class UpdateMannequinModelPartsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateMannequinModelPartsPacket> {
    @Override
    public void receive(UpdateMannequinModelPartsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        int mannequinEntityId = packet.mannequinEntityId;
        Vector3f headRotation = packet.headRotation;
        Vector3f bodyRotation = packet.bodyRotation;
        Vector3f leftArmRotation = packet.leftArmRotation;
        Vector3f rightArmRotation = packet.rightArmRotation;
        Vector3f leftLegRotation = packet.leftLegRotation;
        Vector3f rightLegRotation = packet.rightLegRotation;
        Vector3f headTranslation = packet.headTranslation;
        Vector3f bodyTranslation = packet.bodyTranslation;
        Vector3f leftArmTranslation = packet.leftArmTranslation;
        Vector3f rightArmTranslation = packet.rightArmTranslation;
        Vector3f leftLegTranslation = packet.leftLegTranslation;
        Vector3f rightLegTranslation = packet.rightLegTranslation;
        boolean isInnerHeadVisible = packet.isInnerHeadVisible;
        boolean isOuterHeadVisible = packet.isOuterHeadVisible;
        boolean isInnerBodyVisible = packet.isInnerBodyVisible;
        boolean isOuterBodyVisible = packet.isOuterBodyVisible;
        boolean isInnerLeftArmVisible = packet.isInnerLeftArmVisible;
        boolean isOuterLeftArmVisible = packet.isOuterLeftArmVisible;
        boolean isInnerRightArmVisible = packet.isInnerRightArmVisible;
        boolean isOuterRightArmVisible = packet.isOuterRightArmVisible;
        boolean isInnerLeftLegVisible = packet.isInnerLeftLegVisible;
        boolean isOuterLeftLegVisible = packet.isOuterLeftLegVisible;
        boolean isInnerRightLegVisible = packet.isOuterLeftLegVisible;
        boolean isOuterRightLegVisible = packet.isOuterRightLegVisible;

        World world = player.getWorld();

        Entity entity = world.getEntityById(mannequinEntityId);

        if (entity instanceof MannequinEntity mannequinEntity) {

            mannequinEntity.setHeadRotation(MathUtils.eulerAngleToFloatVector(headRotation));
            mannequinEntity.setBodyRotation(MathUtils.eulerAngleToFloatVector(bodyRotation));
            mannequinEntity.setLeftArmRotation(MathUtils.eulerAngleToFloatVector(leftArmRotation));
            mannequinEntity.setRightArmRotation(MathUtils.eulerAngleToFloatVector(rightArmRotation));
            mannequinEntity.setLeftLegRotation(MathUtils.eulerAngleToFloatVector(leftLegRotation));
            mannequinEntity.setRightLegRotation(MathUtils.eulerAngleToFloatVector(rightLegRotation));

            mannequinEntity.setHeadTranslation(headTranslation);
            mannequinEntity.setBodyTranslation(bodyTranslation);
            mannequinEntity.setLeftArmTranslation(leftArmTranslation);
            mannequinEntity.setRightArmTranslation(rightArmTranslation);
            mannequinEntity.setLeftLegTranslation(leftLegTranslation);
            mannequinEntity.setRightLegTranslation(rightLegTranslation);

            mannequinEntity.setModelPartVisibility(0, isInnerHeadVisible);
            mannequinEntity.setModelPartVisibility(1, isOuterHeadVisible);
            mannequinEntity.setModelPartVisibility(2, isInnerBodyVisible);
            mannequinEntity.setModelPartVisibility(3, isOuterBodyVisible);
            mannequinEntity.setModelPartVisibility(4, isInnerLeftArmVisible);
            mannequinEntity.setModelPartVisibility(5, isOuterLeftArmVisible);
            mannequinEntity.setModelPartVisibility(6, isInnerRightArmVisible);
            mannequinEntity.setModelPartVisibility(7, isOuterRightArmVisible);
            mannequinEntity.setModelPartVisibility(8, isInnerLeftLegVisible);
            mannequinEntity.setModelPartVisibility(9, isOuterLeftLegVisible);
            mannequinEntity.setModelPartVisibility(10, isInnerRightLegVisible);
            mannequinEntity.setModelPartVisibility(11, isOuterRightLegVisible);

        }
    }
}
