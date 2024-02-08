package com.github.theredbrain.betteradventuremode.client.render.model;

import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class MannequinEntityModel extends PlayerEntityModel<MannequinEntity> {

    public MannequinEntityModel(ModelPart root, boolean thinArms) {
        super(root, thinArms);
    }

    public void setAngles(MannequinEntity mannequinEntity, float f, float g, float h, float i, float j) {
        this.head.pitch = 0.017453292F * mannequinEntity.getHeadRotation().getPitch();
        this.head.yaw = 0.017453292F * mannequinEntity.getHeadRotation().getYaw();
        this.head.roll = 0.017453292F * mannequinEntity.getHeadRotation().getRoll();
        this.body.pitch = 0.017453292F * mannequinEntity.getBodyRotation().getPitch();
        this.body.yaw = 0.017453292F * mannequinEntity.getBodyRotation().getYaw();
        this.body.roll = 0.017453292F * mannequinEntity.getBodyRotation().getRoll();
        this.leftArm.pitch = 0.017453292F * mannequinEntity.getLeftArmRotation().getPitch();
        this.leftArm.yaw = 0.017453292F * mannequinEntity.getLeftArmRotation().getYaw();
        this.leftArm.roll = 0.017453292F * mannequinEntity.getLeftArmRotation().getRoll();
        this.rightArm.pitch = 0.017453292F * mannequinEntity.getRightArmRotation().getPitch();
        this.rightArm.yaw = 0.017453292F * mannequinEntity.getRightArmRotation().getYaw();
        this.rightArm.roll = 0.017453292F * mannequinEntity.getRightArmRotation().getRoll();
        this.leftLeg.pitch = 0.017453292F * mannequinEntity.getLeftLegRotation().getPitch();
        this.leftLeg.yaw = 0.017453292F * mannequinEntity.getLeftLegRotation().getYaw();
        this.leftLeg.roll = 0.017453292F * mannequinEntity.getLeftLegRotation().getRoll();
        this.rightLeg.pitch = 0.017453292F * mannequinEntity.getRightLegRotation().getPitch();
        this.rightLeg.yaw = 0.017453292F * mannequinEntity.getRightLegRotation().getYaw();
        this.rightLeg.roll = 0.017453292F * mannequinEntity.getRightLegRotation().getRoll();
        this.head.pivotX = mannequinEntity.getHeadTranslation().x;
        this.head.pivotY = mannequinEntity.getHeadTranslation().y;
        this.head.pivotZ = mannequinEntity.getHeadTranslation().z;
        this.body.pivotX = mannequinEntity.getBodyTranslation().x;
        this.body.pivotY = mannequinEntity.getBodyTranslation().y;
        this.body.pivotZ = mannequinEntity.getBodyTranslation().z;
        this.leftArm.pivotX = mannequinEntity.getLeftArmTranslation().x;
        this.leftArm.pivotY = mannequinEntity.getLeftArmTranslation().y;
        this.leftArm.pivotZ = mannequinEntity.getLeftArmTranslation().z;
        this.rightArm.pivotX = mannequinEntity.getRightArmTranslation().x;
        this.rightArm.pivotY = mannequinEntity.getRightArmTranslation().y;
        this.rightArm.pivotZ = mannequinEntity.getRightArmTranslation().z;
        this.leftLeg.pivotX = mannequinEntity.getLeftLegTranslation().x;
        this.leftLeg.pivotY = mannequinEntity.getLeftLegTranslation().y;
        this.leftLeg.pivotZ = mannequinEntity.getLeftLegTranslation().z;
        this.rightLeg.pivotX = mannequinEntity.getRightLegTranslation().x;
        this.rightLeg.pivotY = mannequinEntity.getRightLegTranslation().y;
        this.rightLeg.pivotZ = mannequinEntity.getRightLegTranslation().z;
        this.hat.copyTransform(this.head);
        this.jacket.copyTransform(this.body);
        this.leftSleeve.copyTransform(this.leftArm);
        this.rightSleeve.copyTransform(this.rightArm);
        this.leftPants.copyTransform(this.leftLeg);
        this.rightPants.copyTransform(this.rightLeg);
    }
}
