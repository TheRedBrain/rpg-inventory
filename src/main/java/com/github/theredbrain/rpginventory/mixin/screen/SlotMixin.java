package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.screen.DuckSlotMixin;
import com.google.common.collect.Lists;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(Slot.class)
public class SlotMixin implements DuckSlotMixin {
	@Unique
	private List<Text> rpginventory$slotTooltipText = Lists.<Text>newArrayList();

	@Override
	public void rpginventory$setSlotTooltipText(List<Text> newSlotTooltipTextList) {
		this.rpginventory$slotTooltipText = newSlotTooltipTextList;
	}

	@Override
	public List<Text> rpginventory$getSlotTooltipText() {
		return this.rpginventory$slotTooltipText;
	}
}
