package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;

import java.util.UUID;

public class DialogueGrantAdvancementPacket implements FabricPacket {
    public static final PacketType<DialogueGrantAdvancementPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("dialogue_grant_advancement"),
            DialogueGrantAdvancementPacket::new
    );

    public final UUID playerUuid;
    public final Identifier advancementIdentifier;
    public final String criterionName;

    public DialogueGrantAdvancementPacket(UUID playerUuid, Identifier advancementIdentifier, String criterionName) {
        this.playerUuid = playerUuid;
        this.advancementIdentifier = advancementIdentifier;
        this.criterionName = criterionName;
    }

    public DialogueGrantAdvancementPacket(PacketByteBuf buf) {
        this(buf.readUuid(), buf.readIdentifier(), buf.readString());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeUuid(this.playerUuid);
        buf.writeIdentifier(this.advancementIdentifier);
        buf.writeString(this.criterionName);
    }

}
