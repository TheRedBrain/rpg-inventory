package com.github.theredbrain.bamcore.block;
// TODO move to bamdimensions
//import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
//import com.github.theredbrain.bamcore.registry.EntityRegistry;
//import net.minecraft.block.*;
//import net.minecraft.block.entity.*;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.Hand;
//import net.minecraft.util.hit.BlockHitResult;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import org.jetbrains.annotations.Nullable;
//
//public class TeleporterBlock extends BlockWithEntity implements OperatorBlock {
////    public static final EnumProperty<StructureBlockMode> MODE = Properties.STRUCTURE_BLOCK_MODE;
//
//    public TeleporterBlock(AbstractBlock.Settings settings) {
//        super(settings);
////        this.setDefaultState((BlockState) ((BlockState) this.stateManager.getDefaultState()).with(MODE, StructureBlockMode.LOAD));
//    }
//
//    @Override
//    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return new TeleporterBlockBlockEntity(pos, state);
//    }
//
//    @Override
//    @Nullable
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//        return checkType(type, EntityRegistry.TELEPORTER_BLOCK_ENTITY, TeleporterBlockBlockEntity::tick);
//    }
//
//    @Override
//    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        if (world.isClient) {
//            return ActionResult.SUCCESS;
//        }
//        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
////        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // TODO stats
//        return ActionResult.CONSUME;
//    }
//
////    @Override
////    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
////        BlockEntity blockEntity = world.getBlockEntity(pos);
////        Text name = Text.literal("Teleporter");
////        TeleporterBlockBlockEntity teleporterBlockBlockEntity;
////        if (blockEntity instanceof TeleporterBlockBlockEntity) {
////            teleporterBlockBlockEntity = (TeleporterBlockBlockEntity) blockEntity;
////            name = Text.literal(teleporterBlockBlockEntity.getTeleporterName());
////        } else {
////            teleporterBlockBlockEntity = null;
////        }
////        return new ExtendedScreenHandlerFactory((syncId, inventory, player) -> new TeleporterBlockScreenHandler(syncId, inventory, teleporterBlockBlockEntity), name);
////    }
//
////    @Override
////    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
////        BlockEntity blockEntity;
////        if (world.isClient) {
////            return;
////        }
//////        if (placer != null && (blockEntity = world.getBlockEntity(pos)) instanceof TeleporterBlockBlockEntity) {
//////            ((StructureBlockBlockEntity) blockEntity).setAuthor(placer);
//////        }
////    }
//
//    @Override
//    public BlockRenderType getRenderType(BlockState state) {
//        return BlockRenderType.MODEL;
//    }
//
////    @Override
////    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
////        builder.add(MODE);
////    }
//}