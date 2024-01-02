package com.github.theredbrain.bamcore.mixin.world;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess  {
    @Shadow @Final protected MutableWorldProperties properties;

    @Shadow @Final public Random random;
    @Unique
    private int spawnPointIndex = 0;

    /**
     * @author TheRedBrain
     * @reason TODO
     */
    @Overwrite
    public BlockPos getSpawnPos() {
        if (BetterAdventureModeCore.serverConfig.use_predefined_position_for_world_spawn) {
            List<Integer> worldSpawnXList = BetterAdventureModeCore.serverConfig.worldSpawnXList;
            List<Integer> worldSpawnYList = BetterAdventureModeCore.serverConfig.worldSpawnYList;
            List<Integer> worldSpawnZList = BetterAdventureModeCore.serverConfig.worldSpawnZList;
            int listSize = worldSpawnXList.size();
            spawnPointIndex = this.random.nextBetweenExclusive(0, listSize);
            if (spawnPointIndex < worldSpawnXList.size() && spawnPointIndex < worldSpawnYList.size() && spawnPointIndex < worldSpawnZList.size()) {
                BlockPos pos = new BlockPos(worldSpawnXList.get(spawnPointIndex), worldSpawnYList.get(spawnPointIndex), worldSpawnZList.get(spawnPointIndex));
                if (this.getWorldBorder().contains(pos)) {
                    return pos;
                }
            }
        }
        BlockPos blockPos = new BlockPos(this.properties.getSpawnX(), this.properties.getSpawnY(), this.properties.getSpawnZ());
        if (!this.getWorldBorder().contains(blockPos)) {
            blockPos = this.getTopPosition(Heightmap.Type.MOTION_BLOCKING, BlockPos.ofFloored(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
        }
        return blockPos;
    }

//    /**
//     * @author TheRedBrain
//     * @reason TODO
//     */
//    @Overwrite
//    public float getSpawnAngle() {
//        if (BetterAdventureModeCore.serverConfig.use_predefined_position_for_world_spawn) {
//            List<Double> list = BetterAdventureModeCore.serverConfig.worldSpawnAngleList;
//            if (spawnPointIndex < list.size()) {
//                BetterAdventureModeCore.info("use modified spawn angle");
//                return (float) list.get(spawnPointIndex).doubleValue();
//            }
//        }
//        BetterAdventureModeCore.info("use normal spawn angle");
//        return this.properties.getSpawnAngle();
//    }

}
