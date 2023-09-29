package com.github.theredbrain.bamcore.power;
//
//import com.github.theredbrain.rpgmod.RPGMod;
//import com.github.theredbrain.rpgmod.entity.ridable.MountEntity;
//import com.github.theredbrain.rpgmod.registry.EntityRegistry;
//import io.github.apace100.apoli.Apoli;
//import io.github.apace100.apoli.data.ApoliDataTypes;
//import io.github.apace100.apoli.power.Power;
//import io.github.apace100.apoli.power.PowerType;
//import io.github.apace100.apoli.power.RestrictArmorPower;
//import io.github.apace100.apoli.power.factory.PowerFactory;
//import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
//import io.github.apace100.calio.data.SerializableData;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.ai.TargetPredicate;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.Identifier;
//import net.minecraft.world.World;
//
//import java.util.HashMap;
//import java.util.function.Predicate;
//
//public class CentaurMountPower extends Power { // TODO wait for 1.19.4
////    private static final TargetPredicate MOUNT_ENTITY_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(2.0).ignoreVisibility();
//
////    private EntityType entityType;
//
//    public CentaurMountPower(PowerType<?> type, LivingEntity entity) {
//        super(type, entity);
////        this.entityType = entityType;
//    }
//
//    @Override
//    public void onAdded() {
//        super.onAdded();
//        World world = this.entity.getWorld();
//        if (!world.isClient()) {
//
//            MountEntity mount = new MountEntity(EntityRegistry.CENTAUR_MOUNT, world);
//            world.spawnEntity(mount);
////            MountEntity mount = this.entity.world.getClosestEntity(MountEntity.class, MOUNT_ENTITY_PREDICATE, this.entity, this.entity.getX(), this.entity.getY(), this.entity.getZ())
//            this.entity.startRiding(mount);
//        }
//    }
//
//    public void onRespawn() {
//        super.onRespawn();
//        World world = this.entity.getWorld();
//        if (!world.isClient()) {
//
//            MountEntity mount = new MountEntity(EntityRegistry.CENTAUR_MOUNT, world);
//            world.spawnEntity(mount);
////            MountEntity mount = this.entity.world.getClosestEntity(MountEntity.class, MOUNT_ENTITY_PREDICATE, this.entity, this.entity.getX(), this.entity.getY(), this.entity.getZ())
//            this.entity.startRiding(mount);
//        }
//    }
////
////    public static PowerFactory createFactory() {
////        return new PowerFactory<>(RPGMod.identifier("summon_and_ride_mount"),
////                new SerializableData()
////                        .add("mount",
////                                MountEntity mount = new MountEntity(this.entityType, world);, null)
////                        .add("chest", ApoliDataTypes.ITEM_CONDITION, null)
////                        .add("legs", ApoliDataTypes.ITEM_CONDITION, null)
////                        .add("feet", ApoliDataTypes.ITEM_CONDITION, null),
////                data ->
////                        (type, player) -> {
////                            HashMap<EquipmentSlot, Predicate<ItemStack>> restrictions = new HashMap<>();
////                            if(data.isPresent("head")) {
////                                restrictions.put(EquipmentSlot.HEAD, (ConditionFactory<ItemStack>.Instance)data.get("head"));
////                            }
////                            if(data.isPresent("chest")) {
////                                restrictions.put(EquipmentSlot.CHEST, (ConditionFactory<ItemStack>.Instance)data.get("chest"));
////                            }
////                            if(data.isPresent("legs")) {
////                                restrictions.put(EquipmentSlot.LEGS, (ConditionFactory<ItemStack>.Instance)data.get("legs"));
////                            }
////                            if(data.isPresent("feet")) {
////                                restrictions.put(EquipmentSlot.FEET, (ConditionFactory<ItemStack>.Instance)data.get("feet"));
////                            }
////                            return new RestrictArmorPower(type, player, restrictions);
////                        });
////    }
//}
