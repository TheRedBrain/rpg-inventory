package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class UpdateMannequinSettingsPacket implements FabricPacket {
    public static final PacketType<UpdateMannequinSettingsPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_mannequin_settings"),
            UpdateMannequinSettingsPacket::new
    );

    public final int mannequinEntityId;
    public final boolean isLeftHanded;
    public final boolean isPushable;
    public final boolean isSneaking;
    public final boolean isUsingItem;
    public final boolean isBaby;
    public final boolean hasVisualFire;
    public final boolean isAffectedByPistons;
    public final boolean hasNoGravity;
    public final boolean isCustomNameVisible;
    public final String customName;
    public final String textureIdentifierString;
    public final String sheathedWeaponMode;
    public final float entityYaw;
//    public final Vec3d entityPos;
    public final Vector3f entityPos;

    public UpdateMannequinSettingsPacket(
            int mannequinEntityId,
            boolean isLeftHanded,
            boolean isPushable,
            boolean isSneaking,
            boolean isUsingItem,
            boolean isBaby,
            boolean hasVisualFire,
            boolean isAffectedByPistons,
            boolean hasNoGravity,
            boolean isCustomNameVisible,
            String customName,
            String textureIdentifierString,
            String sheathedWeaponMode,
            float entityYaw,
//            Vec3d entityPos
            Vector3f entityPos
    ) {
        this.mannequinEntityId = mannequinEntityId;
        this.isLeftHanded = isLeftHanded;
        this.isPushable = isPushable;
        this.isSneaking = isSneaking;
        this.isUsingItem = isUsingItem;
        this.isBaby = isBaby;
        this.hasVisualFire = hasVisualFire;
        this.isAffectedByPistons = isAffectedByPistons;
        this.hasNoGravity = hasNoGravity;
        this.isCustomNameVisible = isCustomNameVisible;
        this.customName = customName;
        this.textureIdentifierString = textureIdentifierString;
        this.sheathedWeaponMode = sheathedWeaponMode;
        this.entityYaw = entityYaw;
        this.entityPos = entityPos;
    }

    public UpdateMannequinSettingsPacket(PacketByteBuf buf) {
        this(
                buf.readInt(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readFloat(),
//                buf.readVec3d()
                buf.readVector3f()
        );
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.mannequinEntityId);
        buf.writeBoolean(this.isLeftHanded);
        buf.writeBoolean(this.isPushable);
        buf.writeBoolean(this.isSneaking);
        buf.writeBoolean(this.isUsingItem);
        buf.writeBoolean(this.isBaby);
        buf.writeBoolean(this.hasVisualFire);
        buf.writeBoolean(this.isAffectedByPistons);
        buf.writeBoolean(this.hasNoGravity);
        buf.writeBoolean(this.isCustomNameVisible);
        buf.writeString(this.customName);
        buf.writeString(this.textureIdentifierString);
        buf.writeString(this.sheathedWeaponMode);
        buf.writeFloat(this.entityYaw);
        buf.writeVector3f(this.entityPos);
    }

}
