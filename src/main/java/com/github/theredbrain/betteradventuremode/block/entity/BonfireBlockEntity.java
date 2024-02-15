package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.block.BonfireBlock;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class BonfireBlockEntity extends BlockEntity {
    public BonfireBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.BONFIRE_BLOCK_ENTITY, pos, state);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, BonfireBlockEntity blockEntity) {
        if (state.get(BonfireBlock.ACTIVE)) {
            Random random = world.random;
            int i;
            if (random.nextFloat() < 0.11F) {
                for(i = 0; i < random.nextInt(2) + 2; ++i) {
                    CampfireBlock.spawnSmokeParticle(world, pos, true, false);
                }
            }
        }
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, BonfireBlockEntity blockEntity) {
        if (world.getTime() % 80L == 0L && state.get(BonfireBlock.ACTIVE)) {
            if (!world.isClient) {
                Box box = (new Box(pos)).expand(10);
                List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
                Iterator var11 = list.iterator();

                PlayerEntity playerEntity;
                while(var11.hasNext()) {
                    playerEntity = (PlayerEntity)var11.next();
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.CIVILISATION_EFFECT, 100, 0, true, false, true));
                }
            }
            world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }
}
