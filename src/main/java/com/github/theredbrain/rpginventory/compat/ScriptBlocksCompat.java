package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.scriptblocks.ScriptBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Optional;

public class ScriptBlocksCompat {
	public static MutablePair<RegistryKey<World>, MutablePair<BlockPos, MutablePair<Double, Double>>> getPVPRespawnPosition(ServerPlayerEntity serverPlayerEntity, boolean endOfBattle) {
		return ScriptBlocks.getPVPRespawnPosition(serverPlayerEntity, endOfBattle);
	}
	public static void setCurrentPVPControllerBlockPosition(PlayerEntity playerEntity, Optional<BlockPos> currentPVPControllerBlockPosition) {
		ScriptBlocks.setCurrentPVPControllerBlockPosition(playerEntity, currentPVPControllerBlockPosition);
	}
}
