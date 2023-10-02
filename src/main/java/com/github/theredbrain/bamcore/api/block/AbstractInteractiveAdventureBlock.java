package com.github.theredbrain.bamcore.api.block;

import com.github.theredbrain.bamcore.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractInteractiveAdventureBlock extends Block {
    public static final BooleanProperty INTACT;
    private final TagKey<Item> requiredTools;
    private final boolean requiresTools;
    private final int respawnModifier;

    public AbstractInteractiveAdventureBlock(@Nullable TagKey<Item> requiredTools, boolean requiresTools, int respawnModifier, Settings settings) {
        super(settings);
        this.requiredTools = requiredTools;
        if (this.requiredTools == null && requiresTools) {
            this.requiresTools = false;
        } else {
            this.requiresTools = requiresTools;
        }
        this.respawnModifier = Math.max(respawnModifier, 1);
        this.setDefaultState(this.getStateManager().getDefaultState().with(INTACT, true));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{INTACT});
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack tool = player.getStackInHand(Hand.MAIN_HAND);
        if (!world.isClient()) {
            if (state.isOf(this) && (!this.requiresTools || (tool.isIn(this.requiredTools) && tool.getItem() instanceof ToolItem && (ItemUtils.isUsable(tool) && hand == Hand.MAIN_HAND) || (tool.isIn(this.requiredTools) && !(tool.getItem() instanceof ToolItem) && hand == Hand.MAIN_HAND)))) {

                if (state.get(AbstractInteractiveAdventureBlock.INTACT)) {
                    // TODO drop lootTable on interaction
                    this.getLootTableId();
                    List<ItemStack> droppedStacks = Block.getDroppedStacks(state, (ServerWorld) world, pos, null, player, tool);

                    for (ItemStack itemStack : droppedStacks) {
                        Block.dropStacks(state, world, pos, null, player, itemStack);
                    }
//                this.dropStacks(state, (ServerWorld) world, hit.getBlockPos(), player, player.getStackInHand(hand));
//                List<ItemStack> droppedStacks = LootTable.EMPTY.generateLoot(new LootContext());//builder().build().generateLoot(LootContext);//this.getLootTableId().
//                    if (this.drop != null) {
//                        if (!player.getInventory().insertStack(new ItemStack(drop))) {
//                            player.dropItem(new ItemStack(drop), false);
//                        }
////                    }
//                    }
                    // TODO sounds
                    world.setBlockState(pos, state.with(INTACT, false));
                    if (player != null && tool.getItem() instanceof ToolItem ) {
                        tool.damage(1, player, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));
                    }
                    return ActionResult.SUCCESS;
                } else {
                    // TODO send message to player that the resource is empty
                    player.sendMessage(Text.translatable("interactive_adventure_block.ressourceNodeDepleted"), false);
//                    RPGMod.LOGGER.info("Resource node depleted");
                }
            } else {
                // TODO send message to player that they need a better tool
                player.sendMessage(Text.translatable("interactive_adventure_block.betterToolRequired"), false);
//                RPGMod.LOGGER.info("Better tool required");
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        player.incrementStat(Stats.MINED.getOrCreateStat(this));
        dropStack(world, pos, this.getPickStack(world, pos, state));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.isOf(this) && !state.get(INTACT) && random.nextInt(this.respawnModifier) == 0) {

            // TODO push entities out of block bounds
            world.setBlockState(pos, state.with(INTACT, true));
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.isOf(this) && !state.get(INTACT);
    }

    static {
        INTACT = BooleanProperty.of("intact");
    }
}
