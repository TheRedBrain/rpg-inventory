package com.github.theredbrain.betteradventuremode.mixin.spell_engine.internals;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellCastAttemptMixin;
import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellCostMixin;
import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellImpactActionDamageMixin;
import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellImpactActionHealMixin;
import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.Function;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.effect.EntityImmunity;
import net.spell_engine.api.entity.SpellSpawnedEntity;
import net.spell_engine.api.spell.CustomSpellHandler;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.entity.ConfigurableKnockback;
import net.spell_engine.internals.*;
import net.spell_engine.internals.arrow.ArrowHelper;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.AnimationHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(SpellHelper.class)
public abstract class SpellHelperMixin {

    @Shadow
    private static void beamImpact(World world, LivingEntity caster, List<Entity> targets, SpellInfo spellInfo, SpellHelper.ImpactContext context) {
        throw new AssertionError();
    }

    @Shadow
    private static void applyAreaImpact(World world, LivingEntity caster, List<Entity> targets, float range, Spell.Release.Target.Area area, SpellInfo spellInfo, SpellHelper.ImpactContext context, boolean additionalTargetLookup) {
        throw new AssertionError();
    }

    @Shadow
    private static void directImpact(World world, LivingEntity caster, Entity target, SpellInfo spellInfo, SpellHelper.ImpactContext context) {
        throw new AssertionError();
    }

    /**
     * @author TheRedBrain
     * @reason check for custom cost
     */
    @Overwrite
    public static SpellCast.Attempt attemptCasting(PlayerEntity player, ItemStack itemStack, Identifier spellId, boolean checkAmmo) {
        SpellCasterEntity caster = (SpellCasterEntity)player;
        Spell spell = SpellRegistry.getSpell(spellId);
        if (spell == null) {
            return SpellCast.Attempt.none();
        } else if (caster.getCooldownManager().isCoolingDown(spellId)) {
            return SpellCast.Attempt.failOnCooldown(new SpellCast.Attempt.OnCooldownInfo());
        } else {
            if (checkAmmo) {
                SpellHelper.AmmoResult ammoResult = SpellHelper.ammoForSpell(player, spell, itemStack);
                if (!ammoResult.satisfied()) {
                    return SpellCast.Attempt.failMissingItem(new SpellCast.Attempt.MissingItemInfo(ammoResult.ammo().getItem()));
                }
            }
            if (((DuckSpellCostMixin) spell.cost).betteradventuremode$checkHealthCost()) {
                float healthCost = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getHealthCost();
                if (healthCost > 0 && healthCost > player.getHealth()) {
                    return DuckSpellCastAttemptMixin.failCustom();
                }
            }
            if (((DuckSpellCostMixin) spell.cost).betteradventuremode$checkManaCost()) {
                float manaCost = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getManaCost();
                if (manaCost > 0 && manaCost > ((DuckLivingEntityMixin) player).betteradventuremode$getMana()) {
                    return DuckSpellCastAttemptMixin.failCustom();
                }
            }
            if (((DuckSpellCostMixin) spell.cost).betteradventuremode$checkStaminaCost()) {
                float staminaCost = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getStaminaCost();
                if (staminaCost > 0 && staminaCost > ((DuckLivingEntityMixin) player).betteradventuremode$getStamina()) {
                    return DuckSpellCastAttemptMixin.failCustom();
                }
            }
            if (spell.cost.effect_id != null && ((DuckSpellCostMixin) spell.cost).betteradventuremode$checkSpellEffectCost()) {
                StatusEffect effect = (StatusEffect) Registries.STATUS_EFFECT.get(new Identifier(spell.cost.effect_id));
                if (effect != null && !player.hasStatusEffect(effect)) {
                    return DuckSpellCastAttemptMixin.failCustom();
                }
            }
            return SpellCast.Attempt.success();
        }
    }

