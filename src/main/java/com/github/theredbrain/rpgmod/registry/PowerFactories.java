package com.github.theredbrain.rpgmod.registry;
//
//import com.github.theredbrain.rpgmod.RPGMod;
//import com.github.theredbrain.rpgmod.entity.ridable.CentaurMountEntity;
//import com.github.theredbrain.rpgmod.power.CentaurMountPower;
//import io.github.apace100.apoli.Apoli;
//import io.github.apace100.apoli.power.FreezePower;
//import io.github.apace100.apoli.power.Power;
//import io.github.apace100.apoli.power.factory.PowerFactory;
//import io.github.apace100.apoli.registry.ApoliRegistries;
//import net.minecraft.registry.Registry;
//import net.minecraft.util.Identifier;
//
//public class PowerFactories {
//    public static void register() {
//        register(() -> Power.createSimpleFactory(CentaurMountPower::new, new Identifier(RPGMod.MOD_ID, "centaur_mount_power")));
//    }
//
//    private static void register(PowerFactory<?> powerFactory) {
//        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
//    }
//}
