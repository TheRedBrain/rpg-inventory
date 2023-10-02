package com.github.theredbrain.bamcore.block;

import com.github.theredbrain.bamcore.api.block.AbstractInteractiveAdventureBlock;
import com.github.theredbrain.bamcore.util.ItemUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public abstract class AbstractSetSpawnBlock extends Block {

    public static final BooleanProperty ACTIVE;
    private static final ImmutableList<Vec3i> VALID_HORIZONTAL_SPAWN_OFFSETS;
    private static final ImmutableList<Vec3i> VALID_SPAWN_OFFSETS;
    private final TagKey<Item> activationItems;
    private final Identifier activationSound;
    private final Identifier setSpawnSound;
    private final boolean consumesActivationItem;
    public AbstractSetSpawnBlock(@Nullable TagKey<Item> activationItems, @Nullable Identifier activationSound, @Nullable Identifier setSpawnSound, boolean requiresActivationItem, boolean consumesActivationItem, Settings settings) {
        super(settings);
        this.activationItems = activationItems;
        this.activationSound = activationSound;
        this.setSpawnSound = setSpawnSound;
        if (this.activationItems == null) {
            this.consumesActivationItem = false;
        } else {
            this.consumesActivationItem = consumesActivationItem;
        }
        this.setDefaultState(this.getStateManager().getDefaultState().with(ACTIVE, requiresActivationItem));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{ACTIVE});
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!state.get(ACTIVE)) {
            if (hand == Hand.MAIN_HAND && !itemStack.isIn(this.activationItems) && player.getStackInHand(Hand.OFF_HAND).isIn(this.activationItems)) {
                return ActionResult.PASS;
            } else if (itemStack.isIn(this.activationItems)) {
                BlockState blockState = (BlockState)state.with(ACTIVE, true);
                world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState));

                if (this.activationSound != null) {
                    world.playSound((PlayerEntity)null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvent.of(this.activationSound), SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                if (!player.getAbilities().creativeMode && !this.consumesActivationItem) {
                    itemStack.decrement(1);
                }

                return ActionResult.success(world.isClient);
            }
            return ActionResult.PASS;
        } else {
            if (!world.isClient) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
                if (serverPlayerEntity.getSpawnPointDimension() != world.getRegistryKey() || !pos.equals(serverPlayerEntity.getSpawnPointPosition())) {
                    serverPlayerEntity.setSpawnPoint(world.getRegistryKey(), pos, 0.0F, false, true);
                    if (this.setSpawnSound != null) {
                        world.playSound((PlayerEntity) null, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvent.of(this.setSpawnSound), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.CONSUME;
        }
    }

    public static Optional<Vec3d> findRespawnPosition(EntityType<?> entity, CollisionView world, BlockPos pos) {
        Optional<Vec3d> optional = findRespawnPosition(entity, world, pos, true);
        return optional.isPresent() ? optional : findRespawnPosition(entity, world, pos, false);
    }

    private static Optional<Vec3d> findRespawnPosition(EntityType<?> entity, CollisionView world, BlockPos pos, boolean ignoreInvalidPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        UnmodifiableIterator var5 = VALID_SPAWN_OFFSETS.iterator();

        Vec3d vec3d;
        do {
            if (!var5.hasNext()) {
                return Optional.empty();
            }

            Vec3i vec3i = (Vec3i)var5.next();
            mutable.set(pos).move(vec3i);
            vec3d = Dismounting.findRespawnPos(entity, world, mutable, ignoreInvalidPos);
        } while(vec3d == null);

        return Optional.of(vec3d);
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    static {
        ACTIVE = BooleanProperty.of("active");
        VALID_HORIZONTAL_SPAWN_OFFSETS = ImmutableList.of(new Vec3i(0, 0, -1), new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(1, 0, 0), new Vec3i(-1, 0, -1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, 1), new Vec3i(1, 0, 1));
        VALID_SPAWN_OFFSETS = (new ImmutableList.Builder()).addAll((Iterable)VALID_HORIZONTAL_SPAWN_OFFSETS).addAll(VALID_HORIZONTAL_SPAWN_OFFSETS.stream().map(Vec3i::down).iterator()).addAll(VALID_HORIZONTAL_SPAWN_OFFSETS.stream().map(Vec3i::up).iterator()).add((Object)(new Vec3i(0, 1, 0))).build();
    }
}
