package com.github.theredbrain.betteradventuremode.spell_engine;

import net.spell_engine.internals.casting.SpellCast;

public interface DuckSpellCastAttemptMixin {
    static SpellCast.Attempt failCustom() {
        return new SpellCast.Attempt(ExtendedSpellCastAttemptResult.CUSTOM_FAIL, null, null);
    }
}
