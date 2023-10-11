package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.effect.FoodStatusEffect;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreEntityAttributes;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.trinkets.*;
import dev.emi.trinkets.api.*;
import dev.emi.trinkets.mixin.accessor.ScreenHandlerAccessor;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.util.pond.OwoSlotExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.*;

@Environment(EnvType.CLIENT)
public class AdventureInventoryScreen extends BaseOwoHandledScreen<FlowLayout, PlayerScreenHandler/*AdventureInventoryScreenHandler*/> implements TrinketScreen {
    public static final Identifier INVENTORY_SLOT_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/inventory_slot_texture.png");
    public static final Identifier STATUS_EFFECT_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/status_effects_background.png");
    public static final Identifier CHARACTER_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/character_background_texture.png");
    public static final Identifier CUSTOM_WIDGETS_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/custom_widgets.png");
    public static final Identifier CUSTOM_STATUS_EFFECT_WIDGETS_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/custom_status_effect_widgets.png");
    private float mouseX;
    private float mouseY;
    private boolean showAttributeScreen = false;
    private boolean showStatusEffectsScreen = false;
    private int oldActiveSpellSlotAmount = 0;
    private int oldEffectsListSize = 0;
    private List<StatusEffectInstance> foodEffectsList = new ArrayList<>(Collections.emptyList());
    private List<StatusEffectInstance> negativeEffectsList = new ArrayList<>(Collections.emptyList());
    private List<StatusEffectInstance> positiveEffectsList = new ArrayList<>(Collections.emptyList());
    private List<StatusEffectInstance> neutralEffectsList = new ArrayList<>(Collections.emptyList());

    private List<Pair<SlotReference, ItemStack>> oldEquippedTrinkets = new ArrayList<>(Collections.emptyList());

    private Map<String, Integer> trinketSlotIds = new HashMap<>(Map.of());
    private Map<String, Integer> spellSlotIds = new HashMap<>(Map.of());

    public AdventureInventoryScreen(PlayerEntity player) {
        super(player.playerScreenHandler, player.getInventory(), Text.translatable("gui.adventureInventory"));
        this.populateTrinketSlotIds(player);
    }

