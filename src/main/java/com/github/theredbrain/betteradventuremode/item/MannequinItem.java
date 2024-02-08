package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.function.Consumer;

public class MannequinItem extends Item {
    public MannequinItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Direction direction = context.getSide();
        if (direction == Direction.DOWN) {
            return ActionResult.FAIL;
        }
        World world = context.getWorld();
        ItemPlacementContext itemPlacementContext = new ItemPlacementContext(context);
        BlockPos blockPos = itemPlacementContext.getBlockPos();
        ItemStack itemStack = context.getStack();
        Vec3d vec3d = Vec3d.ofBottomCenter(blockPos);
        EntityType<MannequinEntity> entityType = EntityRegistry.MANNEQUIN_ENTITY;
        Box box = entityType.getDimensions().getBoxAt(vec3d.getX(), vec3d.getY(), vec3d.getZ());
        if (!world.isSpaceEmpty(null, box) || !world.getOtherEntities(null, box).isEmpty()) {
            return ActionResult.FAIL;
        }
        if (world instanceof ServerWorld serverWorld) {
            Consumer consumer = EntityType.copier(serverWorld, itemStack, context.getPlayer());
            MannequinEntity mannequinEntity = entityType.create(serverWorld, itemStack.getNbt(), consumer, blockPos, SpawnReason.SPAWN_EGG, true, true);
            if (mannequinEntity == null) {
                return ActionResult.FAIL;
            }
            float f = (float) MathHelper.floor(MathHelper.wrapDegrees(context.getPlayerYaw() - 180.0f));
            mannequinEntity.bodyYaw = f;
            mannequinEntity.headYaw = f;
            mannequinEntity.refreshPositionAndAngles(mannequinEntity.getX(), mannequinEntity.getY(), mannequinEntity.getZ(), f, 0.0f);
            serverWorld.spawnEntityAndPassengers(mannequinEntity);
            world.playSound(null, mannequinEntity.getX(), mannequinEntity.getY(), mannequinEntity.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75f, 0.8f);
            mannequinEntity.emitGameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
        }
        itemStack.decrement(1);
        return ActionResult.success(world.isClient);
    }
}
