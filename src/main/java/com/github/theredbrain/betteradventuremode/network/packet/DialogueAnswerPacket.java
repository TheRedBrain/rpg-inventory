package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class DialogueAnswerPacket implements FabricPacket {
    public static final PacketType<DialogueAnswerPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("dialogue_answer"),
            DialogueAnswerPacket::new
    );

    public final BlockPos dialogueBlockPos;
    public final Identifier answerIdentifier;

    public DialogueAnswerPacket(BlockPos dialogueBlockPos, Identifier answerIdentifier) {
        this.dialogueBlockPos = dialogueBlockPos;
        this.answerIdentifier = answerIdentifier;
    }

    public DialogueAnswerPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readIdentifier());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.dialogueBlockPos);
        buf.writeIdentifier(this.answerIdentifier);
    }
}
