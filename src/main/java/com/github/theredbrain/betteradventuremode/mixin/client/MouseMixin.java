package com.github.theredbrain.betteradventuremode.mixin.client;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.SmoothUtil;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(value = Mouse.class, priority = 950)
public abstract class MouseMixin {

	@Shadow @Final private MinecraftClient client;

	@Shadow private double x;

	@Shadow private double y;

	@Shadow @Final private SmoothUtil cursorXSmoother;

	@Shadow @Final private SmoothUtil cursorYSmoother;

	@Shadow private double cursorDeltaX;

	@Shadow private double cursorDeltaY;
	@Shadow private double lastMouseUpdateTime;

	@Shadow public boolean isCursorLocked() {
		throw new AssertionError();
	}

	/**
	 * @author TheRedBrain
	 * @reason WIP
	 */
	@Overwrite
	public void updateMouse() {
		boolean betterThirdPersonEnabled = BetterAdventureModeClient.clientConfig.enable_360_degree_third_person && BetterAdventureMode.serverConfig.allow_360_degree_third_person && this.client.options.getPerspective() != Perspective.FIRST_PERSON;
		boolean arePlayerYawChangesDisabledByAttacking = BetterAdventureMode.serverConfig.disable_player_yaw_changes_during_attacks && ((MinecraftClient_BetterCombat) this.client).isWeaponSwingInProgress();
		double d = GlfwUtil.getTime();
		double e = d - this.lastMouseUpdateTime;
		this.lastMouseUpdateTime = d;
		if (this.isCursorLocked() && this.client.isWindowFocused()) {
			double f = (Double)this.client.options.getMouseSensitivity().getValue() * 0.6000000238418579 + 0.20000000298023224;
			double g = f * f * f;
			double h = g * 8.0;
			double k;
			double l;
			if (this.client.options.smoothCameraEnabled) {
				double i = this.cursorXSmoother.smooth(this.cursorDeltaX * h, e * h);
				double j = this.cursorYSmoother.smooth(this.cursorDeltaY * h, e * h);
				k = i;
				l = j;
			} else if (this.client.options.getPerspective().isFirstPerson() && this.client.player.isUsingSpyglass()) {
				this.cursorXSmoother.clear();
				this.cursorYSmoother.clear();
				k = this.cursorDeltaX * g;
				l = this.cursorDeltaY * g;
			} else {
				this.cursorXSmoother.clear();
				this.cursorYSmoother.clear();
				k = this.cursorDeltaX * h;
				l = this.cursorDeltaY * h;
			}

			this.cursorDeltaX = 0.0;
			this.cursorDeltaY = 0.0;
			int m = 1;
			if ((Boolean)this.client.options.getInvertYMouse().getValue()) {
				m = -1;
			}

			this.client.getTutorialManager().onUpdateMouse(k, l);
			if (betterThirdPersonEnabled) {
				BetterAdventureModeClient.INSTANCE.cameraYaw += k / 8.0F;
				BetterAdventureModeClient.INSTANCE.cameraPitch += (l * m) / 8.0F;
				float currentPitch = BetterAdventureModeClient.INSTANCE.cameraPitch;
				if (currentPitch < -90.0F) {
					BetterAdventureModeClient.INSTANCE.cameraPitch = -90.0F;
				} else if (currentPitch > 90.0F) {
					BetterAdventureModeClient.INSTANCE.cameraPitch = 90.0F;
				}
			}
			if (this.client.player != null) {
				StatusEffect statusEffect = Registries.STATUS_EFFECT.get(Identifier.tryParse(BetterAdventureMode.serverConfig.allow_pitch_changes_status_effect));
				boolean changingPitchDisabled = !(statusEffect != null && this.client.player.hasStatusEffect(statusEffect)) && !(this.client.player.isUsingItem() && this.client.player.getActiveItem().isIn(Tags.ENABLES_CHANGING_PITCH_ON_USING)) && BetterAdventureMode.serverConfig.disable_player_pitch_changes && BetterAdventureMode.serverConfig.allow_360_degree_third_person;
				this.client.player.changeLookDirection(betterThirdPersonEnabled || arePlayerYawChangesDisabledByAttacking ? 0 : k, changingPitchDisabled ? 0 : l * (double)m);
			}

		} else {
			this.cursorDeltaX = 0.0;
			this.cursorDeltaY = 0.0;
		}
	}
}