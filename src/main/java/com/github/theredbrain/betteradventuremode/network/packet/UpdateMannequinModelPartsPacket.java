package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import org.joml.Vector3f;

public class UpdateMannequinModelPartsPacket implements FabricPacket {
    public static final PacketType<UpdateMannequinModelPartsPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_mannequin_model_parts"),
            UpdateMannequinModelPartsPacket::new
    );

    public final int mannequinEntityId;
    public final Vector3f headRotation;
    public final Vector3f bodyRotation;
    public final Vector3f leftArmRotation;
    public final Vector3f rightArmRotation;
    public final Vector3f leftLegRotation;
    public final Vector3f rightLegRotation;
    public final Vector3f headTranslation;
    public final Vector3f bodyTranslation;
    public final Vector3f leftArmTranslation;
    public final Vector3f rightArmTranslation;
    public final Vector3f leftLegTranslation;
    public final Vector3f rightLegTranslation;
    public final boolean isInnerHeadVisible;
    public final boolean isOuterHeadVisible;
    public final boolean isInnerBodyVisible;
    public final boolean isOuterBodyVisible;
    public final boolean isInnerLeftArmVisible;
    public final boolean isOuterLeftArmVisible;
    public final boolean isInnerRightArmVisible;
    public final boolean isOuterRightArmVisible;
    public final boolean isInnerLeftLegVisible;
    public final boolean isOuterLeftLegVisible;
    public final boolean isInnerRightLegVisible;
    public final boolean isOuterRightLegVisible;

    public UpdateMannequinModelPartsPacket(
            int mannequinEntityId,
            Vector3f headRotation,
            Vector3f bodyRotation,
            Vector3f leftArmRotation,
            Vector3f rightArmRotation,
            Vector3f leftLegRotation,
            Vector3f rightLegRotation,
            Vector3f headTranslation,
            Vector3f bodyTranslation,
            Vector3f leftArmTranslation,
            Vector3f rightArmTranslation,
            Vector3f leftLegTranslation,
            Vector3f rightLegTranslation,
            boolean isInnerHeadVisible,
            boolean isOuterHeadVisible,
            boolean isInnerBodyVisible,
            boolean isOuterBodyVisible,
            boolean isInnerLeftArmVisible,
            boolean isOuterLeftArmVisible,
            boolean isInnerRightArmVisible,
            boolean isOuterRightArmVisible,
            boolean isInnerLeftLegVisible,
            boolean isOuterLeftLegVisible,
            boolean isInnerRightLegVisible,
            boolean isOuterRightLegVisible
    ) {
        this.mannequinEntityId = mannequinEntityId;
        this.headRotation = headRotation;
        this.bodyRotation = bodyRotation;
        this.leftArmRotation = leftArmRotation;
        this.rightArmRotation = rightArmRotation;
        this.leftLegRotation = leftLegRotation;
        this.rightLegRotation = rightLegRotation;
        this.headTranslation = headTranslation;
        this.bodyTranslation = bodyTranslation;
        this.leftArmTranslation = leftArmTranslation;
        this.rightArmTranslation = rightArmTranslation;
        this.leftLegTranslation = leftLegTranslation;
        this.rightLegTranslation = rightLegTranslation;
        this.isInnerHeadVisible = isInnerHeadVisible;
        this.isOuterHeadVisible = isOuterHeadVisible;
        this.isInnerBodyVisible = isInnerBodyVisible;
        this.isOuterBodyVisible = isOuterBodyVisible;
        this.isInnerLeftArmVisible = isInnerLeftArmVisible;
        this.isOuterLeftArmVisible = isOuterLeftArmVisible;
        this.isInnerRightArmVisible = isInnerRightArmVisible;
        this.isOuterRightArmVisible = isOuterRightArmVisible;
        this.isInnerLeftLegVisible = isInnerLeftLegVisible;
        this.isOuterLeftLegVisible = isOuterLeftLegVisible;
        this.isInnerRightLegVisible = isInnerRightLegVisible;
        this.isOuterRightLegVisible = isOuterRightLegVisible;
    }

    public UpdateMannequinModelPartsPacket(PacketByteBuf buf) {
        this(buf.readInt(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readVector3f(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.mannequinEntityId);
        buf.writeVector3f(this.headRotation);
        buf.writeVector3f(this.bodyRotation);
        buf.writeVector3f(this.leftArmRotation);
        buf.writeVector3f(this.rightArmRotation);
        buf.writeVector3f(this.leftLegRotation);
        buf.writeVector3f(this.rightLegRotation);
        buf.writeVector3f(this.headTranslation);
        buf.writeVector3f(this.bodyTranslation);
        buf.writeVector3f(this.leftArmTranslation);
        buf.writeVector3f(this.rightArmTranslation);
        buf.writeVector3f(this.leftLegTranslation);
        buf.writeVector3f(this.rightLegTranslation);
        buf.writeBoolean(this.isInnerHeadVisible);
        buf.writeBoolean(this.isOuterHeadVisible);
        buf.writeBoolean(this.isInnerBodyVisible);
        buf.writeBoolean(this.isOuterBodyVisible);
        buf.writeBoolean(this.isInnerLeftArmVisible);
        buf.writeBoolean(this.isOuterLeftArmVisible);
        buf.writeBoolean(this.isInnerRightArmVisible);
        buf.writeBoolean(this.isOuterRightArmVisible);
        buf.writeBoolean(this.isInnerLeftLegVisible);
        buf.writeBoolean(this.isOuterLeftLegVisible);
        buf.writeBoolean(this.isInnerRightLegVisible);
        buf.writeBoolean(this.isOuterRightLegVisible);
    }

}
