package com.github.theredbrain.bamcore.api.util;

import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.tuple.MutablePair;

public class BlockRotationUtils {

    public static int calculateNewRotatedBlockState(int currentStateValue, BlockRotation rotation) {
        if (rotation == BlockRotation.CLOCKWISE_90) {
            return (currentStateValue + 1) % 4;
        } else if (rotation == BlockRotation.CLOCKWISE_180){
            return (currentStateValue + 2) % 4;
        } else if (rotation == BlockRotation.COUNTERCLOCKWISE_90){
            return (currentStateValue + 3) % 4;
        } else {
            return currentStateValue;
        }
    }

    public static Direction calculateNewHorizontalFacingBlockState(Direction currentState, BlockRotation rotation) {
        if (rotation == BlockRotation.CLOCKWISE_90) {
            return currentState.rotateYClockwise();
        } else if (rotation == BlockRotation.CLOCKWISE_180){
            return currentState.getOpposite();
        } else if (rotation == BlockRotation.COUNTERCLOCKWISE_90){
            return currentState.rotateYCounterclockwise();
        } else {
            return currentState;
        }
    }

    public static BlockRotation calculateRotationFromDifferentRotatedStates(int value1, int value2) {
        if ((value2 + 1) % 4 == value1) {
            return BlockRotation.CLOCKWISE_90;
        } else if ((value2 + 2) % 4 == value1) {
            return BlockRotation.CLOCKWISE_180;
        } else if ((value2 + 3) % 4 == value1) {
            return BlockRotation.COUNTERCLOCKWISE_90;
        } else {
            return BlockRotation.NONE;
        }
    }

    public static BlockPos rotateOffsetBlockPos(BlockPos blockPos, BlockRotation rotation) {
        if (rotation == BlockRotation.CLOCKWISE_90) {
            return new BlockPos(-(blockPos.getZ()), blockPos.getY(), blockPos.getX());
        } else if (rotation == BlockRotation.CLOCKWISE_180) {
            return new BlockPos(-(blockPos.getX()), blockPos.getY(), -(blockPos.getZ()));
        } else if (rotation == BlockRotation.COUNTERCLOCKWISE_90) {
            return new BlockPos(blockPos.getZ(), blockPos.getY(), -(blockPos.getX()));
        } else {
            return blockPos;
        }
    }

    public static BlockPos mirrorOffsetBlockPos(BlockPos blockPos, BlockMirror mirror) {
        if (mirror == BlockMirror.FRONT_BACK) {
            return new BlockPos(-(blockPos.getX()), blockPos.getY(), blockPos.getZ());
        } else if (mirror == BlockMirror.LEFT_RIGHT) {
            return new BlockPos(blockPos.getX(), blockPos.getY(), -(blockPos.getZ()));
        } else {
            return blockPos;
        }
    }

    public static Vec3i rotateOffsetVec3i(Vec3i vec3i, BlockRotation rotation) {
        if (rotation == BlockRotation.CLOCKWISE_90) {
            return new Vec3i(-(vec3i.getZ()), vec3i.getY(), vec3i.getX());
        } else if (rotation == BlockRotation.CLOCKWISE_180) {
            return new Vec3i(-(vec3i.getX()), vec3i.getY(), -(vec3i.getZ()));
        } else if (rotation == BlockRotation.COUNTERCLOCKWISE_90) {
            return new Vec3i(vec3i.getZ(), vec3i.getY(), -(vec3i.getX()));
        } else {
            return vec3i;
        }
    }

    public static Vec3i mirrorOffsetVec3i(Vec3i vec3i, BlockMirror mirror) {
        if (mirror == BlockMirror.FRONT_BACK) {
            return new Vec3i(-(vec3i.getX()), vec3i.getY(), vec3i.getZ());
        } else if (mirror == BlockMirror.LEFT_RIGHT) {
            return new Vec3i(vec3i.getX(), vec3i.getY(), -(vec3i.getZ()));
        } else {
            return vec3i;
        }
    }

    public static double rotateYaw(double yaw, BlockRotation rotation) {
        double tempYaw = yaw + 180;
        if (rotation == BlockRotation.CLOCKWISE_90) {
            tempYaw = (tempYaw + 90)%360;
            return tempYaw - 180;
        } else if (rotation == BlockRotation.CLOCKWISE_180) {
            tempYaw = (tempYaw + 180)%360;
            return tempYaw - 180;
        } else if (rotation == BlockRotation.COUNTERCLOCKWISE_90) {
            tempYaw = (tempYaw - 90)%360;
            return tempYaw - 180;
        } else {
            return yaw;
        }

    }

    public static double mirrorYaw(double yaw, BlockMirror mirror) {
        if (mirror == BlockMirror.FRONT_BACK) {
            return -yaw;
        } else if (mirror == BlockMirror.LEFT_RIGHT) {
            if (yaw < -90) {
                return yaw + 90;
            } else if (yaw < 0) {
                return yaw - 90;
            } else if (yaw > 90) {
                return yaw - 90;
            } else if (yaw > 0) {
                return yaw + 90;
            } else {
                return yaw;
            }
        } else {
            return yaw;
        }
    }

