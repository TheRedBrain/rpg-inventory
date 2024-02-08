package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class ExportImportMannequinEquipmentPacket implements FabricPacket {
    public static final PacketType<ExportImportMannequinEquipmentPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("export_import_mannequin_equipment_trinket"),
            ExportImportMannequinEquipmentPacket::new
    );

    public final int mannequinEntityId;
    public final boolean isExport;

    public ExportImportMannequinEquipmentPacket(int mannequinEntityId, boolean isExport) {
        this.mannequinEntityId = mannequinEntityId;
        this.isExport = isExport;
    }

    public ExportImportMannequinEquipmentPacket(PacketByteBuf buf) {
        this(buf.readInt(), buf.readBoolean());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.mannequinEntityId);
        buf.writeBoolean(this.isExport);
    }

}
