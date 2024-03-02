package com.github.theredbrain.betteradventuremode;

import com.github.theredbrain.betteradventuremode.client.gui.screen.ingame.CraftingBenchBlockScreen;
import com.github.theredbrain.betteradventuremode.client.gui.screen.ingame.MannequinScreen;
import com.github.theredbrain.betteradventuremode.client.gui.screen.ingame.ShopBlockScreen;
import com.github.theredbrain.betteradventuremode.client.gui.screen.ingame.TeleporterBlockScreen;
import com.github.theredbrain.betteradventuremode.client.render.block.entity.*;
import com.github.theredbrain.betteradventuremode.client.render.renderer.MannequinEntityRenderer;
import com.github.theredbrain.betteradventuremode.registry.ClientPacketRegistry;
import com.github.theredbrain.betteradventuremode.client.render.renderer.SpawnerBoundEntityRenderer;
import com.github.theredbrain.betteradventuremode.config.ClientConfig;
import com.github.theredbrain.betteradventuremode.config.ClientConfigWrapper;
import com.github.theredbrain.betteradventuremode.registry.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;

import java.util.List;


public class BetterAdventureModeClient implements ClientModInitializer {

    public static ClientConfig clientConfig;

    @Override
    public void onInitializeClient() {
        // Config
        AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        clientConfig = ((ClientConfigWrapper)AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).client;

        // Packets
        ClientPacketRegistry.init();

        // Registry
        KeyBindingsRegistry.registerKeyBindings();
        registerTransparency();
        registerBlockEntityRenderer();
        registerEntityRenderer();
        registerScreens();
        registerModelPredicateProviders();
        registerColors();
        EventsRegistry.initializeClientEvents();
        registerCustomModelStatusEffectRenderers();
        registerSpellModels();
    }

    private void registerTransparency() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.INTERACTIVE_BERRY_BUSH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.INTERACTIVE_RED_MUSHROOM_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.INTERACTIVE_BROWN_MUSHROOM_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.BONFIRE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_OAK_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_IRON_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_SPRUCE_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_BIRCH_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_JUNGLE_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_ACACIA_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_CHERRY_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_DARK_OAK_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_MANGROVE_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_BAMBOO_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_CRIMSON_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_WARPED_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_OAK_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_IRON_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_SPRUCE_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_BIRCH_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_JUNGLE_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_ACACIA_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_CHERRY_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_DARK_OAK_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_MANGROVE_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_BAMBOO_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_CRIMSON_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.USE_RELAY_WARPED_TRAPDOOR, RenderLayer.getCutout());
    }

    private void registerBlockEntityRenderer() {
        BlockEntityRendererFactories.register(EntityRegistry.HOUSING_BLOCK_ENTITY, HousingBlockBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.MIMIC_BLOCK_ENTITY, MimicBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.AREA_BLOCK_ENTITY, StatusEffectApplierBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistry.TELEPORTER_BLOCK_ENTITY, TeleporterBlockBlockEntityRenderer::new);
    }

    private void registerEntityRenderer() {
        EntityRendererRegistry.register(EntityRegistry.MANNEQUIN_ENTITY, MannequinEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SPAWNER_BOUND_ENTITY, SpawnerBoundEntityRenderer::new);
    }

    private void registerScreens() {
        HandledScreens.register(ScreenHandlerTypesRegistry.CRAFTING_BENCH_BLOCK_SCREEN_HANDLER, CraftingBenchBlockScreen::new);
        HandledScreens.register(ScreenHandlerTypesRegistry.MANNEQUIN_SCREEN_HANDLER, MannequinScreen::new);
        HandledScreens.register(ScreenHandlerTypesRegistry.SHOP_BLOCK_SCREEN_HANDLER, ShopBlockScreen::new);
        HandledScreens.register(ScreenHandlerTypesRegistry.TELEPORTER_BLOCK_SCREEN_HANDLER, TeleporterBlockScreen::new);
    }

    private void registerColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem)((Object)stack.getItem())).getColor(stack), ItemRegistry.LEATHER_HELMET, ItemRegistry.LEATHER_CHESTPLATE, ItemRegistry.LEATHER_LEGGINGS, ItemRegistry.LEATHER_BOOTS, ItemRegistry.LEATHER_GLOVES, ItemRegistry.LEATHER_SHOULDERS);
    }

    private void registerModelPredicateProviders() {
        // shields
        ModelPredicateProviderRegistry.register(ItemRegistry.BUCKLER, new Identifier("blocking"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(ItemRegistry.PLANK_SHIELD, new Identifier("blocking"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(ItemRegistry.GREAT_SHIELD, new Identifier("blocking"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

        // spell proxies
        ModelPredicateProviderRegistry.register(ItemRegistry.ELEMENTAL_FLAME, new Identifier("charging"), (stack, world, entity, seed) -> isItemStackUsedForSpellCasting(stack, entity) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(ItemRegistry.ELEMENTAL_FLAME, new Identifier("charge"), (stack, world, entity, seed) -> {
            var progress = getItemStackSpellCastingProgress(stack, entity);
            if (progress != null) {
                return progress.ratio();
            }
            return 0.0F;
        });

        // spell scrolls
        ModelPredicateProviderRegistry.register(ItemRegistry.FIREBALL_SPELL_SCROLL, new Identifier("charging"), (stack, world, entity, seed) -> isItemStackUsedForSpellCasting(stack, entity) ? 1.0f : 0.0f);

        // food
        ModelPredicateProviderRegistry.register(ItemRegistry.BROWN_MUSHROOM, new Identifier("eating"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(ItemRegistry.BROWN_MUSHROOM, new Identifier("progress"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return (float) (stack.getMaxUseTime() + 3 - entity.getItemUseTimeLeft()) / (float) stack.getMaxUseTime();
            }
        });
    }

    private void registerCustomModelStatusEffectRenderers() {
//        CustomModelStatusEffect.register(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT, new WeaponsSheathedRenderer());
    }

    private void registerSpellModels() {
        CustomModels.registerModelIds(List.of(
                BetterAdventureMode.identifier("projectile/test_spell_projectile")
        ));
    }

    private static SpellCast.Progress getItemStackSpellCastingProgress(ItemStack itemStack, LivingEntity entity) {
        if (entity instanceof SpellCasterEntity caster && entity.getMainHandStack() == itemStack) {
            var process = caster.getSpellCastProcess();
            // Watch out! This condition check is duplicated
            if (process != null && process.spell().casting_animates_ranged_weapon) {
                return process.progress(entity.getWorld().getTime());
            }
        }
        return null;
    }

    private static boolean isItemStackUsedForSpellCasting(ItemStack itemStack, LivingEntity entity) {
        if (entity instanceof SpellCasterEntity caster && entity.getMainHandStack() == itemStack) {
            var process = caster.getSpellCastProcess();
            // Watch out! This condition check is duplicated
            return process != null && process.spell().casting_animates_ranged_weapon;
        }
        return false;
    }
}
