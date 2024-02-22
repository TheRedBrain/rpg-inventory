package com.github.theredbrain.betteradventuremode.mixin.client.network.message;

import com.github.theredbrain.betteradventuremode.client.gui.hud.DuckInGameHudMixin;
import com.github.theredbrain.betteradventuremode.client.network.message.DuckMessageHandlerMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(MessageHandler.class)
public abstract class MessageHandlerMixin implements DuckMessageHandlerMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract UUID extractSender(Text text);

    @Override
    public void betteradventuremode$onAnnouncement(Text announcement) {
        if (!this.client.options.getHideMatchedNames().getValue() || !this.client.shouldBlockMessages(this.extractSender(announcement))) {
            ((DuckInGameHudMixin) this.client.inGameHud).betteradventuremode$setAnnouncementMessage(announcement);

            this.client.getNarratorManager().narrateSystemMessage(announcement);
        }
    }
}
