package com.github.theredbrain.betteradventuremode.mixin.spell_engine.internals;

import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellContainerMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.api.spell.SpellPool;
import net.spell_engine.compat.TrinketsCompat;
import net.spell_engine.internals.SpellContainerHelper;
import net.spell_engine.internals.SpellRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(SpellContainerHelper.class)
public abstract class SpellContainerHelperMixin {

    @Shadow
    private static boolean isOffhandContainerValid(PlayerEntity player, SpellContainer.ContentType allowedContent) {
        throw new AssertionError();
    }

    @Shadow
    private static List<String> getOffhandSpellIds(PlayerEntity player) {
        throw new AssertionError();
    }

    /**
     *
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public static SpellContainer getEquipped(SpellContainer proxyContainer, PlayerEntity player) {
        if (proxyContainer != null && proxyContainer.is_proxy) {
            LinkedHashSet<String> spellIds = new LinkedHashSet(proxyContainer.spell_ids);

            SpellPool validSpellPool = SpellPool.empty;
            if (((DuckSpellContainerMixin) proxyContainer).betteradventuremode$getProxyPool() != null && !(((DuckSpellContainerMixin) proxyContainer).betteradventuremode$getProxyPool().isEmpty())) {
                validSpellPool = SpellRegistry.spellPool(new Identifier(((DuckSpellContainerMixin) proxyContainer).betteradventuremode$getProxyPool()));
            }

            if (TrinketsCompat.isEnabled()) {
                spellIds.addAll(TrinketsCompat.getEquippedSpells(proxyContainer, player));
            }

            if (SpellEngineMod.config.spell_book_offhand && isOffhandContainerValid(player, proxyContainer.content)) {
                spellIds.addAll(getOffhandSpellIds(player));
            }

            ArrayList<SpellInfo> spells = new ArrayList();
            Iterator var4 = spellIds.iterator();

            while(var4.hasNext()) {
                String idString = (String)var4.next();
                Identifier id = new Identifier(idString);
                Spell spell = SpellRegistry.getSpell(id);
                if (spell != null) {
                    spells.add(new SpellInfo(spell, id));
                }
            }

            HashSet<String> toRemove = new HashSet();
            Iterator var11 = spells.iterator();

            while(true) {
                SpellInfo spell;
                String tag;
                do {
                    if (!var11.hasNext()) {
                        spellIds.removeAll(toRemove);
                        return new SpellContainer(proxyContainer.content, false, (String)null, 0, new ArrayList(spellIds));
                    }

                    spell = (SpellInfo)var11.next();
                    tag = spell.spell().group;
                } while(tag == null);

                Iterator var8 = spells.iterator();

                while(var8.hasNext()) {
                    SpellInfo other = (SpellInfo)var8.next();
                    if (!spell.id().equals(other.id()) && tag.equals(other.spell().group) && spell.spell().learn.tier > other.spell().learn.tier) {
                        toRemove.add(other.id().toString());
                    }

                    if (validSpellPool != SpellPool.empty) {
                        if (!validSpellPool.spellIds().contains(other.id())) {
                            toRemove.add(other.id().toString());
                        }
                    }
                }
            }
        } else {
            return proxyContainer;
        }
    }
}
