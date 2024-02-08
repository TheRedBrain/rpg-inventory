package com.github.theredbrain.betteradventuremode.client.render.entity;

import net.minecraft.text.Text;

public enum MannequinModelPart {
    HEAD(0, "head", true),
    HAT(1, "hat", true),
    BODY(2, "body", true),
    JACKET(3, "jacket", true),
    LEFT_ARM(4, "left_arm", true),
    LEFT_SLEEVE(5, "left_sleeve", true),
    RIGHT_ARM(6, "right_arm", true),
    RIGHT_SLEEVE(7, "right_sleeve", true),
    LEFT_LEG(0, "left_leg", false),
    LEFT_PANTS(1, "left_pants", false),
    RIGHT_LEG(2, "right_leg", false),
    RIGHT_PANTS(3, "right_pants", false);

    private final int id;
    private final int bitFlag;
    private final String name;
    private final Text optionName;
    private final boolean isFirstHalf;

    private MannequinModelPart(int id, String name, boolean isFirstHalf) {
        this.id = id;
        this.bitFlag = 1 << id;
        this.name = name;
        this.optionName = Text.translatable("mannequin_entity.modelPart." + name);
        this.isFirstHalf = isFirstHalf;
    }

    public int getBitFlag() {
        return this.bitFlag;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Text getOptionName() {
        return this.optionName;
    }

    public boolean isFirstHalf() {
        return this.isFirstHalf;
    }
}
