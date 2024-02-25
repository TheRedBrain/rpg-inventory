package com.github.theredbrain.betteradventuremode.mixin.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import dev.emi.trinkets.*;
import dev.emi.trinkets.api.SlotGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(value = CreativeInventoryScreen.class, priority = 1050)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> implements TrinketScreen {

    @Shadow private static ItemGroup selectedTab;
    @Unique
    private static final Identifier TAB_ADVENTURE_INVENTORY_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/adventure_creative_inventory/tab_adventure_inventory.png");
    @Unique
    private static final Identifier SPELL_SLOTS_BACKGROUND = BetterAdventureMode.identifier("textures/gui/container/adventure_creative_inventory/spell_slots_background.png");

    private CreativeInventoryScreenMixin() {
        super(null, null, null);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"), method = "drawBackground")
    private void betteradventuremode$drawAdventureInventoryBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (selectedTab.getType() == ItemGroup.Type.INVENTORY && BetterAdventureMode.serverConfig.use_adventure_inventory_screen) {
            context.drawTexture(TAB_ADVENTURE_INVENTORY_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        }
    }
//    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V"), method = "drawBackground", cancellable = true)
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIFFLnet/minecraft/entity/LivingEntity;)V"), method = "drawBackground", cancellable = true)
    private void betteradventuremode$moveDrawnPlayerEntity(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (selectedTab.getType() == ItemGroup.Type.INVENTORY && BetterAdventureMode.serverConfig.use_adventure_inventory_screen) {
//            InventoryScreen.drawEntity(context, this.x + 64, this.y + 6, this.x + 96, this.y + 49, 20, 0.0625F, (float)mouseX, (float)mouseY, this.client.player);
            InventoryScreen.drawEntity(context, this.x + 64, this.y + 6, 20, (float)(this.x + 64 - mouseX), (float)(this.y + 6 - 30 - mouseY), this.client.player);
//            InventoryScreen.drawEntity(context, this.x + 79, this.y + 45, 20, this.x + 88 - mouseX, this.y + 45 - 30 - mouseY, (LivingEntity)this.client.player);
            betteradventuremode$drawExtraGroups(context, this);
            ci.cancel();
        }
    }

    @Override
    public Rect2i trinkets$getGroupRect(SlotGroup group) {
        if (BetterAdventureMode.serverConfig.use_adventure_inventory_screen) {
            int order = group.getOrder();
            return switch (order) {
                case 1 -> new Rect2i(26, 32, 17, 17);
                case 2 -> new Rect2i(152, 5, 17, 17);
                case 3 -> new Rect2i(44, 5, 17, 17);
                case 4 -> new Rect2i(26, 5, 17, 17);
                case 5 -> new Rect2i(8, 5, 17, 17);
                case 6 -> new Rect2i(98, 5, 17, 17);
                case 7 -> new Rect2i(116, 5, 17, 17);
                case 8 -> new Rect2i(134, 5, 17, 17);
                case 9 -> new Rect2i(8, 32, 17, 17);
                case 10 -> new Rect2i(44, 32, 17, 17);
                case 11 -> new Rect2i(98, 32, 17, 17);
                case 12 -> new Rect2i(116, 32, 17, 17);
                case 13 -> new Rect2i(134, 32, 17, 17);
                case 14 -> new Rect2i(152, 32, 17, 17);
                case 15 -> new Rect2i(-33, 5, 17, 17);
                case 16 -> new Rect2i(-33, 23, 17, 17);
                case 17 -> new Rect2i(-33, 41, 17, 17);
                case 18 -> new Rect2i(-33, 59, 17, 17);
                case 19 -> new Rect2i(-15, 5, 17, 17);
                case 20 -> new Rect2i(-15, 23, 17, 17);
                case 21 -> new Rect2i(-15, 41, 17, 17);
                case 22 -> new Rect2i(-15, 59, 17, 17);
                default -> new Rect2i(0, 0, 0, 0);
            };
        } else {
            int groupNum = trinkets$getHandler().trinkets$getGroupNum(group);
            if (groupNum <= 3) {
                // Look what else do you want me to do
                return switch (groupNum) {
                    case 1 -> new Rect2i(15, 19, 17, 17);
                    case 2 -> new Rect2i(126, 19, 17, 17);
                    case 3 -> new Rect2i(145, 19, 17, 17);
                    case -5 -> new Rect2i(53, 5, 17, 17);
                    case -6 -> new Rect2i(53, 32, 17, 17);
                    case -7 -> new Rect2i(107, 5, 17, 17);
                    case -8 -> new Rect2i(107, 32, 17, 17);
                    case -45 -> new Rect2i(34, 19, 17, 17);
                    default -> new Rect2i(0, 0, 0, 0);
                };
            }
            Point pos = trinkets$getHandler().trinkets$getGroupPos(group);
            if (pos != null) {
                return new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17);
            }
        }
        return new Rect2i(0, 0, 0, 0);
    }

    @Unique
    private static void betteradventuremode$drawExtraGroups(DrawContext context, TrinketScreen trinketScreen) {
        int x = trinketScreen.trinkets$getX();
        int y = trinketScreen.trinkets$getY();
        context.drawTexture(SPELL_SLOTS_BACKGROUND, x - 41, y, 0, 0, 45, 84);
    }
}
