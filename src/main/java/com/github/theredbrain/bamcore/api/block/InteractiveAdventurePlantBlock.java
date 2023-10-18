package com.github.theredbrain.bamcore.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class InteractiveAdventurePlantBlock extends AbstractInteractiveAdventureBlock {

    private static final VoxelShape BIG_PLANT;
    private static final VoxelShape SMALL_PLANT;
    private final int plant_size;
    public InteractiveAdventurePlantBlock(int plant_size, @Nullable Block cloakedBlock, @Nullable Identifier cloakAdvancementIdentifier, @Nullable TagKey<Item> requiredTools, boolean requiresTools, int respawnModifier, Settings settings) {
        super(cloakedBlock, cloakAdvancementIdentifier, requiredTools, requiresTools, respawnModifier, settings);
        this.plant_size = plant_size;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (this.plant_size == 0 && state.get(AbstractInteractiveAdventureBlock.INTACT)) {
            return SMALL_PLANT;
        } else if (this.plant_size == 0 && !state.get(AbstractInteractiveAdventureBlock.INTACT)) {
            return VoxelShapes.empty();
        } else {
            return BIG_PLANT;
        }
    }

    /*
    TODO interactable ressource blocks
        interacting "harvests" them and changes blockstate
            maybe multiple stages before harvest is complete
            different tools progress different amounts of stages
        might need interaction with a specific items/tool
        after being harvested some leaves/rubble/etc remains
        refills after some time
        use loottable?
            different possible items
                multiple qualities for ressources
            different amounts of items
        tree logs
        stone/ore veins
        plants/mushrooms
     */

    static {
        BIG_PLANT = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
        SMALL_PLANT = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
    }
}