    public void handledScreenTick() {
        PlayerEntity player = this.handler.player();
        this.updateAttributeScreen(player);
        this.buildSpellSlots(player);
        this.updateEffectsScreen(player);
        TrinketScreenManager.tick();
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    private void toggleAttributeScreen() {
        if (this.showAttributeScreen) {
            this.showAttributeScreen = false;
        } else {
            this.showAttributeScreen = true;
        }
        this.component(FlowLayout.class, "toggleAttributeScreenButtonContainer")
                .clearChildren()
                .child(
                        Components.wrapVanillaWidget(new PageTurnWidget(0, 0, !this.showAttributeScreen, button -> this.toggleAttributeScreen(), true))
                );
        if (this.showAttributeScreen) {
            this.component(FlowLayout.class, "additional_inventory_screen_right").clearChildren();
            this.buildAttributesScreen(this.handler.player());
//            this.component(FlowLayout.class, "additional_inventory_screen_right").child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fill(100)).surface(Surface.PANEL));
        } else {
            this.component(FlowLayout.class, "additional_inventory_screen_right").clearChildren();
        }
//                .setMessage(this.showAttributeScreen ? Text.translatable("gui.adventureInventory.toggleAttributeScreenButton.on")
//                        : Text.translatable("gui.adventureInventory.toggleAttributeScreenButton.off"));
//        this.component(ButtonComponent.class, "toggleAttributeScreenButton")
//                .tooltip(this.showAttributeScreen ? Text.translatable("gui.adventureInventory.toggleAttributeScreenButton.on.tooltip")
//                        : Text.translatable("gui.adventureInventory.toggleAttributeScreenButton.off.tooltip"));
    }

    private void updateAttributeScreen(PlayerEntity player) {
        if (this.showAttributeScreen) {
            this.component(LabelComponent.class, "attributes_max_health_value").text(Text.literal(String.valueOf((int)player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH))));
            this.component(LabelComponent.class, "attributes_health_regeneration_value").text(Text.literal(String.valueOf(player.getAttributeValue(BetterAdventureModeCoreEntityAttributes.HEALTH_REGENERATION))));
            this.component(LabelComponent.class, "attributes_armor_value").text(Text.literal(String.valueOf((int)player.getAttributeValue(EntityAttributes.GENERIC_ARMOR))));
            this.component(LabelComponent.class, "attributes_armor_toughness_value").text(Text.literal(String.valueOf(player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS))));
            this.component(LabelComponent.class, "attributes_max_stamina_value").text(Text.literal(String.valueOf((int)player.getAttributeValue(BetterAdventureModeCoreEntityAttributes.MAX_STAMINA))));
            this.component(LabelComponent.class, "attributes_stamina_regeneration_value").text(Text.literal(String.valueOf(player.getAttributeValue(BetterAdventureModeCoreEntityAttributes.STAMINA_REGENERATION))));
            this.component(LabelComponent.class, "attributes_max_mana_value").text(Text.literal(String.valueOf((int)((DuckPlayerEntityMixin)player).bamcore$getMaxMana())));
            this.component(LabelComponent.class, "attributes_mana_regeneration_value").text(Text.literal(String.valueOf(((DuckPlayerEntityMixin)player).bamcore$getManaRegeneration())));
            this.component(LabelComponent.class, "attributes_encumbrance_value").text(
                    Text.literal(String.valueOf((int)((DuckPlayerEntityMixin)player).bamcore$getEquipmentWeight()))
                    .append(Text.literal("/"))
                    .append(Text.literal(String.valueOf((int)((DuckPlayerEntityMixin)player).bamcore$getMaxEquipmentWeight())))
            );
//            this.component(LabelComponent.class, "attributes_max_poise_value").text(Text.literal(String.valueOf(player.getAttributeValue(EntityAttributesRegistry.MAX_POISE)))); // TODO poise
        }
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .id("root");
        rootComponent.child(Containers.horizontalFlow(Sizing.content(), Sizing.content())
                .children(List.of(
                        Containers.verticalFlow(Sizing.fixed(130), Sizing.fixed(228))
                                .id("additional_inventory_screen_left"),
                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                .children(List.of(
                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                .children(List.of(
                                                        Containers.verticalFlow(Sizing.fixed(87), Sizing.content())
                                                                .children(List.of(
                                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                                .children(List.of(
                                                                                        Components.label(Text.translatable("gui.adventureInventory.equipmentSlots"))
                                                                                                .color(Color.ofArgb(Colors.BLACK))
                                                                                                .margins(Insets.of(0, 4, 0, 0))
                                                                                )),
                                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                                .children(List.of( // TODO replace grid with verticalFlow
                                                                                        Containers.grid(Sizing.content(), Sizing.content(), 1, 1)
                                                                                                .child(slotAsComponent(46) // custom helmet
                                                                                                        .margins(Insets.of(1, 1, 1, 1)), 0, 0)
                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18)),
                                                                                        Containers.grid(Sizing.content(), Sizing.content(), 1, 1)
                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                        .id("necklaces_slot_container"), 0, 0)
                                                                                                .margins(Insets.of(0, 0, 1, 0))
                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                                ))
                                                                                .padding(Insets.of(0, 0, 25, 25))
                                                                                .id("equipment_slots_top_row"),
                                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                                .children(List.of(
                                                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                                                .children(List.of(
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("shoulders_slot_container"),
                                                                                                        slotAsComponent(47) // custom chestplate
                                                                                                                .margins(Insets.of(1, 1, 1, 1)),
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("belts_slot_container"),
                                                                                                        slotAsComponent(48) // custom leggings
                                                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                                ))
                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18)),
                                                                                        Containers.verticalFlow(Sizing.fixed(51), Sizing.fixed(72))
                                                                                                .surface(Surface.tiled(CHARACTER_BACKGROUND_TEXTURE, 51, 72)),
                                                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                                                .children(List.of(
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("rings_1_slot_container"),
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("rings_2_slot_container"),
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("gloves_slot_container"),
                                                                                                        slotAsComponent(49) // custom boots
                                                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                                ))
                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                                ))
                                                                                .id("equipment_slots_middle_row"),
                                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                                .children(List.of(
                                                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                                                .children(List.of( // TODO replace grid with verticalFlow
                                                                                                        Containers.grid(Sizing.content(), Sizing.content(), 1, 2)
                                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                        .id("main_hand_slot_container"), 0, 0)
                                                                                                                .child(slotAsComponent(50) // custom offhand
                                                                                                                         .margins(Insets.of(1, 1, 1, 1)), 0, 1)
                                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                                                )),
                                                                                        Containers.verticalFlow(Sizing.fixed(15), Sizing.fixed(18)),
                                                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                                                .children(List.of( // TODO replace grid with verticalFlow
                                                                                                        Containers.grid(Sizing.content(), Sizing.content(), 1, 2)
                                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                        .id("alternative_main_hand_slot_container"), 0, 0)
                                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                        .id("alternative_off_hand_slot_container"), 0, 1)
                                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                                                ))
                                                                                ))
                                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .id("equipment_slots_bottom_row")
                                                                ))
                                                                .id("equipment_slots_container_left"),
                                                        Containers.verticalFlow(Sizing.fixed(72), Sizing.content())
                                                                .children(List.of(
                                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                                .children(List.of(
                                                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                                                .child(
                                                                                                        Components.wrapVanillaWidget(new PageTurnWidget(0, 0, !this.showAttributeScreen, button -> this.toggleAttributeScreen(), true))
                                                                                                )
                                                                                                .id("toggleAttributeScreenButtonContainer")
                                                                                ))
                                                                                .horizontalAlignment(HorizontalAlignment.RIGHT),
                                                                        Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(54)),
                                                                        Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(18)).child(
                                                                                Components.label(Text.translatable("gui.adventureInventory.spellSlots"))
                                                                                        .color(Color.ofArgb(Colors.BLACK))
                                                                                        .margins(Insets.of(4, 5, 0, 0))
                                                                        ),
                                                                        Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(36))
                                                                                .id("spell_slots_container")
                                                                ))
                                                                .margins(Insets.of(0, 0, 3, 0))
                                                                .id("equipment_slots_container_right")
                                                )),
                                        Containers.horizontalFlow(Sizing.fixed(162), Sizing.content())
                                                .child(Components.label(this.title)
                                                        .color(Color.ofArgb(Colors.BLACK))
                                                        .margins(Insets.of(4, 0, 0, 0))),
                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                .children(List.of(
                                                        // main inventory
                                                        Containers.grid(Sizing.content(), Sizing.fixed(54), 3, 9)
                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                .margins(Insets.of(0, 4, 0, 0))
                                                                .id("main_inventory_slots_container"),
                                                        // hotbar
                                                        Containers.grid(Sizing.content(), Sizing.fixed(18), 1, 9)
                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                .id("hotbar_slots_container")
                                                ))
                                                .padding(Insets.of(4, 0, 0, 0))
                                                .id("inventory_slots_container")
                                ))
                                .padding(Insets.of(7, 7, 7, 7))
                                .surface(Surface.PANEL)
                                .id("main_inventory"),
                        Containers.verticalFlow(Sizing.fixed(130), Sizing.fixed(228))
                                .id("additional_inventory_screen_right")
                ))
        );

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                component(GridLayout.class, "main_inventory_slots_container").child(
                                slotAsComponent(j + (i + 1) * 9)
                                        .margins(Insets.of(1, 1, 1, 1)),
                                i,
                                j);
            }
        }
        for (int i = 0; i < 9; ++i) {
            component(GridLayout.class, "hotbar_slots_container").child(
                            slotAsComponent(i + 36)
                                    .margins(Insets.of(1, 1, 1, 1)),
                            0,
                            i);
        }

        // disable vanilla crafting slots
        ((OwoSlotExtension)handler.slots.get(0)).owo$setDisabledOverride(true);
        ((OwoSlotExtension)handler.slots.get(1)).owo$setDisabledOverride(true);
        ((OwoSlotExtension)handler.slots.get(2)).owo$setDisabledOverride(true);
        ((OwoSlotExtension)handler.slots.get(3)).owo$setDisabledOverride(true);
        ((OwoSlotExtension)handler.slots.get(4)).owo$setDisabledOverride(true);

        // disable vanilla armor slots
        ((OwoSlotExtension)handler.slots.get(5)).owo$setDisabledOverride(true);
        ((OwoSlotExtension)handler.slots.get(6)).owo$setDisabledOverride(true);
        ((OwoSlotExtension)handler.slots.get(7)).owo$setDisabledOverride(true);
        ((OwoSlotExtension)handler.slots.get(8)).owo$setDisabledOverride(true);

        // disable vanilla offhand slot
        ((OwoSlotExtension)handler.slots.get(45)).owo$setDisabledOverride(true);

