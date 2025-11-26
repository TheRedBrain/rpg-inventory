package com.github.theredbrain.rpginventory.mixin.client.azurelib;

import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.render.azurelib.DuckAzArmorBoneProviderMixin;
import mod.azure.azurelib.common.internal.client.util.RenderUtils;
import mod.azure.azurelib.rewrite.model.AzBakedModel;
import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.armor.bone.AzArmorBoneContext;
import mod.azure.azurelib.rewrite.render.armor.bone.AzArmorBoneProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AzArmorBoneContext.class)
public abstract class AzArmorBoneContextMixin {
	@Unique
	private AzBone leftGlove = null;
	@Unique
	private AzBone rightGlove = null;
	@Unique
	private AzBone leftShoulder = null;
	@Unique
	private AzBone rightShoulder = null;

	@Shadow(remap = false)
	protected abstract void setBoneVisible(@Nullable AzBone bone, boolean visible);

	@Shadow(remap = false)
	public abstract void setAllVisible(boolean pVisible);

	@Shadow(remap = false)
	public AzBone head;

	@Shadow(remap = false)
	public AzBone body;

	@Shadow(remap = false)
	public AzBone rightArm;

	@Shadow(remap = false)
	public AzBone leftArm;

	@Shadow(remap = false)
	public AzBone waist;

	@Shadow(remap = false)
	public AzBone rightLeg;

	@Shadow(remap = false)
	public AzBone leftLeg;

	@Shadow(remap = false)
	public AzBone rightBoot;

	@Shadow(remap = false)
	public AzBone leftBoot;

	@Inject(method = "setAllVisible", at = @At("TAIL"), remap = false)
	public void rpginventory$setAllVisible(boolean pVisible, CallbackInfo ci) {

		this.setBoneVisible(this.leftGlove, pVisible);
		this.setBoneVisible(this.rightGlove, pVisible);
		this.setBoneVisible(this.leftShoulder, pVisible);
		this.setBoneVisible(this.rightShoulder, pVisible);
	}

	@Inject(method = "grabRelevantBones", at = @At(value = "INVOKE", target = "Lmod/azure/azurelib/rewrite/render/armor/bone/AzArmorBoneProvider;getWaistBone(Lmod/azure/azurelib/rewrite/model/AzBakedModel;)Lmod/azure/azurelib/rewrite/model/AzBone;", shift = At.Shift.AFTER), remap = false)
	public void rpginventory$grabRelevantBones(AzBakedModel model, AzArmorBoneProvider boneProvider, CallbackInfo ci) {
		this.leftGlove = ((DuckAzArmorBoneProviderMixin) boneProvider).rpginventory$getLeftGloveBone(model);
		this.rightGlove = ((DuckAzArmorBoneProviderMixin) boneProvider).rpginventory$getRightGloveBone(model);
		this.leftShoulder = ((DuckAzArmorBoneProviderMixin) boneProvider).rpginventory$getLeftShoulderBone(model);
		this.rightShoulder = ((DuckAzArmorBoneProviderMixin) boneProvider).rpginventory$getRightShoulderBone(model);
	}

	/**
	 * @author TheRedBrain
	 * @reason incorporate glove and shoulder slot
	 */
	@Overwrite
	public void applyBaseTransformations(BipedEntityModel<?> baseModel) {
		if (this.head != null) {
			ModelPart headPart = baseModel.head;

			RenderUtils.matchModelPartRot(headPart, this.head);
			this.head.updatePosition(headPart.pivotX, -headPart.pivotY, headPart.pivotZ);
		}

		if (this.body != null) {
			ModelPart bodyPart = baseModel.body;

			RenderUtils.matchModelPartRot(bodyPart, this.body);
			this.body.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
		}

		if (this.rightArm != null) {
			ModelPart rightArmPart = baseModel.rightArm;

			RenderUtils.matchModelPartRot(rightArmPart, this.rightArm);
			this.rightArm.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);

			if (this.rightGlove != null) {
				RenderUtils.matchModelPartRot(rightArmPart, this.rightGlove);
				this.rightGlove.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);
			}

