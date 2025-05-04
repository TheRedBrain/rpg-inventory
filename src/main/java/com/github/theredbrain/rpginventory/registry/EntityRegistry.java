package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {

	public static final BlockEntityType<MannequinBlockEntity> MANNEQUIN_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
			RPGInventory.identifier("mannequin"),
			FabricBlockEntityTypeBuilder.create(MannequinBlockEntity::new, BlockRegistry.MANNEQUIN).build());

	public static void init() {
	}
}
