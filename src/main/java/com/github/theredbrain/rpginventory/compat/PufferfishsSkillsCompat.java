package com.github.theredbrain.rpginventory.compat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.Skill;
import net.puffish.skillsmod.api.SkillsAPI;

import java.util.Optional;

public class PufferfishsSkillsCompat {

	public static Text getSkillLockedItemTooltipLine(String category_formatting_config_string, String category, String skill_formatting_config_string, String skill) {
		Text text = Text.empty();
		Optional<Category> optionalCategory = SkillsAPI.getCategory(Identifier.of(category));
		if (optionalCategory.isPresent()) {
			Optional<Skill> optionalSkill = optionalCategory.get().getSkill(skill);
			if (optionalSkill.isPresent()) {
				StringBuilder category_formatting_string = new StringBuilder();
				if (!category_formatting_config_string.isEmpty()) {
					for (int i = 0; i < category_formatting_config_string.length(); i++) {
						category_formatting_string.append("§").append(category_formatting_config_string.charAt(i));
					}
				}
				StringBuilder skill_formatting_string = new StringBuilder();
				if (!skill_formatting_config_string.isEmpty()) {
					for (int i = 0; i < skill_formatting_config_string.length(); i++) {
						skill_formatting_string.append("§").append(skill_formatting_config_string.charAt(i));
					}
				}
				text = Text.translatable("item.additional_tooltip.player_relation.skill_locked", Text.translatable(category_formatting_string + ""), Text.translatable(skill_formatting_string + ""));
			}
		}
		return text;
	}

	public static boolean isSkillUnlocked(ServerPlayerEntity serverPlayerEntity, String category, String skill) {
		Optional<Category> optionalCategory = SkillsAPI.getCategory(Identifier.of(category));
		if (optionalCategory.isPresent() && optionalCategory.get().isUnlocked(serverPlayerEntity)) {
			Optional<Skill> optionalSkill = optionalCategory.get().getSkill(skill);
			if (optionalSkill.isPresent()) {
				Skill.State state = optionalSkill.get().getState(serverPlayerEntity);
				if (state.equals(Skill.State.UNLOCKED)) {
					return true;
				}
			}
		}
		return false;
	}
}