			if (this.rightShoulder != null) {
				RenderUtils.matchModelPartRot(rightArmPart, this.rightShoulder);
				this.rightShoulder.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);
			}
		}

		if (this.leftArm != null) {
			ModelPart leftArmPart = baseModel.leftArm;

			RenderUtils.matchModelPartRot(leftArmPart, this.leftArm);
			this.leftArm.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);

			if (this.rightGlove != null) {
				RenderUtils.matchModelPartRot(leftArmPart, this.rightGlove);
				this.rightGlove.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);
			}

			if (this.rightShoulder != null) {
				RenderUtils.matchModelPartRot(leftArmPart, this.rightShoulder);
				this.rightShoulder.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);
			}
		}

		if (this.rightLeg != null) {
			ModelPart rightLegPart = baseModel.rightLeg;

			RenderUtils.matchModelPartRot(rightLegPart, this.rightLeg);
			this.rightLeg.updatePosition(rightLegPart.pivotX + 2, 12 - rightLegPart.pivotY, rightLegPart.pivotZ);

			if (this.rightBoot != null) {
				RenderUtils.matchModelPartRot(rightLegPart, this.rightBoot);
				this.rightBoot.updatePosition(rightLegPart.pivotX + 2, 12 - rightLegPart.pivotY, rightLegPart.pivotZ);
			}
			if (this.waist != null) {
				RenderUtils.matchModelPartRot(baseModel.body, this.waist);
				this.waist.updatePosition(baseModel.body.pivotX, -(baseModel.body.pivotY), baseModel.body.pivotZ);
			}
		}

		if (this.leftLeg != null) {
			ModelPart leftLegPart = baseModel.leftLeg;

			RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
			this.leftLeg.updatePosition(leftLegPart.pivotX - 2, 12 - leftLegPart.pivotY, leftLegPart.pivotZ);

			if (this.leftBoot != null) {
				RenderUtils.matchModelPartRot(leftLegPart, this.leftBoot);
				this.leftBoot.updatePosition(leftLegPart.pivotX - 2, 12 - leftLegPart.pivotY, leftLegPart.pivotZ);
			}
			if (this.waist != null) {
				RenderUtils.matchModelPartRot(baseModel.body, this.waist);
				this.waist.updatePosition(baseModel.body.pivotX, -(baseModel.body.pivotY), baseModel.body.pivotZ);
			}
		}
	}

	/**
	 * @author TheRedBrain
	 * @reason incorporate glove and shoulder slot
	 */
	@Overwrite
	public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, BipedEntityModel<?> model) {
		this.setAllVisible(false);
		currentPart.visible = true;
		AzBone bone = null;
		if (currentPart != model.hat && currentPart != model.head) {
			if (currentPart == model.body) {
				bone = this.body;
			} else if (currentPart == model.leftArm) {
				bone = currentSlot == ExtendedEquipmentSlot.GLOVES ? this.leftGlove : currentSlot == ExtendedEquipmentSlot.SHOULDERS ? this.leftShoulder : this.leftArm;
			} else if (currentPart == model.rightArm) {
				bone = currentSlot == ExtendedEquipmentSlot.GLOVES ? this.rightGlove : currentSlot == ExtendedEquipmentSlot.SHOULDERS ? this.rightShoulder : this.rightArm;
			} else if (currentPart == model.leftLeg) {
				bone = currentSlot == EquipmentSlot.FEET ? this.leftBoot : this.leftLeg;
			} else if (currentPart == model.rightLeg) {
				bone = currentSlot == EquipmentSlot.FEET ? this.rightBoot : this.rightLeg;
			}
		} else {
			bone = this.head;
		}

		if (bone != null) {
			bone.setHidden(false);
		}

		if (currentSlot == EquipmentSlot.LEGS && (currentPart == model.leftLeg || currentPart == model.rightLeg) && this.waist != null) {
			this.waist.setHidden(false);
		}

	}

	/**
	 * @author TheRedBrain
	 * @reason incorporate glove and shoulder slot
	 */
	@Overwrite
	public void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
		this.setAllVisible(false);
		if (currentSlot == EquipmentSlot.HEAD) {
			this.setBoneVisible(this.head, true);
		} else if (currentSlot == EquipmentSlot.CHEST) {
			this.setBoneVisible(this.body, true);
			this.setBoneVisible(this.rightArm, true);
			this.setBoneVisible(this.leftArm, true);
			this.setBoneVisible(this.waist, false);
		} else if (currentSlot == EquipmentSlot.LEGS) {
			this.setBoneVisible(this.rightLeg, true);
			this.setBoneVisible(this.leftLeg, true);
			this.setBoneVisible(this.waist, true);
		} else if (currentSlot == EquipmentSlot.FEET) {
			this.setBoneVisible(this.rightBoot, true);
			this.setBoneVisible(this.leftBoot, true);
		} else if (currentSlot == ExtendedEquipmentSlot.GLOVES) {
			this.setBoneVisible(this.rightGlove, true);
			this.setBoneVisible(this.leftGlove, true);
		} else if (currentSlot == ExtendedEquipmentSlot.SHOULDERS) {
			this.setBoneVisible(this.rightShoulder, true);
			this.setBoneVisible(this.leftShoulder, true);
		}
	}
}
