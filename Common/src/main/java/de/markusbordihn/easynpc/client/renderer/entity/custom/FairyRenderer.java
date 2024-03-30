/*
 * Copyright 2022 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.easynpc.client.renderer.entity.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.client.model.custom.FairyModel;
import de.markusbordihn.easynpc.client.renderer.entity.StandardHumanoidMobRenderer;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import de.markusbordihn.easynpc.entity.easynpc.npc.Fairy;
import de.markusbordihn.easynpc.entity.easynpc.npc.Fairy.Variant;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Rotations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;

public class FairyRenderer
    extends StandardHumanoidMobRenderer<Fairy, Fairy.Variant, FairyModel<Fairy>> {

  protected static final Map<Variant, ResourceLocation> TEXTURE_BY_VARIANT =
      Util.make(
          new EnumMap<>(Variant.class),
          map -> {
            map.put(
                Variant.BLUE,
                new ResourceLocation(Constants.MOD_ID, "textures/entity/fairy/fairy_blue.png"));
            map.put(
                Variant.GREEN,
                new ResourceLocation(Constants.MOD_ID, "textures/entity/fairy/fairy_green.png"));
            map.put(
                Variant.RED,
                new ResourceLocation(Constants.MOD_ID, "textures/entity/fairy/fairy_red.png"));
          });
  protected static final ResourceLocation DEFAULT_TEXTURE = TEXTURE_BY_VARIANT.get(Variant.GREEN);

  public FairyRenderer(
      EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation) {
    super(
        context,
        new FairyModel<>(context.bakeLayer(modelLayerLocation)),
        0.3F,
        DEFAULT_TEXTURE,
        TEXTURE_BY_VARIANT);
  }

  @Override
  public void rotateEntity(EasyNPC<?> easyNPC, PoseStack poseStack) {
    Rotations rootRotation = easyNPC.getEasyNPCModelData().getModelRootRotation();
    if (rootRotation != null) {
      poseStack.translate(0, 0.5, 0);
      poseStack.mulPose(Axis.XP.rotation(rootRotation.getX()));
      poseStack.mulPose(Axis.YP.rotation(rootRotation.getY()));
      poseStack.mulPose(Axis.ZP.rotation(rootRotation.getZ()));
      poseStack.translate(0, -0.5, 0);
    }
  }

  @Override
  public void renderDefaultPose(
      Fairy entity,
      FairyModel<Fairy> model,
      Pose pose,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int light) {
    switch (pose) {
      case DYING:
        poseStack.translate(-0.5D, 0.0D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(180f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(this.getFlipDegrees(entity)));
        poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
        model.getHead().xRot = -0.7853982F;
        model.getHead().yRot = -0.7853982F;
        model.getHead().zRot = -0.7853982F;
        break;
      case LONG_JUMPING:
        model.leftArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
        model.rightArmPose = HumanoidModel.ArmPose.SPYGLASS;
        break;
      case SLEEPING:
        poseStack.translate(0.5D, 0.0D, 0.0D);
        break;
      case SPIN_ATTACK:
        model.leftArmPose = HumanoidModel.ArmPose.BLOCK;
        model.rightArmPose = HumanoidModel.ArmPose.THROW_SPEAR;
        poseStack.mulPose(Axis.YP.rotationDegrees(-35f));
        break;
      default:
        model.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        model.getHead().xRot = 0F;
        model.getHead().yRot = 0F;
        model.getHead().zRot = 0F;
        break;
    }
  }
}
