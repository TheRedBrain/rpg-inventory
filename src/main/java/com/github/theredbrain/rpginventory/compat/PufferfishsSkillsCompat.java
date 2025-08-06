package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.rpginventory.component.type.SkillLockedComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.SkillsMod;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.Skill;
import net.puffish.skillsmod.api.SkillsAPI;

import java.util.Optional;

public class PufferfishsSkillsCompat {

	public static Text getSkillLockedItemTooltipLine(SkillLockedComponent skillLockedComponent) {
		Text text = Text.empty();
		Optional<Category> optionalCategory = SkillsAPI.getCategory(Identifier.of(skillLockedComponent.category()));
		if (optionalCategory.isPresent()) {
			Optional<Skill> optionalSkill = optionalCategory.get().getSkill(skillLockedComponent.skill());
			if (optionalSkill.isPresent()) {
				text = Text.translatable(skillLockedComponent.tooltipText());
			}
		}
		return text;
	}

	public static boolean isSkillUnlocked(ServerPlayerEntity serverPlayerEntity, String category, String skill) {
		Optional<Category> optionalCategory = SkillsAPI.getCategory(Identifier.of(category));
		if (optionalCategory.isPresent() && optionalCategory.get().isUnlocked(serverPlayerEntity)) {
			Optional<Skill> optionalSkill = optionalCategory.get().getSkill(skill);
			if (optionalSkill.isPresent()) {
				Optional<Skill.State> state = SkillsMod.getInstance().getSkillState(serverPlayerEntity, Identifier.of(category), skill);
				return state.isPresent() && state.get().equals(Skill.State.UNLOCKED);
			}
		}
		return false;
	}
}