//        this.buildTrinketSlots();
    }

    private void populateTrinketSlotIds(PlayerEntity player) {

        this.trinketSlotIds.clear();
        this.spellSlotIds.clear();
        int trinketSlotStart = ((TrinketPlayerScreenHandler) this.handler).trinkets$getTrinketSlotStart();
        int trinketSlotEnd = ((TrinketPlayerScreenHandler) this.handler).trinkets$getTrinketSlotEnd();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            for (int i = trinketSlotStart; i < trinketSlotEnd; i++) {
                Slot s = this.handler.slots.get(i);
                if (!(s instanceof SurvivalTrinketSlot ts)) {
                    continue;
                }
                SlotType type = ts.getType();
                if (Objects.equals(type.getGroup(), "alternative_main_hand") && Objects.equals(type.getName(), "alternative_main_hand")) {
                    this.trinketSlotIds.put("alternative_main_hand", i);
                } else if (Objects.equals(type.getGroup(), "alternative_off_hand") && Objects.equals(type.getName(), "alternative_off_hand")) {
                    this.trinketSlotIds.put("alternative_off_hand", i);
                } else if (Objects.equals(type.getGroup(), "belts") && Objects.equals(type.getName(), "belt")) {
                    this.trinketSlotIds.put("belts", i);
                } else if (Objects.equals(type.getGroup(), "gloves") && Objects.equals(type.getName(), "gloves")) {
                    this.trinketSlotIds.put("gloves", i);
                } else if (Objects.equals(type.getGroup(), "main_hand") && Objects.equals(type.getName(), "main_hand")) {
                    this.trinketSlotIds.put("main_hand", i);
                } else if (Objects.equals(type.getGroup(), "necklaces") && Objects.equals(type.getName(), "necklace")) {
                    this.trinketSlotIds.put("necklaces", i);
                } else if (Objects.equals(type.getGroup(), "rings_1") && Objects.equals(type.getName(), "ring")) {
                    this.trinketSlotIds.put("rings_1", i);
                } else if (Objects.equals(type.getGroup(), "rings_2") && Objects.equals(type.getName(), "ring")) {
                    this.trinketSlotIds.put("rings_2", i);
                } else if (Objects.equals(type.getGroup(), "shoulders") && Objects.equals(type.getName(), "shoulders")) {
                    this.trinketSlotIds.put("shoulders", i);
                } else if (Objects.equals(type.getGroup(), "spell_slot_1") && Objects.equals(type.getName(), "spell")) {
                    this.spellSlotIds.put("spell_slot_1", i);
                } else if (Objects.equals(type.getGroup(), "spell_slot_2") && Objects.equals(type.getName(), "spell")) {
                    this.spellSlotIds.put("spell_slot_2", i);
                } else if (Objects.equals(type.getGroup(), "spell_slot_3") && Objects.equals(type.getName(), "spell")) {
                    this.spellSlotIds.put("spell_slot_3", i);
                } else if (Objects.equals(type.getGroup(), "spell_slot_4") && Objects.equals(type.getName(), "spell")) {
                    this.spellSlotIds.put("spell_slot_4", i);
                } else if (Objects.equals(type.getGroup(), "spell_slot_5") && Objects.equals(type.getName(), "spell")) {
                    this.spellSlotIds.put("spell_slot_5", i);
                } else if (Objects.equals(type.getGroup(), "spell_slot_6") && Objects.equals(type.getName(), "spell")) {
                    this.spellSlotIds.put("spell_slot_6", i);
                } else if (Objects.equals(type.getGroup(), "spell_slot_7") && Objects.equals(type.getName(), "spell")) {
                    this.spellSlotIds.put("spell_slot_7", i);
                } else if (Objects.equals(type.getGroup(), "spell_slot_8") && Objects.equals(type.getName(), "spell")) {
                    this.spellSlotIds.put("spell_slot_8", i);
                }
            }
        }
    }

    private void buildTrinketSlots() {

        for (String key : this.trinketSlotIds.keySet()) {
            this.component(FlowLayout.class, key + "_slot_container").clearChildren();
            this.component(FlowLayout.class, key + "_slot_container").child(
                    slotAsComponent(this.trinketSlotIds.get(key))
                    .margins(Insets.of(1, 1, 1, 1))
            );
        }
    }

    private void buildAttributesScreen(PlayerEntity player) {
        this.component(FlowLayout.class, "additional_inventory_screen_right")
                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100))
                        .children(List.of(
                                Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                        .child(
                                                Components.label(Text.translatable("gui.adventureInventory.attributes"))
                                                        .color(Color.ofArgb(Colors.BLACK))
                                                        .margins(Insets.of(0, 0, 0, 0))
                                        ),
                                Containers.verticalScroll(Sizing.fill(100), Sizing.fixed(201),
                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                .children(List.of(
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("attribute.name.generic.max_health").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf((int)player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH))))
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_max_health_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0)),
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("attribute.name.generic.health_regeneration").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf(player.getAttributeValue(BetterAdventureModeCoreEntityAttributes.HEALTH_REGENERATION))))
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_health_regeneration_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0)),
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("attribute.name.generic.armor").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf((int)player.getAttributeValue(EntityAttributes.GENERIC_ARMOR))))
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_armor_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0)),
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("attribute.name.generic.armor_toughness").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf(player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS))))
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_armor_toughness_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0)),
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("attribute.name.generic.max_stamina").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf((int)player.getAttributeValue(BetterAdventureModeCoreEntityAttributes.MAX_STAMINA))))
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_max_stamina_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0)),
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("attribute.name.generic.stamina_regeneration").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf(player.getAttributeValue(BetterAdventureModeCoreEntityAttributes.STAMINA_REGENERATION))))
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_stamina_regeneration_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0)),
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("attribute.name.generic.max_mana").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf((int)((DuckPlayerEntityMixin)player).bamcore$getMaxMana())))
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_max_mana_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0)),
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("attribute.name.generic.mana_regeneration").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf(((DuckPlayerEntityMixin)player).bamcore$getManaRegeneration())))
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_mana_regeneration_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0)),
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("gui.adventureInventory.attributes.encumbrance").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf((int)((DuckPlayerEntityMixin)player).bamcore$getEquipmentWeight()))
                                                                                        .append(Text.literal("/"))
                                                                                        .append(Text.literal(String.valueOf((int)((DuckPlayerEntityMixin)player).bamcore$getMaxEquipmentWeight())))
                                                                                )
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_encumbrance_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0))/*,
                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content()) // TODO poise
                                                                .children(List.of(
                                                                        Components.label(Text.translatable("attribute.name.generic.max_poise").append(Text.literal(": ")))
                                                                                .color(Color.ofArgb(Colors.BLACK)),
                                                                        Components.label(Text.literal(String.valueOf(player.getAttributeValue(EntityAttributesRegistry.MAX_POISE))))
                                                                                .color(Color.ofArgb(Colors.BLACK)).id("attributes_max_poise_value")
                                                                ))
                                                                .margins(Insets.of(0, 2, 0, 0))*/
                                                ))
                                                .padding(Insets.of(2, 0, 2, 2))
                                        )
                                        .scrollbar(ScrollContainer.Scrollbar.vanillaFlat())
                        ))
                        .padding(Insets.of(7, 7, 7, 7))
                        .surface(Surface.PANEL)
                        .id("attributes"));
    }

    private void buildStatusEffectsScreen() {
        this.component(FlowLayout.class, "additional_inventory_screen_left").clearChildren();
        this.component(FlowLayout.class, "additional_inventory_screen_left")
                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100))
                        .children(List.of(
                                Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                        .child(
                                                Components.label(Text.translatable("gui.adventureInventory.status_effects"))
                                                        .color(Color.ofArgb(Colors.BLACK))
                                                        .margins(Insets.of(0, 4, 0, 0))
                                        ),
                                Containers.verticalScroll(Sizing.fill(100), Sizing.fixed(201),
                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                .children(List.of(
                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                .margins(Insets.of(0, 0, 0, 0))
                                                                .id("food_effects_panel"),
                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                .margins(Insets.of(0, 0, 0, 0))
                                                                .id("negative_effects_panel"),
                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                .margins(Insets.of(0, 0, 0, 0))
                                                                .id("positive_effects_panel"),
                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                .margins(Insets.of(0, 0, 0, 0))
                                                                .id("neutral_effects_panel")
                                                ))
                                                .padding(Insets.of(2, 0, 2, 2))
                                        )
                                        .scrollbar(ScrollContainer.Scrollbar.vanillaFlat())
                        ))
                        .padding(Insets.of(7, 7, 7, 7))
                        .surface(Surface.PANEL)
                        .id("status_effects"));
    }

    private void buildSpellSlots(PlayerEntity player) {

        int activeSpellSlotAmount = (int) player.getAttributeInstance(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT).getValue();
//        BetterAdventureModeCore.LOGGER.info("screen activeSpellSlotAmount: " + activeSpellSlotAmount);
//        BetterAdventureModeCore.LOGGER.info("this.oldActiveSpellSlotAmount: " + this.oldActiveSpellSlotAmount);
        if (this.oldActiveSpellSlotAmount != activeSpellSlotAmount) {

            component(FlowLayout.class, "spell_slots_container").clearChildren();

            component(FlowLayout.class, "spell_slots_container")
                    .child(Containers.grid(Sizing.fill(100), Sizing.fixed(36), 2, 4)
                            .id("spell_slots_container_grid"));

            // build active slots
            for (int i = 0; i < activeSpellSlotAmount; i++) {
                // enable active slots
//                ((OwoSlotExtension)this.handler.getSlot(this.spellSlotIds.get("spell_slot_" + (i + 1)))).owo$setDisabledOverride(false);
//                BetterAdventureModeCore.LOGGER.info("enabled spell slot: spell_slot_" + (i + 1));

                this.component(GridLayout.class, "spell_slots_container_grid")
                        .child(Containers.horizontalFlow(Sizing.fixed(18), Sizing.fixed(18))
//                                        .child(
//                                                slotAsComponent(this.spellSlotIds.get("spell_slot_" + (i + 1)))
//                                                        .margins(Insets.of(1, 1, 1, 1))
//                                        )
                                        .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                        .id("spell_slot_" + (i + 1)),
                                i < 4 ? 0 : 1,
                                i < 4 ? i : i - 4
                        );
            }

            // disable inactive slots
//            for (int i = activeSpellSlotAmount; i < 8; i++) {
//                ((OwoSlotExtension)this.handler.getSlot(this.spellSlotIds.get("spell_slot_" + (i + 1)))).owo$setDisabledOverride(true);
////                BetterAdventureModeCore.LOGGER.info("disabled spell slot: spell_slot_" + (i + 1));
//            }

            this.oldActiveSpellSlotAmount = activeSpellSlotAmount;
        }
    }

    private void buildEffectContainers(String effectCategory) {
        List<StatusEffectInstance> effectsList = Objects.equals(effectCategory, "food") ? this.foodEffectsList : Objects.equals(effectCategory, "negative") ? this.negativeEffectsList : Objects.equals(effectCategory, "positive") ? this.positiveEffectsList : this.neutralEffectsList;
        this.component(FlowLayout.class, effectCategory + "_effects_panel").clearChildren();
        this.component(FlowLayout.class, effectCategory + "_effects_panel")
                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                        .children(List.of(
                                Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                        .child(
                                                Components.label(Text.translatable("gui.adventureInventory.status_effects." + effectCategory + "_effects"))
                                                        .color(Color.ofArgb(Colors.BLACK))
                                                        .margins(Insets.of(4, 0, 0, 0))
                                        ),
                                Containers.verticalFlow(Sizing.fill(100), Sizing.content()).id(effectCategory + "_effects_container")
                        ))
                );
        int listSize = effectsList.size();
        int rowAmount = 1;
        if (listSize > 3) {
            rowAmount = (listSize - (listSize % 3)) / 3;
        }
        int j = 0;
        this.component(FlowLayout.class, effectCategory + "_effects_container")
                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .id(effectCategory + "_effects_panel_row_" + j));
        for (int i = 0; i < listSize; i++) {
            StatusEffect statusEffect = effectsList.get(i).getEffectType();
            this.component(FlowLayout.class, effectCategory + "_effects_panel_row_" + j)
                    .child(Containers.horizontalFlow(Sizing.fixed(32), Sizing.fixed(32))
                            .child(Components.texture(statusEffectTexture(statusEffect), 0, 0, 18, 18, 18,18))
                            .surface(Surface.tiled(STATUS_EFFECT_BACKGROUND_TEXTURE, 32, 32))
                            .horizontalAlignment(HorizontalAlignment.CENTER)
                            .verticalAlignment(VerticalAlignment.CENTER)
                            .margins(Insets.of(2, 2, 2, 2))
                            .tooltip(effectsList.get(i).getEffectType().getName().copy())
//                            .tooltip(TooltipComponent.of(getStatusEffectDescription(effectsList.get(i))))
                            .id(effectCategory + "_effect_container_" + i)
                    );
            if (i % 3 == 2) {
                j++;
                this.component(FlowLayout.class, effectCategory + "_effects_container")
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                .verticalAlignment(VerticalAlignment.CENTER)
                                .id(effectCategory + "_effects_panel_row_" + j));
            }
        }
    }

    private void updateEffectsScreen(PlayerEntity player) {
        List<StatusEffectInstance> effectsList = Ordering.natural().sortedCopy(player.getStatusEffects());
        List<StatusEffectInstance> visibleEffectsList = new ArrayList<>(Collections.emptyList());
        for (StatusEffectInstance statusEffectInstance : effectsList) {
            if (statusEffectInstance.shouldShowIcon()) {
                visibleEffectsList.add(statusEffectInstance);
            }
        }
        int visibleEffectsListSize = visibleEffectsList.size();
        if (visibleEffectsListSize == 0) {
            this.component(FlowLayout.class, "additional_inventory_screen_left").clearChildren();
            this.oldEffectsListSize = 0;
            return;
        }
        boolean bl = false;

        // determine if the UI should be updated
        if (visibleEffectsListSize != this.oldEffectsListSize) {
            bl = true;
            this.oldEffectsListSize = visibleEffectsListSize;
        }

        if (bl) {
            this.foodEffectsList.clear();
            this.negativeEffectsList.clear();
            this.positiveEffectsList.clear();
            this.neutralEffectsList.clear();

            for (StatusEffectInstance statusEffectInstance : visibleEffectsList) {
                if (statusEffectInstance.getEffectType() instanceof FoodStatusEffect) {
                    this.foodEffectsList.add(statusEffectInstance);
                } else if (statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
                    this.negativeEffectsList.add(statusEffectInstance);
                } else if (statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.BENEFICIAL) {
                    this.positiveEffectsList.add(statusEffectInstance);
                } else if (statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.NEUTRAL) {
                    this.neutralEffectsList.add(statusEffectInstance);
                }
            }
            this.buildStatusEffectsScreen();

            if (!this.foodEffectsList.isEmpty()) {
                buildEffectContainers("food");
            }
            if (!this.negativeEffectsList.isEmpty()) {
                buildEffectContainers("negative");
            }
            if (!this.positiveEffectsList.isEmpty()) {
                buildEffectContainers("positive");
            }
            if (!this.neutralEffectsList.isEmpty()) {
                buildEffectContainers("neutral");
            }
        } else {
            if (!this.foodEffectsList.isEmpty()) {
                updateEffectContainers("food");
            }
            if (!this.negativeEffectsList.isEmpty()) {
                updateEffectContainers("negative");
            }
            if (!this.positiveEffectsList.isEmpty()) {
                updateEffectContainers("positive");
            }
            if (!this.neutralEffectsList.isEmpty()) {
                updateEffectContainers("neutral");
            }
        }
        visibleEffectsList.clear();
    }

    private void updateEffectContainers(String effectCategory) {
        List<StatusEffectInstance> effectsList = Objects.equals(effectCategory, "food") ? this.foodEffectsList : Objects.equals(effectCategory, "negative") ? this.negativeEffectsList : Objects.equals(effectCategory, "positive") ? this.positiveEffectsList : this.neutralEffectsList;
        if (!effectsList.isEmpty()) {
            for (int i = 0; i < effectsList.size(); i++) {
                this.component(FlowLayout.class, effectCategory + "_effect_container_" + i).tooltip(getStatusEffectTooltip(effectsList.get(i)));
//                this.component(FlowLayout.class, effectCategory + "_effect_container_" + i).tooltip(getStatusEffectDescription(effectsList.get(i)));
            }
        }
    }

    private Identifier statusEffectTexture(StatusEffect statusEffect) {
        Identifier statusEffectId = Registries.STATUS_EFFECT.getId(statusEffect);
        if (statusEffectId != null) {
            return new Identifier(statusEffectId.getNamespace(), "textures/mob_effect/" + statusEffectId.getPath() + ".png");
        } else {
            return new Identifier("textures/mob_effect/luck.png");
        }
    }

    @Override
    protected void init() {
//        if (this.client.interactionManager.hasCreativeInventory()) {
//            // TODO call custom CreativeInventoryScreen
//            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
//            return;
//        }
        TrinketScreenManager.init(this);
        super.init();
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
//        TrinketScreenManager.drawActiveGroup(context);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TrinketScreenManager.update(mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        drawEntity(i + 51, j + 93, 30, (float)(i + 51) - this.mouseX, (float)(j + 93 - 50) - this.mouseY, this.client.player);
//        TrinketScreenManager.drawExtraGroups(context);
    }

    public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float)Math.atan(mouseX / 40.0f);
        float g = (float)Math.atan(mouseY / 40.0f);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x, y -18 /* -18 to negate an offset on the OwoScreen I can't find the cause of*/, 1050.0f);
        matrixStack.scale(1.0f, 1.0f, -1.0f);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0f, 0.0f, 1000.0f);
        matrixStack2.scale(size, size, size);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float)Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(g * 20.0f * ((float)Math.PI / 180));
        quaternionf.mul(quaternionf2);
        matrixStack2.multiply(quaternionf);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0f + f * 20.0f;
        entity.setYaw(180.0f + f * 40.0f);
        entity.setPitch(-g * 20.0f);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternionf2.conjugate();
        entityRenderDispatcher.setRotation(quaternionf2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, matrixStack2, immediate, 0xF000F0));
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }

    @Override
    public void drawSlot(DrawContext context, Slot slot) {
        super.drawSlot(context, slot);

        // draw slot overlay
        ItemStack stack = slot.getStack();
        if (!stack.isEmpty() && stack.isDamageable() && stack.getDamage() >= stack.getMaxDamage() - 1) {
            drawDisabledItemSlotHighlight(context, slot.x, slot.y, 0);
        }
    }

    public static void drawDisabledItemSlotHighlight(DrawContext context, int x, int y, int z) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, -1602211792, -1602211792, z);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }

    private List<Text> getStatusEffectTooltip(StatusEffectInstance statusEffect) {
        List<Text> list = List.of(this.getStatusEffectTitle(statusEffect));
//        list.addAll(this.getStatusEffectDescription(statusEffect));
//        list.add(TooltipComponent.of((OrderedText) StatusEffectUtil.getDurationText(statusEffect, 1.0f)));
//        BetterAdventureModeCore.LOGGER.info(list.toString());
        return list;
    }

    private OrderedText getStatusEffectDescription(StatusEffectInstance statusEffect) {
        Text text = statusEffect.getEffectType().getName().copy();
//        if (statusEffect.getAmplifier() >= 1 && statusEffect.getAmplifier() <= 9) {
//            text.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + (statusEffect.getAmplifier() + 1)));
//        }
        return (OrderedText) text;
    }

    private Text getStatusEffectTitle(StatusEffectInstance statusEffect) {
        Text text = statusEffect.getEffectType().getName().copy();
//        if (statusEffect.getAmplifier() >= 1 && statusEffect.getAmplifier() <= 9) {
//            text.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + (statusEffect.getAmplifier() + 1)));
//        }
        return text;

//        MutableText mutableText = statusEffect.getEffectType().getName().copy();
//        if (statusEffect.getAmplifier() >= 1 && statusEffect.getAmplifier() <= 9) {
//            MutableText var10000 = mutableText.append(ScreenTexts.SPACE);
//            int var10001 = statusEffect.getAmplifier();
//            var10000.append(Text.translatable("enchantment.level." + (var10001 + 1)));
//        }
//        return mutableText.formatted(Formatting.LIGHT_PURPLE);
    }

    @Override
    public TrinketPlayerScreenHandler trinkets$getHandler() {
        return ((TrinketPlayerScreenHandler)this.handler);
    }

