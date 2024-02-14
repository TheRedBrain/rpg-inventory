package com.github.theredbrain.betteradventuremode.util;

import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.EulerAngle;
import org.joml.Vector3f;

public class MathUtils {
    public static EulerAngle eulerAngleToFloatVector(Vector3f vector3f) {
        return new EulerAngle(vector3f.x, vector3f.y, vector3f.z);
    }

    public static Vector3f eulerAngleToFloatVector(EulerAngle eulerAngle) {
        return new Vector3f(eulerAngle.getPitch(), eulerAngle.getYaw(), eulerAngle.getRoll());
    }

    public static NbtList vector3fToNbt(Vector3f vector3f) {
        NbtList nbtList = new NbtList();
        nbtList.add(NbtFloat.of(vector3f.x));
        nbtList.add(NbtFloat.of(vector3f.y));
        nbtList.add(NbtFloat.of(vector3f.z));
        return nbtList;
    }

    public static Vector3f vector3fFromNbt(NbtList serialized) {
        return new Vector3f(serialized.getFloat(0), serialized.getFloat(1), serialized.getFloat(2));
    }
}
