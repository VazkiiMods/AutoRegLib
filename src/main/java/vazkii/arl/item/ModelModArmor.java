///**
// * This class was created by <Vazkii>. It's distributed as
// * part of the Quark Mod. Get the Source Code in github:
// * https://github.com/Vazkii/Quark
// *
// * Quark is Open Source and distributed under the
// * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
// *
// * File Created @ [02/07/2016, 23:16:57 (GMT)]
// */
//package vazkii.arl.item;
//
//import javax.annotation.Nonnull;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.mojang.blaze3d.platform.GlStateManager;
//import com.mojang.blaze3d.vertex.IVertexBuilder;
//
//import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
//import net.minecraft.client.renderer.entity.model.BipedModel;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.item.ArmorStandEntity;
//import net.minecraft.item.CrossbowItem;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.item.UseAction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.HandSide;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import vazkii.arl.util.ClientTicker;
//
//@OnlyIn(Dist.CLIENT)
//public abstract class ModelModArmor<T extends LivingEntity> extends BipedModel<T> {
//
//	public abstract void setModelParts();
//
//	@Override
//	public void render(MatrixStack stack, IVertexBuilder vbuilder, int lightmap, int overlay, float f2, float f3, float f4, float f5) {
//
//		GlStateManager.pushMatrix();
//		if(entity instanceof ArmorStandEntity) { // Fixes rendering on armor stands
//			f3 = 0;
//			GlStateManager.translatef(0F, 0.15F, 0F);
//		}
//
//		super.render(stack, vbuilder, lightmap, overlay, f2, f3, f4, f5);
//		GlStateManager.popMatrix();
//	}
//	
//	@Override
//	public void setLivingAnimations(T entity, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
//		setModelParts();
//		prepareForRender(entity, ClientTicker.partialTicks);
//		
//		super.setLivingAnimations(entity, p_212843_2_, p_212843_3_, p_212843_4_);
//	}
//
//	public void prepareForRender(T entity, float pticks) {
//		LivingEntity living = (LivingEntity) entity;
//		isSneak = living != null && living.isSneaking();
//		isChild = living != null && living.isChild();
//		if(living instanceof AbstractClientPlayerEntity) {
//			AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) living;
//
//			swingProgress = player.getSwingProgress(pticks);
//
//			BipedModel.ArmPose leftPose = BipedModel.ArmPose.EMPTY;
//			BipedModel.ArmPose rightPose = BipedModel.ArmPose.EMPTY;
//			ItemStack itemstack = player.getHeldItemMainhand();
//			ItemStack itemstack1 = player.getHeldItemOffhand();
//
//			leftPose = getArmPose(player, itemstack, itemstack1, Hand.MAIN_HAND);
//			rightPose = getArmPose(player, itemstack, itemstack1, Hand.MAIN_HAND);
//			
//			if(player.getPrimaryHand() == HandSide.RIGHT) {
//				rightArmPose = leftPose;
//				leftArmPose = rightPose;
//			} else {
//				rightArmPose = rightPose;
//				leftArmPose = leftPose;
//			}
//		}
//	}
//
//	// vanilla copy
//	private BipedModel.ArmPose getArmPose(AbstractClientPlayerEntity p_217766_1_, ItemStack p_217766_2_, ItemStack p_217766_3_, Hand p_217766_4_) {
//		BipedModel.ArmPose bipedmodel$armpose = BipedModel.ArmPose.EMPTY;
//		ItemStack itemstack = p_217766_4_ == Hand.MAIN_HAND ? p_217766_2_ : p_217766_3_;
//		if (!itemstack.isEmpty()) {
//			bipedmodel$armpose = BipedModel.ArmPose.ITEM;
//			if (p_217766_1_.getItemInUseCount() > 0) {
//				UseAction useaction = itemstack.getUseAction();
//				if (useaction == UseAction.BLOCK) {
//					bipedmodel$armpose = BipedModel.ArmPose.BLOCK;
//				} else if (useaction == UseAction.BOW) {
//					bipedmodel$armpose = BipedModel.ArmPose.BOW_AND_ARROW;
//				} else if (useaction == UseAction.SPEAR) {
//					bipedmodel$armpose = BipedModel.ArmPose.THROW_SPEAR;
//				} else if (useaction == UseAction.CROSSBOW && p_217766_4_ == p_217766_1_.getActiveHand()) {
//					bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_CHARGE;
//				}
//			} else {
//				boolean flag3 = p_217766_2_.getItem() == Items.CROSSBOW;
//				boolean flag = CrossbowItem.isCharged(p_217766_2_);
//				boolean flag1 = p_217766_3_.getItem() == Items.CROSSBOW;
//				boolean flag2 = CrossbowItem.isCharged(p_217766_3_);
//				if (flag3 && flag) {
//					bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
//				}
//
//				if (flag1 && flag2 && p_217766_2_.getItem().getUseAction(p_217766_2_) == UseAction.NONE) {
//					bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
//				}
//			}
//		}
//
//		return bipedmodel$armpose;
//	}
//
//}
