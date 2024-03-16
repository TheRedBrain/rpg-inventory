package com.github.theredbrain.betteradventuremode.mixin.spell_engine.internals;

import net.spell_engine.internals.casting.SpellCast;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(SpellCast.Attempt.Result.class)
public class SpellCastAttemptResultMixin {
    @Invoker("<init>")
    private static SpellCast.Attempt.Result init(String name, int id) {
        throw new AssertionError();
    }
    @Shadow
    @Final
    @Mutable
    private static SpellCast.Attempt.Result[] $VALUES;

    static {
        ArrayList<SpellCast.Attempt.Result> values =  new ArrayList<>(Arrays.asList($VALUES));
        SpellCast.Attempt.Result last = values.get(values.size() - 1);

        // add new value
        values.add( init("CUSTOM_FAIL", last.ordinal() + 1) );

        $VALUES = values.toArray(new SpellCast.Attempt.Result[0]);
    }
}
