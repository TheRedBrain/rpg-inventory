package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class DialogueGiveItemsFromLootTablePacket implements FabricPacket {
    public static final PacketType<DialogueGiveItemsFromLootTablePacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("dialogue_give_items_from_loot_table"),
            DialogueGiveItemsFromLootTablePacket::new
    );

    public final UUID playerUuid;
    public final Identifier lootTableIdentifier;

    public DialogueGiveItemsFromLootTablePacket(UUID playerUuid, Identifier lootTableIdentifier) {
        this.playerUuid = playerUuid;
        this.lootTableIdentifier = lootTableIdentifier;
    }

    public DialogueGiveItemsFromLootTablePacket(PacketByteBuf buf) {
        this(buf.readUuid(), buf.readIdentifier());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeUuid(this.playerUuid);
        buf.writeIdentifier(this.lootTableIdentifier);
    }

}
