package com.github.theredbrain.rpginventory.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.scores.PlayerTeam;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Optional;

public class ScriptBlocksCompat {
	public static LevelData.RespawnData getPVPRespawnPosition(PlayerTeam team, ServerPlayer serverPlayerEntity, boolean endOfBattle) {
//		return ScriptBlocks.getPVPRespawnPosition(team, serverPlayerEntity, endOfBattle);
		ServerPlayer.RespawnConfig respawnConfig = serverPlayerEntity.getRespawnConfig();
		if (respawnConfig != null) {
			return respawnConfig.respawnData();
		}
		return null;
	}

	public static void setCurrentPVPControllerBlockPosition(Player playerEntity, Optional<BlockPos> currentPVPControllerBlockPosition) {
//		ScriptBlocks.setCurrentPVPControllerBlockPosition(playerEntity, currentPVPControllerBlockPosition);
	}

	public static void addPlayerAndTeamToPVPControllerBlock(PlayerTeam team, Player playerEntity, ServerLevel serverWorld, Optional<BlockPos> currentPVPControllerBlockPosition) {
//		ScriptBlocks.addPlayerAndTeamToPVPControllerBlock(team, playerEntity, serverWorld, currentPVPControllerBlockPosition);
	}

	public static void removePlayerFromPVPControllerBlock(Player playerEntity, ServerLevel serverWorld, Optional<BlockPos> currentPVPControllerBlockPosition) {
//		ScriptBlocks.removePlayerFromPVPControllerBlock(playerEntity, serverWorld, currentPVPControllerBlockPosition);
	}
}
