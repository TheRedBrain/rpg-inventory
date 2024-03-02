package com.github.theredbrain.betteradventuremode.mixin.spell_engine.api.spell;

import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellImpactActionDamageMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Spell.Impact.Action.Damage.class)
public class SpellImpactActionDamageMixin implements DuckSpellImpactActionDamageMixin {
    @Unique
    private double direct_damage = 0.0;
    @Unique
    private String damage_type_override = "";

    @Override
    public double betteradventuremode$getDirectDamage() {
        return direct_damage;
    }

    @Override
    public String betteradventuremode$getDamageTypeOverride() {
        return damage_type_override;
    }
}
