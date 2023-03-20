package com.github.theredbrain.rpgmod.network.listener;

import com.github.theredbrain.rpgmod.network.packet.c2s.play.ConsumeItemC2SPacket;
import com.github.theredbrain.rpgmod.network.packet.c2s.play.CustomPlayerActionC2SPacket;
import net.minecraft.network.listener.ServerPacketListener;

public interface CustomServerPlayPacketListener extends ServerPacketListener {
    public void onCustomPlayerAction(CustomPlayerActionC2SPacket var1);
    public void onConsumeItem(ConsumeItemC2SPacket var1);
}