//    @Override
//    public Rect2i trinkets$getGroupRect(SlotGroup group) {
//        int order = group.getOrder();
//        return switch (order) {
//            case 1 -> new Rect2i(59, 80, 17, 17); // alternative main hand
//            case 2 -> new Rect2i(77, 80, 17, 17); // alternative off hand
//            case 3 -> new Rect2i(8, 44, 17, 17); // belts
//            case 4 -> new Rect2i(77, 44, 17, 17); // gloves
//            case 5 -> new Rect2i(8, 80, 17, 17); // main hand
//            case 6 -> new Rect2i(52, -10, 17, 17); // necklaces
//            case 7 -> new Rect2i(77, 8, 17, 17); // rings 1
//            case 8 -> new Rect2i(77, 26, 17, 17); // rings 2
//            case 9 -> new Rect2i(8, 8, 17, 17); // shoulders
//            case 10 -> new Rect2i(98, 62, 17, 17); // spell slot 1
//            case 11 -> new Rect2i(98 + 18, 62, 17, 17); // spell slot 2
//            case 12 -> new Rect2i(98 + 36, 62, 17, 17); // spell slot 3
//            case 13 -> new Rect2i(98 + 54, 62, 17, 17); // spell slot 4
//            case 14 -> new Rect2i(98, 62 + 18, 17, 17); // spell slot 5
//            case 15 -> new Rect2i(98 + 18, 62 + 18, 17, 17); // spell slot 6
//            case 16 -> new Rect2i(98 + 36, 62 + 18, 17, 17); // spell slot 7
//            case 17 -> new Rect2i(98 + 54, 62 + 18, 17, 17); // spell slot 8
//            default -> new Rect2i(0, 0, 0, 0);
//        };
//    }

    @Override
    public Rect2i trinkets$getGroupRect(SlotGroup group) {
        Point pos = ((TrinketPlayerScreenHandler) handler).trinkets$getGroupPos(group);
        if (pos != null) {
            return new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17);
        }
        return new Rect2i(0, 0, 0, 0);
    }

    @Override
    public Slot trinkets$getFocusedSlot() {
        return this.focusedSlot;
    }

    @Override
    public int trinkets$getX() {
        return this.x;
    }

    @Override
    public int trinkets$getY() {
        return this.y;
    }

    @Override
    public void trinkets$updateTrinketSlots() {
        // TODO update trinket slot backgrounds
//        TrinketsApi.getTrinketComponent(this.handler.player()).ifPresent(trinkets -> {
////                    trinkets.update();
//                    Map<String, SlotGroup> groups = trinkets.getGroups();
//
//                    // clears previous groups
////            ((TrinketPlayerScreenHandler)this.handler).groupPos.clear();
////            while (trinketSlotStart < trinketSlotEnd) {
////                slots.remove(trinketSlotStart);
////                ((ScreenHandlerAccessor) (this)).getTrackedStacks().remove(trinketSlotStart);
////                ((ScreenHandlerAccessor) (this)).getPreviousTrackedStacks().remove(trinketSlotStart);
////                trinketSlotEnd--;
////            }
//
//            for (Map.Entry<String, Map<String, TrinketInventory>> entry : trinkets.getInventory().entrySet()) {
//                String groupId = entry.getKey();
//                SlotGroup group = groups.get(groupId);
//                int groupOffset = 1;
//
//                if (group.getSlotId() != -1) {
//                    groupOffset++;
//                }
//                int width = 0;
////                Point pos = trinkets$getGroupPos(group);
//                Rect2i rect2i = trinkets$getGroupRect(group);
//                if (rect2i == null) {
//                    continue;
//                }
//                for (Map.Entry<String, TrinketInventory> slot : entry.getValue().entrySet().stream().sorted((a, b) ->
//                        Integer.compare(a.getValue().getSlotType().getOrder(), b.getValue().getSlotType().getOrder())).toList()) {
//                    TrinketInventory stacks = slot.getValue();
//                    if (stacks.size() == 0) {
//                        continue;
//                    }
//                    int slotOffset = 1;
////                    int x = (int) ((groupOffset / 2) * 18 * Math.pow(-1, groupOffset));
////                    slotHeights.computeIfAbsent(group, (k) -> new ArrayList<>()).add(new Point(x, stacks.size()));
////                    slotTypes.computeIfAbsent(group, (k) -> new ArrayList<>()).add(stacks.getSlotType());
//                    for (int i = 0; i < stacks.size(); i++) {
////                        int y = (int) (rect2i..y() + (slotOffset / 2) * 18 * Math.pow(-1, slotOffset));
//                        this.handler.slots.add(new SurvivalTrinketSlot(stacks, i, x + rect2i.getX(), y + rect2i.getY(), group, stacks.getSlotType(), i, groupOffset == 1 && i == 0));
////                        slotOffset++;
//                    }
////                    groupOffset++;
////                    width++;
//                }
////                slotWidths.put(group, width);
//            }
//        });
    }
