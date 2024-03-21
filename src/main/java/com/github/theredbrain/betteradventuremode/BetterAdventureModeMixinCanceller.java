package com.github.theredbrain.betteradventuremode;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class BetterAdventureModeMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if (mixinClassName.equals("net.bettercombat.mixin.client.AbstractClientPlayerEntityMixin")) {
            return true;
        }
        return false;
    }
}
