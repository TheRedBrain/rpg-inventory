package com.github.theredbrain.betteradventuremode.network.packet;
//
//import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
//import com.github.theredbrain.betteradventuremode.block.entity.TriggeredMessageBlockEntity;
//import net.fabricmc.fabric.api.networking.v1.FabricPacket;
//import net.fabricmc.fabric.api.networking.v1.PacketType;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3i;
//
//public class UpdateTriggeredMessageBlockPacket implements FabricPacket {
//    public static final PacketType<UpdateTriggeredMessageBlockPacket> TYPE = PacketType.create(
//            BetterAdventureMode.identifier("update_triggered_message_block"),
//            UpdateTriggeredMessageBlockPacket::new
//    );
//    public final BlockPos triggeredMessageBlockPosition;
//    public final boolean showMessageArea;
//    public final Vec3i messageAreaDimensions;
//    public final BlockPos messageAreaPositionOffset;
//    public final String message;
//    public final boolean overlay;
//    public final TriggeredMessageBlockEntity.TriggerMode triggerMode;
//    public UpdateTriggeredMessageBlockPacket(BlockPos triggeredMessageBlockPosition, boolean showMessageArea, Vec3i messageAreaDimensions, BlockPos messageAreaPositionOffset, String message, boolean overlay, String triggerMode) {
//        this.triggeredMessageBlockPosition = triggeredMessageBlockPosition;
//        this.showMessageArea = showMessageArea;
//        this.messageAreaDimensions = messageAreaDimensions;
//        this.messageAreaPositionOffset = messageAreaPositionOffset;
//        this.message = message;
//        this.overlay = overlay;
//        this.triggerMode = TriggeredMessageBlockEntity.TriggerMode.byName(triggerMode).orElseGet(() -> TriggeredMessageBlockEntity.TriggerMode.ONCE);
//    }
//    public UpdateTriggeredMessageBlockPacket(PacketByteBuf buf) {
//        this(
//                buf.readBlockPos(),
//                buf.readBoolean(),
//                new Vec3i(
//                        buf.readInt(),
//                        buf.readInt(),
//                        buf.readInt()
//                ),
//                buf.readBlockPos(),
//                buf.readString(),
//                buf.readBoolean(),
//                buf.readString()
//        );
//    }
//    @Override
//    public PacketType<?> getType() {
//        return TYPE;
//    }
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeBlockPos(this.triggeredMessageBlockPosition);
//        buf.writeBoolean(this.showMessageArea);
//        buf.writeInt(this.messageAreaDimensions.getX());
//        buf.writeInt(this.messageAreaDimensions.getY());
//        buf.writeInt(this.messageAreaDimensions.getZ());
//        buf.writeBlockPos(this.messageAreaPositionOffset);
//        buf.writeString(this.message);
//        buf.writeBoolean(this.overlay);
//        buf.writeString(this.triggerMode.asString());
//    }
//}
