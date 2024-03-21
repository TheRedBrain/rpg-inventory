package com.github.theredbrain.betteradventuremode.data;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class WeaponPoses {

    private final @Nullable String pose;
    private final @Nullable String poseBlocking;
    private final @Nullable String offHandPose;
    private final @Nullable String offHandPoseBlocking;
    private final @Nullable String twoHandedPose;
//    private final @Nullable String two_handed_pose_walking;
//    private final @Nullable String two_handed_pose_sneaking;
//    private final @Nullable String two_handed_pose_sneak_walking;

    public WeaponPoses(@Nullable String pose, @Nullable String poseBlocking, @Nullable String offHandPose, @Nullable String offHandPoseBlocking, @Nullable String twoHandedPose) {
        this.pose = pose;
        this.poseBlocking = poseBlocking;
        this.offHandPose = offHandPose;
        this.offHandPoseBlocking = offHandPoseBlocking;
        this.twoHandedPose = twoHandedPose;
    }

    public @Nullable String pose() {
        return this.pose;
    }

    public @Nullable String poseBlocking() {
        return this.poseBlocking;
    }

    public @Nullable String offHandPose() {
        return this.offHandPose;
    }

    public @Nullable String offHandPoseBlocking() {
        return this.offHandPoseBlocking;
    }

    public @Nullable String twoHandedPose() {
        return this.twoHandedPose;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            WeaponPoses that = (WeaponPoses)obj;
            return Objects.equals(this.pose, that.pose) && Objects.equals(this.poseBlocking, that.poseBlocking) && Objects.equals(this.offHandPose, that.offHandPose) && Objects.equals(this.offHandPoseBlocking, that.offHandPoseBlocking) && Objects.equals(this.twoHandedPose, that.twoHandedPose);
        } else {
            return false;
        }
    }

    public String toString() {
        return "WeaponPoses[pose=" + this.pose + ", poseBlocking=" + this.poseBlocking + ", offHandPose=" + this.offHandPose + ", offHandPoseBlocking=" + this.offHandPoseBlocking + ", twoHandedPose=" + this.twoHandedPose + "]";
    }
}
