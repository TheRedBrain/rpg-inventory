package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class AddStatusEffectPacket implements FabricPacket {
    public static final PacketType<AddStatusEffectPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("add_status_effect"),
            AddStatusEffectPacket::new
    );

    public final Identifier effectId;
    public final int duration;
    public final int amplifier;
    public final boolean ambient;
    public final boolean showParticles;
    public final boolean showIcon;
    public final boolean toggle;

    public AddStatusEffectPacket(Identifier effectId, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, boolean toggle) {
        this.effectId = effectId;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.showParticles = showParticles;
        this.showIcon = showIcon;
        this.toggle = toggle;
    }

    public AddStatusEffectPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(this.effectId);
        buf.writeInt(this.duration);
        buf.writeInt(this.amplifier);
        buf.writeBoolean(this.ambient);
        buf.writeBoolean(this.showParticles);
        buf.writeBoolean(this.showIcon);
        buf.writeBoolean(this.toggle);
    }
}
