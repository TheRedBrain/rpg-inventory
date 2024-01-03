package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class AttackStaminaCostPacket implements FabricPacket {
    public static final PacketType<AttackStaminaCostPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("attack_stamina_cost"),
            AttackStaminaCostPacket::new
    );

    public final ItemStack itemStack;

    public AttackStaminaCostPacket(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public AttackStaminaCostPacket(PacketByteBuf buf) {
        this(buf.readItemStack());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeItemStack(this.itemStack);
    }
}
