package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class UpdateDialogueBlockPacket implements FabricPacket {
    public static final PacketType<UpdateDialogueBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_dialogue_block"),
            UpdateDialogueBlockPacket::new
    );

    public final BlockPos dialogueBlockPosition;

    public final List<MutablePair<String, BlockPos>> dialogueUsedBlocksList;

    public final List<MutablePair<String, MutablePair<String, String>>> startingDialogueList;

    public UpdateDialogueBlockPacket(BlockPos dialogueBlockPosition, List<MutablePair<String, BlockPos>> dialogueUsedBlocksList, List<MutablePair<String, MutablePair<String, String>>> startingDialogueList) {
        this.dialogueBlockPosition = dialogueBlockPosition;
        this.dialogueUsedBlocksList = dialogueUsedBlocksList;
        this.startingDialogueList = startingDialogueList;
    }

    public UpdateDialogueBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readList(new PacketByteBufUtils.PairStringBlockPosListReader()), buf.readList(new PacketByteBufUtils.PairStringPairStringStringListReader()));
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.dialogueBlockPosition);
        buf.writeCollection(this.dialogueUsedBlocksList, new PacketByteBufUtils.PairStringBlockPosListWriter());
        buf.writeCollection(this.startingDialogueList, new PacketByteBufUtils.PairStringPairStringStringListWriter());
    }

}
