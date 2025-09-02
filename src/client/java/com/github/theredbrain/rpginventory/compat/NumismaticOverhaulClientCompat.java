package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.rpginventory.gui.screen.ingame.RPGInventoryScreen;
import com.github.theredbrain.rpginventory.gui.screen.ingame.RPGInventoryTrinketScreen;
import com.glisco.numismaticoverhaul.NumismaticOverhaul;
import com.glisco.numismaticoverhaul.client.gui.PurseLayerElement;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.OwoUIDrawContext;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.layers.Layers;

import java.util.List;

public class NumismaticOverhaulClientCompat {
	public static void init() {
		Layers.add(
				PurseLayerContainer::new,
				new PurseLayerElement<>((instance, component) -> {
					instance.alignComponentToHandledScreenCoordinates(
							component,
							160 + NumismaticOverhaul.CONFIG.purseOffsets.survivalX(),
							5 + NumismaticOverhaul.CONFIG.purseOffsets.survivalY()
					);
				}),
				RPGInventoryScreen.class
		);
		Layers.add(
				PurseLayerContainer::new,
				new PurseLayerElement<>((instance, component) -> {
					instance.alignComponentToHandledScreenCoordinates(
							component,
							160 + NumismaticOverhaul.CONFIG.purseOffsets.survivalX(),
							5 + NumismaticOverhaul.CONFIG.purseOffsets.survivalY()
					);
				}),
				RPGInventoryTrinketScreen.class
		);
	}

	private static class PurseLayerContainer extends StackLayout {

		protected PurseLayerContainer(Sizing horizontalSizing, Sizing verticalSizing) {
			super(horizontalSizing, verticalSizing);
		}

		@Override
		protected void drawChildren(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta, List<? extends Component> children) {
			context.getMatrices().push();
			context.getMatrices().translate(0, 0, 300);
			super.drawChildren(context, mouseX, mouseY, partialTicks, delta, children);
			context.getMatrices().pop();
		}
	}
}
