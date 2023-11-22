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

public class InteractiveAdventureFullToAirBlock extends AbstractInteractiveAdventureBlock {
    // fake walls
    public InteractiveAdventureFullToAirBlock(@Nullable Block cloakedBlock, @Nullable Identifier cloakAdvancementIdentifier, @Nullable TagKey<Item> requiredTools, boolean requiresTools, int respawnModifier, Settings settings) {
        super(cloakedBlock, cloakAdvancementIdentifier, requiredTools, requiresTools, respawnModifier, settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.isOf(this) && !state.get(AbstractInteractiveAdventureBlock.INTACT)) {
            return VoxelShapes.empty();
        } else {
            return VoxelShapes.fullCube();
        }
    }
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        if (state.isOf(this) && !state.get(AbstractInteractiveAdventureBlock.INTACT)) {
            return world.getMaxLightLevel();
        } else {
            return state.isTransparent(world, pos) ? 0 : 1;
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
}
