package com.github.theredbrain.betteradventuremode.mixin.spell_engine.api.spell;

import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellContainerMixin;
import net.spell_engine.api.spell.SpellContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SpellContainer.class)
public class SpellContainerMixin implements DuckSpellContainerMixin {
    @Unique
    private String proxyPool = null;

    public String betteradventuremode$getProxyPool() {
        return proxyPool;
    }

    public void betteradventuremode$setProxyPool(String proxyPool) {
        this.proxyPool = proxyPool;
    }
}
