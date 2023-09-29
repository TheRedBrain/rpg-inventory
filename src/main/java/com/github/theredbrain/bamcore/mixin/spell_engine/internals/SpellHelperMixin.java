package com.github.theredbrain.bamcore.mixin.spell_engine.internals;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.spell_engine.DuckSpellCostMixin;
import com.github.theredbrain.bamcore.spell_engine.DuckSpellImpactActionDamageMixin;
import com.github.theredbrain.bamcore.spell_engine.DuckSpellImpactActionHealMixin;
import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.Function;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.spell.CustomSpellHandler;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.entity.ConfigurableKnockback;
import net.spell_engine.internals.*;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.AnimationHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(SpellHelper.class)
public abstract class SpellHelperMixin {

    @Shadow
    private static void beamImpact(World world, LivingEntity caster, List<Entity> targets, Spell spell, SpellHelper.ImpactContext context) {
        throw new AssertionError();
    }

    @Shadow
    private static void areaImpact(World world, LivingEntity caster, List<Entity> targets, Vec3d center, float range, Spell.Release.Target.Area area, boolean offset, Spell spell, SpellHelper.ImpactContext context) {
        throw new AssertionError();
    }

    @Shadow
    private static void directImpact(World world, LivingEntity caster, Entity target, Spell spell, SpellHelper.ImpactContext context) {
        throw new AssertionError();
    }

    @Shadow
    private static float cooldownToSet(LivingEntity caster, Spell spell, float progress) {
        throw new AssertionError();
    }

