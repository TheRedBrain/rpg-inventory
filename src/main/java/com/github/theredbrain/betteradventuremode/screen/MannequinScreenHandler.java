package com.github.theredbrain.betteradventuremode.screen;

import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import com.github.theredbrain.betteradventuremode.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MannequinScreenHandler extends ScreenHandler {
    @Nullable
    private MannequinEntity mannequinEntity = null;
    private PlayerInventory playerInventory;
    private final World world;
    private final boolean canEdit;
    public final Inventory inventory = new SimpleInventory(22);

    public MannequinScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readInt(), buf.readBoolean());
    }

    public MannequinScreenHandler(int syncId, PlayerInventory playerInventory, int mannequinEntityId, boolean canEdit) {
        super(ScreenHandlerTypesRegistry.MANNEQUIN_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();
        Entity entityById = this.world.getEntityById(mannequinEntityId);
        if (entityById instanceof MannequinEntity) {
            this.mannequinEntity = (MannequinEntity) entityById;
        }
        this.canEdit = canEdit;
        int i;
        // hotbar
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 87 + i * 18, 209));
        }
        // main inventory
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + (i + 1) * 9, 87 + j * 18, 151 + i * 18));
            }
        }

        this.addSlot(new Slot(this.inventory, 0, 148, 19) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.HELMETS);
            }

        });

        this.addSlot(new Slot(this.inventory, 1, 148, 37) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.SHOULDERS);
            }

        });

        this.addSlot(new Slot(this.inventory, 2, 148, 55) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.CHEST_PLATES);
            }

        });

        this.addSlot(new Slot(this.inventory, 3, 148, 73) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.BELTS);
            }

        });

        this.addSlot(new Slot(this.inventory, 4, 148, 91) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.LEGGINGS);
            }

        });

        this.addSlot(new Slot(this.inventory, 5, 148, 109) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.MAIN_HAND_ITEMS);
            }

        });

        this.addSlot(new Slot(this.inventory, 6, 231, 19) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.NECKLACES);
            }

        });

        this.addSlot(new Slot(this.inventory, 7, 231, 37) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.RINGS);
            }

        });

        this.addSlot(new Slot(this.inventory, 8, 231, 55) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.RINGS);
            }

        });

        this.addSlot(new Slot(this.inventory, 9, 231, 73) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.GLOVES);
            }

        });

        this.addSlot(new Slot(this.inventory, 10, 231, 91) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.BOOTS);
            }

        });

        this.addSlot(new Slot(this.inventory, 11, 231, 109) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.OFF_HAND_ITEMS);
            }

        });

        this.addSlot(new Slot(this.inventory, 12, 65, 19) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.MAIN_HAND_ITEMS);
            }

        });

        this.addSlot(new Slot(this.inventory, 13, 65, 37) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.OFF_HAND_ITEMS);
            }

        });

        for (i = 0; i < 8; i++) {
            this.addSlot(new Slot(this.inventory, 14 + i, 65, 55 + i * 18) {

                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.isIn(Tags.SPELLS);
                }

            });
        }

    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.mannequinEntity != null && this.mannequinEntity.isAlive() && this.mannequinEntity.distanceTo(player) < 8.0F;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (player instanceof ServerPlayerEntity) {
            this.dropInventory(player, this.inventory);
        }
    }

    @Nullable
    public MannequinEntity getMannequinEntity() {
        return mannequinEntity;
    }
}