    /**
     * @author TheRedBrain
     * @reason integrate health cost, mana cost, stamina cost, reducing amplifier of status effect cost instead of removing them, self consuming of casting item
     */
    @Overwrite
    public static void performSpell(World world, PlayerEntity player, Identifier spellId, List<Entity> targets, SpellCast.Action action, float progress) {
        if (!player.isSpectator()) {
            Spell spell = SpellRegistry.getSpell(spellId);
            if (spell != null) {
                SpellInfo spellInfo = new SpellInfo(spell, spellId);
                ItemStack itemStack = player.getMainHandStack();
                SpellCast.Attempt attempt = SpellHelper.attemptCasting(player, itemStack, spellId);
                if (attempt.isSuccess()) {
                    float castingSpeed = ((SpellCasterEntity) player).getCurrentCastingSpeed();
                    progress = Math.max(Math.min(progress, 1.0F), 0.0F);
                    float channelMultiplier = 1.0F;
                    boolean shouldPerformImpact = true;
                    Supplier<Collection<ServerPlayerEntity>> trackingPlayers = Suppliers.memoize(() -> {
                        return PlayerLookup.tracking(player);
                    });
                    switch (action) {
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

                            SpellCastSyncHelper.clearCasting(player);
                    }

                    SpellHelper.AmmoResult ammoResult = SpellHelper.ammoForSpell(player, spell, itemStack);
                    if (channelMultiplier > 0.0F && ammoResult.satisfied()) {
                        Spell.Release.Target targeting = spell.release.target;
                        boolean released = action == SpellCast.Action.RELEASE;
                        if (shouldPerformImpact) {
                            SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(channelMultiplier, 1.0F, (Vec3d) null, SpellPower.getSpellPower(spell.school, player), SpellHelper.impactTargetingMode(spell));
                            if (spell.release.custom_impact) {
                                Function<CustomSpellHandler.Data, Boolean> handler = (Function) CustomSpellHandler.handlers.get(spellId);
                                released = false;
                                if (handler != null) {
                                    released = (Boolean) handler.apply(new CustomSpellHandler.Data(player, targets, itemStack, action, progress, context));
                                }
                            } else {
                                Optional optionalTarget;
                                Entity targetEntity = null;
                                switch (targeting.type) {
                                    case AREA:
                                        Vec3d center = player.getPos().add(0.0, (double) (player.getHeight() / 2.0F), 0.0);
                                        Spell.Release.Target.Area area = spell.release.target.area;
                                        applyAreaImpact(world, player, targets, spell.range, area, spellInfo, context.position(center), true);
                                        break;
                                    case BEAM:
                                        beamImpact(world, player, targets, spellInfo, context);
                                        break;
                                    case CLOUD:
                                        SpellHelper.placeCloud(world, player, spellInfo, context);
                                        released = true;
                                        break;
                                    case CURSOR:
                                        optionalTarget = targets.stream().findFirst();
                                        if (optionalTarget.isPresent()) {
                                            directImpact(world, player, (Entity) optionalTarget.get(), spellInfo, context);
                                        } else {
                                            released = false;
                                        }
                                        break;
                                    case PROJECTILE:
                                        Optional<Entity> entityFound = targets.stream().findFirst();
                                        if (entityFound.isPresent()) {
                                            targetEntity = (Entity) entityFound.get();
                                        }

                                        SpellHelper.shootProjectile(world, player, targetEntity, spellInfo, context);
                                        break;
                                    case METEOR:
                                        optionalTarget = targets.stream().findFirst();
                                        if (optionalTarget.isPresent()) {
                                            SpellHelper.fallProjectile(world, player, (Entity) optionalTarget.get(), spellInfo, context);
                                        } else {
                                            released = false;
                                        }
                                        break;
                                    case SELF:
                                        directImpact(world, player, player, spellInfo, context);
                                        released = true;
                                        break;
                                    case SHOOT_ARROW:
                                        ArrowHelper.shootArrow(world, player, spellInfo, context);
                                        released = true;
                                }
                            }
                        }

                        if (released) {
                            ParticleHelper.sendBatches(player, spell.release.particles);
                            SoundHelper.playSound(world, player, spell.release.sound);
                            AnimationHelper.sendAnimation(player, (Collection) trackingPlayers.get(), SpellCast.Animation.RELEASE, spell.release.animation, castingSpeed);
                            SpellHelper.imposeCooldown(player, spellId, spell, progress);
                            player.addExhaustion(spell.cost.exhaust * SpellEngineMod.config.spell_cost_exhaust_multiplier);

                            // health cost
                            float healthCost = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getHealthCost();
                            if (healthCost > 0.0F) {
                                player.damage(((DuckDamageSourcesMixin) player.getDamageSources()).betteradventuremode$bloodMagicCasting(), healthCost);
                            }

                            // mana cost
                            float manaCost = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getManaCost();
                            if (manaCost > 0.0F) {
                                ((DuckLivingEntityMixin) player).betteradventuremode$addMana(-manaCost);
                            }

                            // stamina cost
                            float staminaCost = ((DuckSpellCostMixin) spell.cost).betteradventuremode$getStaminaCost();
                            if (staminaCost > 0.0F) {
                                ((DuckLivingEntityMixin) player).betteradventuremode$addStamina(-staminaCost);
                            }

                            // consume spell casting item (used for spell scrolls)
                            if (((DuckSpellCostMixin) spell.cost).betteradventuremode$consumeSelf() && !player.isCreative()) {
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

                            if (ammoResult.ammo() != null && spell.cost.consume_item) {
                                for (int i = 0; i < player.getInventory().size(); ++i) {
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

                            if (spell.cost.effect_id != null) {
                                StatusEffect effect = (StatusEffect) Registries.STATUS_EFFECT.get(new Identifier(spell.cost.effect_id));
                                if (effect != null) {
                                    if (((DuckSpellCostMixin) spell.cost).betteradventuremode$getDecrementEffectAmount() < 0) {
                                        player.removeStatusEffect(effect);
                                    } else if (((DuckSpellCostMixin) spell.cost).betteradventuremode$getDecrementEffectAmount() > 0) {
                                        int newAmplifier = -1;
                                        StatusEffectInstance statusEffectInstance = player.getStatusEffect(effect);
                                        if (statusEffectInstance != null) {
                                            int oldAmplifier = statusEffectInstance.getAmplifier();
                                            newAmplifier = oldAmplifier - 1;
                                        }
                                        player.removeStatusEffect(effect);
                                        if (newAmplifier >= 0) {
                                            player.addStatusEffect(new StatusEffectInstance(effect, statusEffectInstance.getDuration(), newAmplifier, statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles(), statusEffectInstance.shouldShowIcon()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @author TheRedBrain
     * @reason integrate direct damage, direct healing and damage type override
     */
    @Overwrite
    private static boolean performImpact(World world, LivingEntity caster, Entity target, SpellInfo spellInfo, Spell.Impact impact, SpellHelper.ImpactContext context, Collection<ServerPlayerEntity> trackers) {
        if (target instanceof EnderDragonPart) {
            target = ((EnderDragonPart)target).owner;
        }

        if (!((Entity)target).isAttackable()) {
            return false;
        } else {
            boolean success = false;
            boolean isKnockbackPushed = false;
            Spell spell = spellInfo.spell();

            try {
                double particleMultiplier = (double)(1.0F * context.total());
                SpellPower.Result power = context.power();
                MagicSchool school = impact.school != null ? impact.school : spell.school;
                if (power == null || power.school() != school) {
                    power = SpellPower.getSpellPower(school, caster);
                }

                if (power.baseValue() < (double)impact.action.min_power) {
                    power = new SpellPower.Result(power.school(), (double)impact.action.min_power, power.criticalChance(), power.criticalDamage());
                }

                if (impact.action.apply_to_caster) {
                    target = caster;
                }

                TargetHelper.Intent intent = SpellHelper.intent(impact.action);
                if (!TargetHelper.actionAllowed(context.targetingMode(), intent, caster, (Entity)target)) {
                    return false;
                }

                if (intent == TargetHelper.Intent.HARMFUL && context.targetingMode() == TargetHelper.TargetingMode.AREA && ((EntityImmunity)target).isImmuneTo(EntityImmunity.Type.AREA_EFFECT)) {
                    return false;
                }

                LivingEntity livingTarget;
                label141:
                switch (impact.action.type) {
                    case DAMAGE:
                        Spell.Impact.Action.Damage damageData = impact.action.damage;
                        float knockbackMultiplier = Math.max(0.0F, damageData.knockback * context.total());
                        SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;
                        int timeUntilRegen = ((Entity)target).timeUntilRegen;
                        if (target instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity)target;
                            ((ConfigurableKnockback)livingEntity).pushKnockbackMultiplier_SpellEngine(context.hasOffset() ? 0.0F : knockbackMultiplier);
                            isKnockbackPushed = true;
                            if (damageData.bypass_iframes && SpellEngineMod.config.bypass_iframes) {
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

                        // direct damage
                        double directDamageAmount = ((DuckSpellImpactActionDamageMixin) damageData).betteradventuremode$getDirectDamage();
                        if (directDamageAmount > 0.0) {
                            amount = directDamageAmount;
                        }

                        particleMultiplier = power.criticalDamage() + (double)vulnerability.criticalDamageBonus();
                        caster.onAttacking((Entity)target);

                        // damage type override
                        DamageSource damageSource = null;
                        String damageTypeOverride = ((DuckSpellImpactActionDamageMixin) damageData).betteradventuremode$getDamageTypeOverride();
                        if (!damageTypeOverride.equals("") && Identifier.isValid(damageTypeOverride)) {
                            Identifier damageTypeOverrideId = Identifier.tryParse(damageTypeOverride);
                            if (damageTypeOverrideId != null) {
                                RegistryKey<DamageType> key = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, damageTypeOverrideId);
                                Registry<DamageType> registry = ((DamageSourcesAccessor)caster.getDamageSources()).getRegistry();
                                damageSource =  new DamageSource(registry.entryOf(key), caster);
                            }
                        }
                        if (damageSource == null) {
                            damageSource =  SpellDamageSource.create(school, caster);
                        }
                        ((Entity)target).damage(damageSource, (float)amount);

                        if (target instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity)target;
                            ((ConfigurableKnockback)livingEntity).popKnockbackMultiplier_SpellEngine();
                            isKnockbackPushed = false;
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
                            livingTarget = (LivingEntity)target;
                            Spell.Impact.Action.Heal healData = impact.action.heal;
                            particleMultiplier = power.criticalDamage();
                            double healAmount = power.randomValue();
                            healAmount *= (double)healData.spell_power_coefficient;
                            healAmount *= (double)context.total();

                            // direct heal
                            double directHealAmount = ((DuckSpellImpactActionHealMixin) healData).betteradventuremode$getDirectHeal();
                            if (directHealAmount > 0) {
                                healAmount = directHealAmount;
                            }

                            if (context.isChanneled()) {
                                healAmount *= SpellPower.getHaste(caster);
                            }
                            livingTarget.heal((float)healAmount);
                            success = true;
                        }
                        break;
                    case STATUS_EFFECT:
                        Spell.Impact.Action.StatusEffect data = impact.action.status_effect;
                        if (target instanceof LivingEntity) {
                            livingTarget = (LivingEntity)target;
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
                        break;
                    case SPAWN:
                        List spawns;
                        if (impact.action.spawns.length > 0) {
                            spawns = List.of(impact.action.spawns);
                        } else {
                            spawns = List.of(impact.action.spawn);
                        }

                        Iterator var28 = spawns.iterator();

                        while(true) {
                            if (!var28.hasNext()) {
                                break label141;
                            }

                            Spell.Impact.Action.Spawn spawnData = (Spell.Impact.Action.Spawn)var28.next();
                            Identifier id = new Identifier(spawnData.entity_type_id);
                            EntityType<?> type = (EntityType)Registries.ENTITY_TYPE.get(id);
                            Entity entity = type.create(world);
                            SpellHelper.applyEntityPlacement(entity, caster, ((Entity)target).getPos(), spawnData.placement);
                            if (entity instanceof SpellSpawnedEntity) {
                                SpellSpawnedEntity spellSpawnedEntity = (SpellSpawnedEntity)entity;
                                spellSpawnedEntity.onCreatedFromSpell(caster, spellInfo.id(), spawnData);
                            }

                            ((WorldScheduler)world).schedule(spawnData.delay_ticks, () -> {
                                world.spawnEntity(entity);
                            });
                            success = true;
                        }
                    case TELEPORT:
                        Spell.Impact.Action.Teleport teleportData = impact.action.teleport;
                        if (target instanceof LivingEntity) {
                            livingTarget = (LivingEntity)target;
                            switch (teleportData.mode) {
                                case FORWARD:
                                    Spell.Impact.Action.Teleport.Forward forward = teleportData.forward;
                                    Vec3d look = ((Entity)target).getRotationVector();
                                    Vec3d startingPosition = ((Entity)target).getPos();
                                    Vec3d destination = TargetHelper.findTeleportDestination(livingTarget, look, forward.distance, teleportData.required_clearance_block_y);
                                    Vec3d groundJustBelow = TargetHelper.findSolidBlockBelow(livingTarget, destination, ((Entity)target).getWorld(), -1.5F);
                                    if (groundJustBelow != null) {
                                        destination = groundJustBelow;
                                    }

                                    if (destination != null) {
                                        ParticleHelper.sendBatches(livingTarget, teleportData.depart_particles, false);
                                        world.emitGameEvent(GameEvent.TELEPORT, startingPosition, GameEvent.Emitter.of((Entity)target));
                                        livingTarget.teleport(destination.x, destination.y, destination.z);
                                        success = true;
                                    }
                            }
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
                if (isKnockbackPushed) {
                    ((ConfigurableKnockback)target).popKnockbackMultiplier_SpellEngine();
                }
            }

            return success;
        }
    }
}