    /**
     * @author TheRedBrain
     * @reason adds direct damage and direct healing
     */
    @Overwrite
    private static boolean performImpact(World world, LivingEntity caster, Entity target, MagicSchool school, Spell.Impact impact, SpellHelper.ImpactContext context, Collection<ServerPlayerEntity> trackers) {
        if (!((Entity)target).isAttackable()) {
            return false;
        } else {
            boolean success = false;

            try {
                double particleMultiplier = (double)(1.0F * context.total());
                TargetHelper.getRelation(caster, (Entity)target);
                SpellPower.Result power = context.power();
                if (power == null) {
                    power = SpellPower.getSpellPower(school, caster);
                }

                if (power.baseValue() < (double)impact.action.min_power) {
                    power = new SpellPower.Result(power.school(), (double)impact.action.min_power, power.criticalChance(), power.criticalDamage());
                }

                if (impact.action.apply_to_caster) {
                    target = caster;
                }

                if (!TargetHelper.actionAllowed(context.targetingMode(), SpellHelper.intent(impact.action), caster, (Entity)target)) {
                    return false;
                }

                switch (impact.action.type) {
                    case DAMAGE:
                        Spell.Impact.Action.Damage damageData = impact.action.damage;
                        float knockbackMultiplier = Math.max(0.0F, damageData.knockback * context.total());
                        SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;
                        int timeUntilRegen = ((Entity)target).timeUntilRegen;
                        if (target instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity)target;
                            ((ConfigurableKnockback)livingEntity).setKnockbackMultiplier_SpellEngine(context.hasOffset() ? 0.0F : knockbackMultiplier);
                            if (SpellEngineMod.config.bypass_iframes) {
                                ((Entity)target).timeUntilRegen = 0;
                            }

                            vulnerability = SpellPower.getVulnerability(livingEntity, school);
                        }

                        double amount = power.randomValue(vulnerability);
                        amount *= (double)damageData.spell_power_coefficient;
                        amount *= (double)context.total();
                        if (context.isChanneled()) {
                            amount *= SpellPower.getHaste(caster);
                        }

                        double directDamageAmount = ((DuckSpellImpactActionDamageMixin) damageData).getDirectDamage();
                        BetterAdventureModCore.LOGGER.info("Direct Damage Amount: " + directDamageAmount);
                        if (directDamageAmount > 0.0) {
                            amount = directDamageAmount;
                        }

                        particleMultiplier = power.criticalDamage() + (double)vulnerability.criticalDamageBonus();
                        caster.onAttacking((Entity)target);
                        BetterAdventureModCore.LOGGER.info("Damage Amount: " + amount);
                        ((Entity)target).damage(SpellDamageSource.create(school, caster), (float)amount);
                        if (target instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity)target;
                            ((ConfigurableKnockback)livingEntity).setKnockbackMultiplier_SpellEngine(1.0F);
                            ((Entity)target).timeUntilRegen = timeUntilRegen;
                            if (context.hasOffset()) {
                                Vec3d direction = context.knockbackDirection(livingEntity.getPos()).negate();
                                livingEntity.takeKnockback((double)(0.4F * knockbackMultiplier), direction.x, direction.z);
                            }
                        }

                        success = true;
                        break;
                    case HEAL:
                        if (target instanceof LivingEntity) {
                            LivingEntity livingTarget = (LivingEntity)target;
                            Spell.Impact.Action.Heal healData = impact.action.heal;
                            particleMultiplier = power.criticalDamage();
                            double healAmount = power.randomValue();
                            healAmount *= (double)healData.spell_power_coefficient;
                            healAmount *= (double)context.total();
                            if (context.isChanneled()) {
                                healAmount *= SpellPower.getHaste(caster);
                            }

                            double directHealAmount = ((DuckSpellImpactActionHealMixin) healData).getDirectHeal();
                            if (directHealAmount > 0) {
                                healAmount = directHealAmount;
                            }

                            livingTarget.heal((float)healAmount);
                            success = true;
                        }
                        break;
                    case STATUS_EFFECT:
                        Spell.Impact.Action.StatusEffect data = impact.action.status_effect;
                        if (target instanceof LivingEntity) {
                            LivingEntity livingTarget = (LivingEntity)target;
                            Identifier id = new Identifier(data.effect_id);
                            StatusEffect effect = (StatusEffect)Registries.STATUS_EFFECT.get(id);
                            if (!SpellHelper.underApplyLimit(power, livingTarget, school, data.apply_limit)) {
                                return false;
                            }

                            int duration = Math.round(data.duration * 20.0F);
                            int amplifier = data.amplifier + (int)((double)data.amplifier_power_multiplier * power.nonCriticalValue());
                            boolean showParticles = data.show_particles;
                            switch (data.apply_mode) {
                                case ADD:
                                    StatusEffectInstance currentEffect = livingTarget.getStatusEffect(effect);
                                    int newAmplifier = 0;
                                    if (currentEffect != null) {
                                        int incrementedAmplifier = currentEffect.getAmplifier() + 1;
                                        newAmplifier = Math.min(incrementedAmplifier, amplifier);
                                    }

                                    amplifier = newAmplifier;
                                case SET:
                                default:
                                    livingTarget.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier, false, showParticles, true), caster);
                                    success = true;
                            }
                        }
                        break;
                    case FIRE:
                        Spell.Impact.Action.Fire fireData = impact.action.fire;
                        ((Entity)target).setOnFireFor(fireData.duration);
                        if (((Entity)target).getFireTicks() > 0) {
                            ((Entity)target).setFireTicks(((Entity)target).getFireTicks() + fireData.tick_offset);
                        }
                }

                if (success) {
                    if (impact.particles != null) {
                        ParticleHelper.sendBatches((Entity)target, impact.particles, (float)particleMultiplier, trackers);
                    }

                    if (impact.sound != null) {
                        SoundHelper.playSound(world, (Entity)target, impact.sound);
                    }
                }
            } catch (Exception var22) {
                System.err.println("Failed to perform impact effect");
                System.err.println(var22.getMessage());
                if (target instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity)target;
                    ((ConfigurableKnockback)livingEntity).setKnockbackMultiplier_SpellEngine(1.0F);
                }
            }

            return success;
        }
    }

    /**
     * @author TheRedBrain
     * @reason adds mana cost, health cost, reducing amplifier of status effect cost instead of removing them, self consuming of casting item
     */
    @Overwrite
    public static void performSpell(World world, PlayerEntity player, Identifier spellId, List<Entity> targets, ItemStack itemStack, SpellCast.Action action, Hand hand, int remainingUseTicks) {
        Spell spell = SpellRegistry.getSpell(spellId);
        if (spell != null) {
            SpellInfo spellInfo = new SpellInfo(spell, spellId);
            SpellCasterEntity caster = (SpellCasterEntity)player;
            if (!caster.getCooldownManager().isCoolingDown(spellId)) {
                float progress = SpellHelper.getCastProgress(player, remainingUseTicks, spell);
                float channelMultiplier = 1.0F;
                boolean shouldPerformImpact = true;
                Supplier<Collection<ServerPlayerEntity>> trackingPlayers = Suppliers.memoize(() -> {
                    return PlayerLookup.tracking(player);
                });
                switch (action) {
                    case START:
                        SoundHelper.playSound(player.getWorld(), player, spell.cast.start_sound);
                        SpellCastSyncHelper.setCasting(player, hand, spellId, (Collection)trackingPlayers.get());
                        return;
                    case CHANNEL:
                        channelMultiplier = SpellHelper.channelValueMultiplier(spell);
                        break;
                    case RELEASE:
                        if (SpellHelper.isChanneled(spell)) {
                            shouldPerformImpact = false;
                            channelMultiplier = 1.0F;
                        } else {
                            channelMultiplier = progress >= 1.0F ? 1.0F : 0.0F;
                        }

                        SpellCastSyncHelper.clearCasting(player, (Collection)trackingPlayers.get());
                }

                SpellHelper.AmmoResult ammoResult = SpellHelper.ammoForSpell(player, spell, itemStack);
                if (channelMultiplier > 0.0F && ammoResult.satisfied()) {
                    Spell.Release.Target targeting = spell.release.target;
                    boolean released = action == SpellCast.Action.RELEASE;
                    if (shouldPerformImpact) {
                        SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(channelMultiplier, 1.0F, (Vec3d)null, SpellPower.getSpellPower(spell.school, player), SpellHelper.impactTargetingMode(spell));
                        if (spell.release.custom_impact) {
                            Function<CustomSpellHandler.Data, Boolean> handler = (Function)CustomSpellHandler.handlers.get(spellId);
                            released = false;
                            if (handler != null) {
                                released = (Boolean)handler.apply(new CustomSpellHandler.Data(player, targets, itemStack, action, hand, remainingUseTicks, context));
                            }
                        } else {
                            Optional target;
                            switch (targeting.type) {
                                case AREA:
                                    Vec3d center = player.getPos().add(0.0, (double)(player.getHeight() / 2.0F), 0.0);
                                    Spell.Release.Target.Area area = spell.release.target.area;
                                    areaImpact(world, player, targets, center, spell.range, area, false, spell, context);
                                    break;
                                case BEAM:
                                    beamImpact(world, player, targets, spell, context);
                                    break;
                                case CURSOR:
                                    target = targets.stream().findFirst();
                                    if (target.isPresent()) {
                                        directImpact(world, player, (Entity)target.get(), spell, context);
                                    } else {
                                        released = false;
                                    }
                                    break;
                                case PROJECTILE:
                                    Entity targetEntity = null;
                                    Optional<Entity> entityFound = targets.stream().findFirst();
                                    if (entityFound.isPresent()) {
                                        targetEntity = (Entity)entityFound.get();
                                    }

                                    SpellHelper.shootProjectile(world, player, targetEntity, spellInfo, context);
                                    break;
                                case METEOR:
                                    target = targets.stream().findFirst();
                                    if (target.isPresent()) {
                                        SpellHelper.fallProjectile(world, player, (Entity)target.get(), spellInfo, context);
                                    } else {
                                        released = false;
                                    }
                                    break;
                                case SELF:
                                    directImpact(world, player, player, spell, context);
                                    released = true;
                            }
                        }
                    }

                    if (released) {
                        ParticleHelper.sendBatches(player, spell.release.particles);
                        SoundHelper.playSound(world, player, spell.release.sound);
                        AnimationHelper.sendAnimation(player, (Collection)trackingPlayers.get(), SpellCast.Animation.RELEASE, spell.release.animation);
                        float duration = cooldownToSet(player, spell, progress);
                        if (duration > 0.0F) {
                            ((SpellCasterEntity)player).getCooldownManager().set(spellId, Math.round(duration * 20.0F));
                        }

                        player.addExhaustion(spell.cost.exhaust * SpellEngineMod.config.spell_cost_exhaust_multiplier);

                        // health cost
                        float healthCost = ((DuckSpellCostMixin) spell.cost).getHealthCost();
                        if (healthCost > 0.0F) {
                            player.heal(-healthCost);
                        }

                        // mana cost
                        float manaCost = ((DuckSpellCostMixin) spell.cost).getManaCost();
                        if (manaCost > 0.0F) {
                            ((DuckPlayerEntityMixin)player).bamcore$addMana(-manaCost);
                        }

                        // consume spell casting item (used for spell scrolls)
                        if (((DuckSpellCostMixin) spell.cost).isConsumeSelf() && !player.isCreative()) {
                            player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
                            if (!player.isCreative()) {
                                itemStack.decrement(1);
                            }
                        }

                        if (SpellEngineMod.config.spell_cost_durability_allowed && spell.cost.durability > 0) {
                            itemStack.damage(spell.cost.durability, player, (playerObj) -> {
                                playerObj.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                                playerObj.sendEquipmentBreakStatus(EquipmentSlot.OFFHAND);
                            });
                        }

                        if (ammoResult.ammo() != null) {
                            for(int i = 0; i < player.getInventory().size(); ++i) {
                                ItemStack stack = player.getInventory().getStack(i);
                                if (stack.isOf(ammoResult.ammo().getItem())) {
                                    stack.decrement(1);
                                    if (stack.isEmpty()) {
                                        player.getInventory().removeOne(stack);
                                    }
                                    break;
                                }
                            }
                        }

//                        if (spell.cost.effect_id != null) {
//                            StatusEffect effect = (StatusEffect)Registries.STATUS_EFFECT.get(new Identifier(spell.cost.effect_id));
//                            player.removeStatusEffect(effect);
//                        }
                        if (spell.cost.effect_id != null) {
                            StatusEffect effect = (StatusEffect) Registries.STATUS_EFFECT.get(new Identifier(spell.cost.effect_id));
                            int newAmplifier = -1;
                            StatusEffectInstance statusEffectInstance = player.getStatusEffect(effect);
                            if (statusEffectInstance != null) {
                                int oldAmplifier = statusEffectInstance.getAmplifier();
                                newAmplifier = oldAmplifier - 1;
                            }
                            if (newAmplifier < 0) {
                                player.removeStatusEffect(effect);
                            } else {
                                // TODO remove old instance?
                                player.addStatusEffect(new StatusEffectInstance(effect, statusEffectInstance.getDuration(), newAmplifier, statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles(), statusEffectInstance.shouldShowIcon()), (Entity) caster);
                            }
                        }
                    }
                }

            }
        }
    }
