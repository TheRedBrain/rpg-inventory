package com.github.theredbrain.bamcore.mixin.spell_engine.api.spell;

import com.github.theredbrain.bamcore.spell_engine.DuckSpellImpactActionHealMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Spell.Impact.Action.Heal.class)
public class SpellImpactActionHealMixin implements DuckSpellImpactActionHealMixin {
    private double direct_heal = 0.0;

    @Override
    public double getDirectHeal() {
        return direct_heal;
    }

    @Override
    public void setDirectHeal(double directHeal) {
        this.direct_heal = directHeal;
    }
}