//            int groupNum = 1; // Start at 1 because offhand exists

            // ----------------------------------------
//            if (true) {
//                for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
//                    if (!hasSlots(trinkets, group)) {
//                        continue;
//                    }
//                    int order = group.getOrder();
//                    int id = group.getSlotId();
//                    if (id != -1) {
////                        if (this.slots.size() > id) {
////                            Slot slot = this.slots.get(id);
////                            if (!(slot instanceof SurvivalTrinketSlot)) {
////                                groupPos.put(group, new Point(slot.x, slot.y));
////                                groupNums.put(group, -id);
////                            }
////                        }
//                        continue;
//                    } else {
//                        int x;
//                        int y;
//                        // begins at 1 because order=0 is the default
//                        // this way most unwanted cases are avoided
//                        if (order == 1) {
//                            // alternative main hand
//                            x = 59;
//                            y = 80;//98; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                        } else if (order == 2) {
//                            // alternative off hand
//                            x = 77;
//                            y = 80;//98; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                        } else if (order == 3) {
//                            // belts
//                            x = 8;
//                            y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                        } else if (order == 4) {
//                            // gloves
//                            x = 77;
//                            y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                        } else if (order == 5) {
//                            // main hand
//                            x = 8;
//                            y = 80;//98; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                        } else if (order == 6) {
//                            // necklaces
//                            x = 52;
//                            y = -10;//8; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                        } else if (order == 7) {
//                            // rings 1
//                            x = 77;
//                            y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                        } else if (order == 8) {
//                            // rings 2
//                            x = 77;
//                            y = 26;//44; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                        } else if (order == 9) {
//                            // shoulders
//                            x = 8;
//                            y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                        } else if (order == 10) {
//                            // spell slot 1
//                            x = this.spellSlotsX;
//                            y = this.spellSlotsY;
//
//                        } else if (order == 11) {
//                            // spell slot 2
//                            x = this.spellSlotsX + 18;
//                            y = this.spellSlotsY;
//
//                        } else if (order == 12) {
//                            // spell slot 3
//                            x = this.spellSlotsX + 36;
//                            y = this.spellSlotsY;
//
//                        } else if (order == 13) {
//                            // spell slot 4
//                            x = this.spellSlotsX + 54;
//                            y = this.spellSlotsY;
//
//                        } else if (order == 14) {
//                            // spell slot 5
//                            x = this.spellSlotsX;
//                            y = this.spellSlotsY + 18;
//
//                        } else if (order == 15) {
//                            // spell slot 6
//                            x = this.spellSlotsX + 18;
//                            y = this.spellSlotsY + 18;
//
//                        } else if (order == 16) {
//                            // spell slot 7
//                            x = this.spellSlotsX + 36;
//                            y = this.spellSlotsY + 18;
//
//                        } else if (order == 17) {
//                            // spell slot 8
//                            x = this.spellSlotsX + 54;
//                            y = this.spellSlotsY + 18;
//
//                        } else {
////                            if (groupNum >= 4) {
////                                x = 4 - (groupNum / 4) * 18;
////                                y = 8 + (groupNum % 4) * 18;
////                            } else {
////                                x = 77;
////                                y = 62 - groupNum * 18;
////                            }
//                            continue;
//                        }
//                        groupPos.put(group, new Point(x, y));
//                        groupNums.put(group, groupNum);
//                        groupNum++;
//                    }
//                }
//            }
//    }

//    @Override
//    public void trinkets$updateTrinketSlots() {
//        TrinketScreen.super.trinkets$updateTrinketSlots();
//    }

//    private List<Text> getStatusEffectDescription(StatusEffectInstance statusEffect) {
////        if (statusEffect.getEffectType() instanceof FoodStatusEffect) {
////            List<Text> list = new ArrayList<>(List.of());
//////            List<Text> list = Lists.newArrayList();
////            for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : statusEffect.getEffectType().getAttributeModifiers().entrySet()) {
////                EntityAttributeInstance entityAttributeInstance = this.client.player.getAttributes().getCustomInstance(entry.getKey());
////                if (entityAttributeInstance == null) continue;
////                // TODO look at equipment tooltip
//////                EntityAttributeModifier entityAttributeModifier = entry.getValue();
//////                entityAttributeInstance.removeModifier(entityAttributeModifier);
////            }
////            list.add(Text.translatable(statusEffect.getEffectType().getTranslationKey() + ".description"));
////            return list;
////        }
//        return List.of(Text.translatable(statusEffect.getEffectType().getTranslationKey() + ".description"));
//    }
}
