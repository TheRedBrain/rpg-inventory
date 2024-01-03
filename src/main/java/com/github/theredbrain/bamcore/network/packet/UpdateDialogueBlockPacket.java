package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import com.github.theredbrain.bamcore.api.util.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class UpdateDialogueBlockPacket implements FabricPacket {
    public static final PacketType<UpdateDialogueBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_dialogue_block"),
            UpdateDialogueBlockPacket::new
    );

    public final BlockPos dialogueBlockPosition;

    public final List<MutablePair<String, BlockPos>> dialogueUsedBlocksList;

    public final List<MutablePair<String, BlockPos>> dialogueTriggeredBlocksList;

    public final List<MutablePair<String, MutablePair<String, String>>> startingDialogueList;

    public UpdateDialogueBlockPacket(BlockPos dialogueBlockPosition, List<MutablePair<String, BlockPos>> dialogueUsedBlocksList, List<MutablePair<String, BlockPos>> dialogueTriggeredBlocksList, List<MutablePair<String, MutablePair<String, String>>> startingDialogueList) {
        this.dialogueBlockPosition = dialogueBlockPosition;
        this.dialogueUsedBlocksList = dialogueUsedBlocksList;
        this.dialogueTriggeredBlocksList = dialogueTriggeredBlocksList;
        this.startingDialogueList = startingDialogueList;
    }

    public UpdateDialogueBlockPacket(PacketByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readList(new PacketByteBufUtils.MutablePairStringBlockPosReader()),
                buf.readList(new PacketByteBufUtils.MutablePairStringBlockPosReader()),
                buf.readList(new PacketByteBufUtils.MutablePairStringMutablePairStringStringReader())
        );
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.dialogueBlockPosition);
        buf.writeCollection(this.dialogueUsedBlocksList, new PacketByteBufUtils.MutablePairStringBlockPosWriter());
        buf.writeCollection(this.dialogueTriggeredBlocksList, new PacketByteBufUtils.MutablePairStringBlockPosWriter());
        buf.writeCollection(this.startingDialogueList, new PacketByteBufUtils.MutablePairStringMutablePairStringStringWriter());
    }

}
