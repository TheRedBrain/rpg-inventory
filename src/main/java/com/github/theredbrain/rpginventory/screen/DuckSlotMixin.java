package com.github.theredbrain.rpginventory.screen;

import net.minecraft.text.Text;

import java.util.List;

public interface DuckSlotMixin {
	void rpginventory$setSlotTooltipText(List<Text> newSlotTooltipTextList);
	List<Text> rpginventory$getSlotTooltipText();
}
