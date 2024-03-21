package com.github.theredbrain.betteradventuremode.data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;

public class WeaponPosesHelper {

    private static Type registeredWeaponPosesContainerFileFormat = new TypeToken<WeaponPosesContainer>() {}.getType();

    public static WeaponPoses override(WeaponPoses a, WeaponPoses b) {
        String pose = b.pose() != null ? b.pose() : a.pose();
        String poseBlocking = b.poseBlocking() != null ? b.poseBlocking() : a.poseBlocking();
        String offHandPose = b.offHandPose() != null ? b.offHandPose() : a.offHandPose();
        String offHandPoseBlocking = b.offHandPoseBlocking() != null ? b.offHandPoseBlocking() : a.offHandPoseBlocking();
        String twoHandedPose = b.twoHandedPose() != null ? b.twoHandedPose() : a.twoHandedPose();
        return new WeaponPoses(pose, poseBlocking, offHandPose, offHandPoseBlocking, twoHandedPose);
    }

    public static WeaponPosesContainer decode(Reader reader) {
        var gson = new Gson();
        WeaponPosesContainer weaponPosesContainer = gson.fromJson(reader, registeredWeaponPosesContainerFileFormat);
        return weaponPosesContainer;
    }

    public static WeaponPosesContainer decode(JsonReader json) {
        var gson = new Gson();
        WeaponPosesContainer weaponPosesContainer = gson.fromJson(json, registeredWeaponPosesContainerFileFormat);
        return weaponPosesContainer;
    }

    public static String encode(WeaponPosesContainer weaponPosesContainer) {
        var gson = new Gson();
        return gson.toJson(weaponPosesContainer);
    }
}
