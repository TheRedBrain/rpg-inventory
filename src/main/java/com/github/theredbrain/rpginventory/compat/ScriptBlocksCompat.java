package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.scriptblocks.ScriptBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Optional;

public class ScriptBlocksCompat {
	public static MutablePair<RegistryKey<World>, MutablePair<BlockPos, MutablePair<Double, Double>>> getPVPRespawnPosition(Team team, ServerPlayerEntity serverPlayerEntity, boolean endOfBattle) {
		return ScriptBlocks.getPVPRespawnPosition(team, serverPlayerEntity, endOfBattle);
	}

	public static void setCurrentPVPControllerBlockPosition(PlayerEntity playerEntity, Optional<BlockPos> currentPVPControllerBlockPosition) {
		ScriptBlocks.setCurrentPVPControllerBlockPosition(playerEntity, currentPVPControllerBlockPosition);
	}

	public static void addPlayerAndTeamToPVPControllerBlock(Team team, PlayerEntity playerEntity, ServerWorld serverWorld, Optional<BlockPos> currentPVPControllerBlockPosition) {
		ScriptBlocks.addPlayerAndTeamToPVPControllerBlock(team, playerEntity, serverWorld, currentPVPControllerBlockPosition);
	}

	public static void removePlayerFromPVPControllerBlock(PlayerEntity playerEntity, ServerWorld serverWorld, Optional<BlockPos> currentPVPControllerBlockPosition) {
		ScriptBlocks.removePlayerFromPVPControllerBlock(playerEntity, serverWorld, currentPVPControllerBlockPosition);
	}
}
