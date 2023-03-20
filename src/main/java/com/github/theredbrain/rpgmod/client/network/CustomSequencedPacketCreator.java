package com.github.theredbrain.rpgmod.client.network;

import com.github.theredbrain.rpgmod.network.listener.CustomServerPlayPacketListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;

@FunctionalInterface
@Environment(value= EnvType.CLIENT)
public interface CustomSequencedPacketCreator {
    public Packet<CustomServerPlayPacketListener> predict(int var1);
}