    public static MutablePair<Integer, Integer> rotateOffset2DPosition(MutablePair<Integer, Integer> offsetPosition, BlockRotation rotation) {
        if (rotation == BlockRotation.CLOCKWISE_90) {
            return MutablePair.of(-(offsetPosition.getRight()), offsetPosition.getLeft());
        } else if (rotation == BlockRotation.CLOCKWISE_180) {
            return MutablePair.of(-(offsetPosition.getLeft()), -(offsetPosition.getRight()));
        } else if (rotation == BlockRotation.COUNTERCLOCKWISE_90) {
            return MutablePair.of(offsetPosition.getRight(), -(offsetPosition.getLeft()));
        } else {
            return offsetPosition;
        }
    }

    public static MutablePair<Integer, Integer> mirrorOffset2DPosition(MutablePair<Integer, Integer> offsetPosition, BlockMirror mirror) {
        if (mirror == BlockMirror.FRONT_BACK) {
            return MutablePair.of(-(offsetPosition.getLeft()), offsetPosition.getRight());
        } else if (mirror == BlockMirror.LEFT_RIGHT) {
            return MutablePair.of(offsetPosition.getLeft(), -(offsetPosition.getRight()));
        } else {
            return offsetPosition;
        }
    }

    public static MutablePair<BlockPos, MutablePair<Double, Double>> rotateEntrance(MutablePair<BlockPos, MutablePair<Double, Double>> entrance, BlockRotation rotation) {
        return new MutablePair<>(BlockRotationUtils.rotateOffsetBlockPos(entrance.getLeft(), rotation), new MutablePair<>(BlockRotationUtils.rotateYaw(entrance.getRight().getLeft(), rotation), entrance.getRight().getRight()));
    }

    public static MutablePair<BlockPos, MutablePair<Double, Double>> mirrorEntrance(MutablePair<BlockPos, MutablePair<Double, Double>> entrance, BlockMirror mirror) {
        return new MutablePair<>(BlockRotationUtils.mirrorOffsetBlockPos(entrance.getLeft(), mirror), new MutablePair<>(BlockRotationUtils.mirrorYaw(entrance.getRight().getLeft(), mirror), entrance.getRight().getRight()));
    }

    public static MutablePair<BlockPos, Vec3i> rotateOffsetArea(BlockPos positionOffset, Vec3i areaDimensions, BlockRotation rotation) {
        if (rotation == BlockRotation.CLOCKWISE_90) {
            Vec3i newAreaDimensions = new Vec3i(areaDimensions.getZ(), areaDimensions.getY(), areaDimensions.getX());
            BlockPos newPositionOffset = new BlockPos(-(positionOffset.getZ()) - newAreaDimensions.getX() + 1, positionOffset.getY(), positionOffset.getX());
            return MutablePair.of(newPositionOffset, newAreaDimensions);
        } else if (rotation == BlockRotation.CLOCKWISE_180) {
            BlockPos newPositionOffset = new BlockPos(-(positionOffset.getX()) - areaDimensions.getX() + 1, positionOffset.getY(), -(positionOffset.getZ()) - areaDimensions.getZ() + 1);
            return MutablePair.of(newPositionOffset, areaDimensions);
        } else if (rotation == BlockRotation.COUNTERCLOCKWISE_90) {
            Vec3i newAreaDimensions = new Vec3i(areaDimensions.getZ(), areaDimensions.getY(), areaDimensions.getX());
            BlockPos newPositionOffset = new BlockPos(positionOffset.getZ(), positionOffset.getY(), -(positionOffset.getX()) - newAreaDimensions.getZ() + 1);
            return MutablePair.of(newPositionOffset, newAreaDimensions);
        } else {
            return MutablePair.of(positionOffset, areaDimensions);
        }
    }

    public static MutablePair<BlockPos, Vec3i> mirrorOffsetArea(BlockPos positionOffset, Vec3i areaDimensions, BlockMirror mirror) {
        if (mirror == BlockMirror.FRONT_BACK) {
            BlockPos newPositionOffset = new BlockPos(-(positionOffset.getX()) - areaDimensions.getX() + 1, positionOffset.getY(), positionOffset.getZ());
            return MutablePair.of(newPositionOffset, areaDimensions);
        } else if (mirror == BlockMirror.LEFT_RIGHT) {
            BlockPos newPositionOffset = new BlockPos(positionOffset.getX(), positionOffset.getY(), -(positionOffset.getZ()) - areaDimensions.getZ() + 1);
            return MutablePair.of(newPositionOffset, areaDimensions);
        } else {
            return MutablePair.of(positionOffset, areaDimensions);
        }
    }
}