//    public static void performSpell(World world, PlayerEntity player, Identifier spellId, List<Entity> targets, ItemStack itemStack, SpellCast.Action action, Hand hand, int remainingUseTicks) {
//        Spell spell = SpellRegistry.getSpell(spellId);
//        if (spell != null) {
//            SpellCasterEntity caster = (SpellCasterEntity)player;
//            if (!caster.getCooldownManager().isCoolingDown(spellId)) {
//                float progress = SpellHelper.getCastProgress(player, remainingUseTicks, spell);
//                float channelMultiplier = 1.0F;
//                boolean shouldPerformImpact = true;
//                Supplier<Collection<ServerPlayerEntity>> trackingPlayers = Suppliers.memoize(() -> {
//                    return PlayerLookup.tracking(player);
//                });
//                switch (action) {
//                    case START:
//                        SoundHelper.playSound(player.getWorld(), player, spell.cast.start_sound);
//                        SpellCastSyncHelper.setCasting(player, hand, spellId, (Collection)trackingPlayers.get());
//                        return;
//                    case CHANNEL:
//                        channelMultiplier = SpellHelper.channelValueMultiplier(spell);
//                        break;
//                    case RELEASE:
//                        if (SpellHelper.isChanneled(spell)) {
//                            shouldPerformImpact = false;
//                            channelMultiplier = 1.0F;
//                        } else {
//                            channelMultiplier = progress >= 1.0F ? 1.0F : 0.0F;
//                        }
//
//                        SpellCastSyncHelper.clearCasting(player, (Collection)trackingPlayers.get());
//                }
//
//                SpellHelper.AmmoResult ammoResult = SpellHelper.ammoForSpell(player, spell, itemStack);
//                if (channelMultiplier > 0.0F && ammoResult.satisfied()) {
//                    Spell.Release.Target targeting = spell.release.target;
//                    boolean released = action == SpellCast.Action.RELEASE;
//                    if (shouldPerformImpact) {
//                        SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(channelMultiplier, 1.0F, (Vec3d)null, SpellPower.getSpellPower(spell.school, player), SpellHelper.impactTargetingMode(spell));
//                        if (spell.release.custom_impact) {
//                            Function<CustomSpellHandler.Data, Boolean> handler = (Function)CustomSpellHandler.handlers.get(spellId);
//                            released = false;
//                            if (handler != null) {
//                                released = (Boolean)handler.apply(new CustomSpellHandler.Data(player, targets, itemStack, action, hand, remainingUseTicks, context));
//                            }
//                        } else {
//                            Optional target;
//                            switch (targeting.type) {
//                                case AREA:
//                                    Vec3d center = player.getPos().add(0.0, (double)(player.getHeight() / 2.0F), 0.0);
//                                    Spell.Release.Target.Area area = spell.release.target.area;
//                                    areaImpact(world, player, targets, center, spell.range, area, false, spell, context);
//                                    break;
//                                case BEAM:
//                                    beamImpact(world, player, targets, spell, context);
//                                    break;
//                                case CURSOR:
//                                    target = targets.stream().findFirst();
//                                    if (target.isPresent()) {
//                                        directImpact(world, player, (Entity)target.get(), spell, context);
//                                    } else {
//                                        released = false;
//                                    }
//                                    break;
//                                case PROJECTILE:
//                                    Entity targetEntity = null;
//                                    Optional<Entity> entityFound = targets.stream().findFirst();
//                                    if (entityFound.isPresent()) {
//                                        targetEntity = (Entity)entityFound.get();
//                                    }
//
//                                    shootProjectile(world, player, targetEntity, spell, context);
//                                    break;
//                                case METEOR:
//                                    target = targets.stream().findFirst();
//                                    if (target.isPresent()) {
//                                        fallProjectile(world, player, (Entity)target.get(), spell, context);
//                                    } else {
//                                        released = false;
//                                    }
//                                    break;
//                                case SELF:
//                                    directImpact(world, player, player, spell, context);
//                                    released = true;
//                            }
//                        }
//                    }
//
//                    if (released) {
//                        ParticleHelper.sendBatches(player, spell.release.particles);
//                        SoundHelper.playSound(world, player, spell.release.sound);
//                        AnimationHelper.sendAnimation(player, (Collection)trackingPlayers.get(), SpellCast.Animation.RELEASE, spell.release.animation);
//                        float duration = cooldownToSet(player, spell, progress);
//                        if (duration > 0.0F) {
//                            ((SpellCasterEntity)player).getCooldownManager().set(spellId, Math.round(duration * 20.0F));
//                        }
//
//                        player.addExhaustion(spell.cost.exhaust * SpellEngineMod.config.spell_cost_exhaust_multiplier);
//
//                        // health cost
//                        float healthCost = ((DuckSpellCostMixin) spell.cost).getHealthCost();
//                        if (healthCost > 0.0F) {
//                            player.heal(-healthCost);
//                        }
//
//                        // mana cost
//                        float manaCost = ((DuckSpellCostMixin) spell.cost).getManaCost();
//                        if (manaCost > 0.0F) {
//                            ((DuckPlayerEntityMixin)player).bam$addMana(-manaCost);
//                        }
//
//                        if (((DuckSpellCostMixin) spell.cost).isConsumeSelf() && !player.isCreative()) {
//                            player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
//                            if (!player.isCreative()) {
//                                itemStack.decrement(1);
//                            }
//                        }
//
//                        if (SpellEngineMod.config.spell_cost_durability_allowed && spell.cost.durability > 0) {
//                            itemStack.damage(spell.cost.durability, player, (playerObj) -> {
//                                playerObj.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
//                                playerObj.sendEquipmentBreakStatus(EquipmentSlot.OFFHAND);
//                            });
//                        }
//
//                        if (ammoResult.ammo() != null) {
//                            for(int i = 0; i < player.getInventory().size(); ++i) {
//                                ItemStack stack = player.getInventory().getStack(i);
//                                if (stack.isOf(ammoResult.ammo().getItem())) {
//                                    stack.decrement(1);
//                                    if (stack.isEmpty()) {
//                                        player.getInventory().removeOne(stack);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//
//                        if (spell.cost.effect_id != null) {
//                            StatusEffect effect = (StatusEffect) Registries.STATUS_EFFECT.get(new Identifier(spell.cost.effect_id));
//                            int newAmplifier = -1;
//                            StatusEffectInstance statusEffectInstance = player.getStatusEffect(effect);
//                            if (statusEffectInstance != null) {
//                                int oldAmplifier = statusEffectInstance.getAmplifier();
//                                newAmplifier = oldAmplifier - 1;
//                            }
//                            if (newAmplifier < 0) {
//                                player.removeStatusEffect(effect);
//                            } else {
//                                player.addStatusEffect(new StatusEffectInstance(effect, statusEffectInstance.getDuration(), newAmplifier, statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles(), statusEffectInstance.shouldShowIcon()), (Entity) caster);
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//    }
}
