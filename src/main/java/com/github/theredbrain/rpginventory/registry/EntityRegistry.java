package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EntityRegistry {

	public static final BlockEntityType<MannequinBlockEntity> MANNEQUIN_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
			RPGInventory.identifier("mannequin"),
			FabricBlockEntityTypeBuilder.create(MannequinBlockEntity::new, BlockRegistry.MANNEQUIN).build());

	public static void init() {
	}
}
