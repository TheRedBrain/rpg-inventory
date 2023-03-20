package com.github.theredbrain.rpgmod.registry;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.entity.ridable.CentaurMountEntity;
import com.github.theredbrain.rpgmod.entity.ridable.MountEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {
    public static final EntityType<CentaurMountEntity> CENTAUR_MOUNT = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(RPGMod.MOD_ID, "centaur_mount"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, CentaurMountEntity::new)
                    .dimensions(EntityDimensions.fixed(1/*1.3964844f*/, 1.6f)).build());

    public EntityRegistry() {
        registerEntityAttributes();
    }

    private static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(CENTAUR_MOUNT, MountEntity.createMountAttributes());
    }
}
