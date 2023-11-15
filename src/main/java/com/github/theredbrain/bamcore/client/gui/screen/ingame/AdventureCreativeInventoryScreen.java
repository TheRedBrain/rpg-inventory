package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import dev.emi.trinkets.Point;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketScreen;
import dev.emi.trinkets.api.SlotGroup;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroups;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class AdventureCreativeInventoryScreen extends BaseOwoHandledScreen<FlowLayout, CreativeInventoryScreen.CreativeScreenHandler> implements TrinketScreen {

    private final boolean operatorTabEnabled;
    public AdventureCreativeInventoryScreen(PlayerEntity player, FeatureSet enabledFeatures, boolean operatorTabEnabled) {
        super(new CreativeInventoryScreen.CreativeScreenHandler(player), player.getInventory(), ScreenTexts.EMPTY);
        player.currentScreenHandler = this.handler;
        this.backgroundHeight = 136;
        this.backgroundWidth = 195;
        this.operatorTabEnabled = operatorTabEnabled;
        ItemGroups.updateDisplayContext(enabledFeatures, this.shouldShowOperatorTab(player), player.getWorld().getRegistryManager());
    }

    private boolean shouldShowOperatorTab(PlayerEntity player) {
        return player.isCreativeLevelTwoOp() && this.operatorTabEnabled;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {

    }

    @Override
    public TrinketPlayerScreenHandler trinkets$getHandler() {
        return ((TrinketPlayerScreenHandler)this.handler);
    }

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
}
