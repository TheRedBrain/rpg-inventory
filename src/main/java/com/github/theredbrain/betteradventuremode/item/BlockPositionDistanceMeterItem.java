package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockPositionDistanceMeterItem extends Item {

    public BlockPositionDistanceMeterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack itemStack = context.getStack();
        PlayerEntity playerEntity = context.getPlayer();
        BlockPos pos = context.getBlockPos();
        if (playerEntity != null) {
            NbtCompound itemStackNbt = itemStack.getOrCreateNbt();
            if (!itemStackNbt.contains("isRootMode", NbtElement.BYTE_TYPE)) {
                itemStackNbt.putBoolean("isRootMode", true);
            }
            NbtCompound nbt = new NbtCompound();
            if (playerEntity.isSneaking()) {
                boolean isRootMode = itemStackNbt.getBoolean("isRootMode");
                nbt.putInt("x", pos.getX());
                nbt.putInt("y", pos.getY());
                nbt.putInt("z", pos.getZ());
                if (isRootMode) {
                    itemStackNbt.put("root_block_pos", nbt);
                    if (playerEntity.getEntityWorld().isClient()) {
                        playerEntity.sendMessage(Text.translatable("item.betteradventuremode.block_position_distance_meter.set_root_block", pos.getX(), pos.getY(), pos.getZ()));
                    }
                } else {
                    itemStackNbt.put("offset_block_pos", nbt);
                    if (playerEntity.getEntityWorld().isClient()) {
                        playerEntity.sendMessage(Text.translatable("item.betteradventuremode.block_position_distance_meter.set_offset_block", pos.getX(), pos.getY(), pos.getZ()));
                    }
                }
            } else {
                if (itemStackNbt.contains("isRootMode", NbtElement.BYTE_TYPE)) {
                    boolean bl = itemStackNbt.getBoolean("isRootMode");
                    if (playerEntity.getEntityWorld().isClient()) {
                        playerEntity.sendMessage(bl ? Text.translatable("item.betteradventuremode.block_position_distance_meter.root_mode") : Text.translatable("item.betteradventuremode.block_position_distance_meter.offset_mode"));
                    }
                }
                int root_pos_x = 0;
                int root_pos_y = -70;
                int root_pos_z = 0;
                int offset_pos_x = 0;
                int offset_pos_y = -70;
                int offset_pos_z = 0;
                if (itemStackNbt.contains("root_block_pos", NbtElement.COMPOUND_TYPE)) {
                    NbtCompound nbtCompound = itemStackNbt.getCompound("root_block_pos");
                    root_pos_x = nbtCompound.getInt("x");
                    root_pos_y = nbtCompound.getInt("y");
                    root_pos_z = nbtCompound.getInt("z");
                }
                if (itemStackNbt.contains("offset_block_pos", NbtElement.COMPOUND_TYPE)) {
                    NbtCompound nbtCompound = itemStackNbt.getCompound("offset_block_pos");
                    offset_pos_x = nbtCompound.getInt("x");
                    offset_pos_y = nbtCompound.getInt("y");
                    offset_pos_z = nbtCompound.getInt("z");
                }
                if (root_pos_y > -64 && offset_pos_y > -64) {
                    int offset_x = offset_pos_x - root_pos_x;
                    int offset_y = offset_pos_y - root_pos_y;
                    int offset_z = offset_pos_z - root_pos_z;
                    if (playerEntity.getEntityWorld().isClient()) {
                        playerEntity.sendMessage(Text.translatable("item.betteradventuremode.block_position_distance_meter.info", offset_x, offset_y, offset_z));
                    }
                }
            }
            itemStack.setNbt(itemStackNbt);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound itemStackNbt = itemStack.getOrCreateNbt();
        if (user.isSneaking()) {
            boolean bl = true;
            if (!itemStackNbt.contains("isRootMode", NbtElement.BYTE_TYPE)) {
                BetterAdventureMode.info("!itemStackNbt.contains isRootMode");
                itemStackNbt.putBoolean("isRootMode", true);
            } else {
                BetterAdventureMode.info("itemStackNbt.contains isRootMode");
                bl = itemStackNbt.getBoolean("isRootMode");
                itemStackNbt.putBoolean("isRootMode", !bl);
            }
            itemStack.setNbt(itemStackNbt);
            if (world.isClient()) {
                user.sendMessage(bl ? Text.translatable("item.betteradventuremode.block_position_distance_meter.set_root_mode.false") : Text.translatable("item.betteradventuremode.block_position_distance_meter.set_root_mode.true"));
            }
            return TypedActionResult.consume(itemStack);
        }
        if (itemStackNbt.contains("isRootMode", NbtElement.BYTE_TYPE)) {
            boolean bl = itemStackNbt.getBoolean("isRootMode");
            if (user.getEntityWorld().isClient()) {
                user.sendMessage(bl ? Text.translatable("item.betteradventuremode.block_position_distance_meter.root_mode") : Text.translatable("item.betteradventuremode.block_position_distance_meter.offset_mode"));
            }
        }
        int root_pos_x = 0;
        int root_pos_y = -70;
        int root_pos_z = 0;
        int offset_pos_x = 0;
        int offset_pos_y = -70;
        int offset_pos_z = 0;
        if (itemStackNbt.contains("root_block_pos", NbtElement.COMPOUND_TYPE)) {
            NbtCompound nbtCompound = itemStackNbt.getCompound("root_block_pos");
            root_pos_x = nbtCompound.getInt("x");
            root_pos_y = nbtCompound.getInt("y");
            root_pos_z = nbtCompound.getInt("z");
        }
        if (itemStackNbt.contains("offset_block_pos", NbtElement.COMPOUND_TYPE)) {
            NbtCompound nbtCompound = itemStackNbt.getCompound("offset_block_pos");
            offset_pos_x = nbtCompound.getInt("x");
            offset_pos_y = nbtCompound.getInt("y");
            offset_pos_z = nbtCompound.getInt("z");
        }
        if (root_pos_y > -64 && offset_pos_y > -64) {
            int offset_x = offset_pos_x - root_pos_x;
            int offset_y = offset_pos_y - root_pos_y;
            int offset_z = offset_pos_z - root_pos_z;
            if (world.isClient()) {
                user.sendMessage(Text.translatable("item.betteradventuremode.block_position_distance_meter.info", offset_x, offset_y, offset_z));
            }
            return TypedActionResult.consume(itemStack);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound itemStackNbt = stack.getNbt();
        int root_pos_x = 0;
        int root_pos_y = -70;
        int root_pos_z = 0;
        int offset_pos_x = 0;
        int offset_pos_y = -70;
        int offset_pos_z = 0;
        int offset_x = 0;
        int offset_y = 0;
        int offset_z = 0;
        if (itemStackNbt != null) {
            if (itemStackNbt.contains("isRootMode", NbtElement.BYTE_TYPE)) {
                boolean bl = itemStackNbt.getBoolean("isRootMode");
                tooltip.add(bl ? Text.translatable("item.betteradventuremode.block_position_distance_meter.root_mode") : Text.translatable("item.betteradventuremode.block_position_distance_meter.offset_mode"));
            }
            if (itemStackNbt.contains("root_block_pos", NbtElement.COMPOUND_TYPE)) {
                NbtCompound nbtCompound = itemStackNbt.getCompound("root_block_pos");
                root_pos_x = nbtCompound.getInt("x");
                root_pos_y = nbtCompound.getInt("y");
                root_pos_z = nbtCompound.getInt("z");
            }
            if (itemStackNbt.contains("offset_block_pos", NbtElement.COMPOUND_TYPE)) {
                NbtCompound nbtCompound = itemStackNbt.getCompound("offset_block_pos");
                offset_pos_x = nbtCompound.getInt("x");
                offset_pos_y = nbtCompound.getInt("y");
                offset_pos_z = nbtCompound.getInt("z");
            }
        }
        if (root_pos_y > -64 && offset_pos_y > -64) {
            offset_x = offset_pos_x - root_pos_x;
            offset_y = offset_pos_y - root_pos_y;
            offset_z = offset_pos_z - root_pos_z;
        }

        if (root_pos_y > -64) {
            tooltip.add(Text.translatable("item.betteradventuremode.block_position_distance_meter.tooltip.root_pos", root_pos_x, root_pos_y, root_pos_z));
        }
        if (offset_pos_y > -64) {
            tooltip.add(Text.translatable("item.betteradventuremode.block_position_distance_meter.tooltip.offset_pos", offset_pos_x, offset_pos_y, offset_pos_z));
        }
        if (offset_x != 0 || offset_y != 0 || offset_z != 0) {
            tooltip.add(Text.translatable("item.betteradventuremode.block_position_distance_meter.tooltip.offset", offset_x, offset_y, offset_z));
        }
    }
}
