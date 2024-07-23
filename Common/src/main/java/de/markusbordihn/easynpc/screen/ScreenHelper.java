/*
 * Copyright 2023 Markus Bordihn
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

package de.markusbordihn.easynpc.screen;

import com.mojang.blaze3d.platform.Lighting;
import de.markusbordihn.easynpc.data.model.ModelPose;
import de.markusbordihn.easynpc.data.profession.Profession;
import de.markusbordihn.easynpc.data.rotation.CustomRotation;
import de.markusbordihn.easynpc.data.skin.SkinType;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import de.markusbordihn.easynpc.entity.easynpc.data.DialogData;
import de.markusbordihn.easynpc.entity.easynpc.data.GuiData;
import de.markusbordihn.easynpc.entity.easynpc.data.ModelData;
import de.markusbordihn.easynpc.entity.easynpc.data.ProfessionData;
import de.markusbordihn.easynpc.entity.easynpc.data.ScaleData;
import de.markusbordihn.easynpc.entity.easynpc.data.SkinData;
import de.markusbordihn.easynpc.entity.easynpc.data.VariantData;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.joml.Quaternionf;

public class ScreenHelper {

  protected ScreenHelper() {}

  public static void renderEntity(
      GuiGraphics guiGraphics,
      int x,
      int y,
      int scale,
      float yRot,
      float xRot,
      LivingEntity livingEntity) {
    // Prepare Renderer
    Minecraft minecraft = Minecraft.getInstance();
    float rotationY = (float) Math.atan(yRot / 40.0F);
    float rotationX = (float) Math.atan(xRot / 40.0F);
    Quaternionf quaternionfZ = (new Quaternionf()).rotateZ(3.1415927F);
    Quaternionf quaternionfX = (new Quaternionf()).rotateX(rotationX * 20.0F * 0.017453292F);
    quaternionfZ.mul(quaternionfX);

    // Backup entity information
    Component entityCustomName = livingEntity.getCustomName();
    boolean entityShouldShowName = livingEntity.shouldShowName();
    float entityXRot = livingEntity.getXRot();
    float entityYBodyRot = livingEntity.yBodyRot;
    float entityYHeadRot = livingEntity.yHeadRot;
    float entityYHeadRotO = livingEntity.yHeadRotO;
    float entityYRot = livingEntity.getYRot();

    // Adjust entity information for rendering
    livingEntity.yBodyRot = 180.0F + rotationY * 20.0F;
    livingEntity.setYRot(180.0F + rotationY * 40.0F);
    livingEntity.setXRot(-rotationX * 20.0F);
    livingEntity.yHeadRot = livingEntity.getYRot();
    livingEntity.yHeadRotO = livingEntity.getYRot();

    // Hide gui elements or remove custom name
    boolean minecraftHideGui = false;
    if (minecraft != null) {
      minecraftHideGui = minecraft.options.hideGui;
      minecraft.options.hideGui = true;
    } else {
      livingEntity.setCustomName(null);
      livingEntity.setCustomNameVisible(false);
    }

    // Render Entity
    guiGraphics.pose().pushPose();
    guiGraphics.pose().translate(x, y, 1050.0D);
    guiGraphics.pose().scale(scale, scale, -scale);
    guiGraphics.pose().mulPose(quaternionfZ);
    Lighting.setupForEntityInInventory();
    EntityRenderDispatcher entityRenderDispatcher =
        Minecraft.getInstance().getEntityRenderDispatcher();
    quaternionfX.conjugate();
    entityRenderDispatcher.overrideCameraOrientation(quaternionfX);
    entityRenderDispatcher.setRenderShadow(false);
    entityRenderDispatcher.render(
        livingEntity,
        0.0D,
        0.0D,
        0.0D,
        0.0F,
        1.0F,
        guiGraphics.pose(),
        guiGraphics.bufferSource(),
        15728880);
    guiGraphics.flush();
    entityRenderDispatcher.setRenderShadow(true);
    guiGraphics.pose().popPose();
    Lighting.setupFor3DItems();

    // Restore entity information
    livingEntity.yBodyRot = entityYBodyRot;
    livingEntity.setYRot(entityYRot);
    livingEntity.setXRot(entityXRot);
    livingEntity.yHeadRot = entityYHeadRot;
    livingEntity.yHeadRotO = entityYHeadRotO;

    // Restore gui elements or custom name
    if (minecraft != null) {
      minecraft.options.hideGui = minecraftHideGui;
    } else {
      livingEntity.setCustomName(entityCustomName);
      livingEntity.setCustomNameVisible(entityShouldShowName);
    }
  }

  public static void renderScaledEntityAvatar(
      GuiGraphics guiGraphics, int x, int y, float yRot, float xRot, EasyNPC<?> easyNPC) {
    GuiData<?> guiData = easyNPC.getEasyNPCGuiData();
    ScaleData<?> scaleData = easyNPC.getEasyNPCScaleData();
    ModelData<?> modelData = easyNPC.getEasyNPCModelData();
    if (scaleData != null && modelData != null) {
      renderScaledEntityAvatar(
          guiGraphics,
          x,
          y,
          guiData.getEntityGuiScaling(),
          yRot,
          xRot,
          easyNPC,
          scaleData,
          modelData);
    } else {
      renderScaledEntityAvatar(
          guiGraphics, x, y, guiData.getEntityGuiScaling(), yRot, xRot, easyNPC.getLivingEntity());
    }
  }

  public static void renderScaledEntityAvatar(
      GuiGraphics guiGraphics,
      int x,
      int y,
      int scale,
      float yRot,
      float xRot,
      EasyNPC<?> easyNPC,
      ScaleData<?> scaleData,
      ModelData<?> modelData) {

    // Backup entity information
    float entityScaleX = scaleData.getScaleX();
    float entityScaleY = scaleData.getScaleY();
    float entityScaleZ = scaleData.getScaleZ();
    CustomRotation entityModelRootRotation = modelData.getModelRootRotation();
    boolean entityInvisible = easyNPC.getEntity().isInvisible();

    // Adjust entity information for rendering
    scaleData.setScaleX(scaleData.getDefaultScaleX());
    scaleData.setScaleY(scaleData.getDefaultScaleY());
    scaleData.setScaleZ(scaleData.getDefaultScaleZ());
    modelData.setModelRootRotation(new CustomRotation(0.0F, 0.0F, 0.0F));
    easyNPC.getEntity().setInvisible(false);

    // Render Entity
    renderEntity(guiGraphics, x, y, scale, yRot, xRot, easyNPC.getLivingEntity());

    // Restore entity information
    scaleData.setScaleX(entityScaleX);
    scaleData.setScaleY(entityScaleY);
    scaleData.setScaleZ(entityScaleZ);
    modelData.setModelRootRotation(entityModelRootRotation);
    easyNPC.getEntity().setInvisible(entityInvisible);
  }

  public static void renderScaledEntityAvatar(
      GuiGraphics guiGraphics,
      int x,
      int y,
      int scale,
      float yRot,
      float xRot,
      LivingEntity livingEntity) {

    // Backup entity information
    boolean entityInvisible = livingEntity.isInvisible();

    // Adjust entity information for rendering
    livingEntity.setInvisible(false);

    // Render Entity
    renderEntity(guiGraphics, x, y, scale, yRot, xRot, livingEntity);

    // Restore entity information
    livingEntity.setInvisible(entityInvisible);
  }

  public static void renderCustomPoseEntityAvatar(
      GuiGraphics guiGraphics,
      int x,
      int y,
      int scale,
      float yRot,
      float xRot,
      EasyNPC<?> easyNPC) {
    ModelData<?> modelData = easyNPC.getEasyNPCModelData();
    Entity entity = easyNPC.getEntity();

    // Backup entity information
    ModelPose entityModelPose = modelData.getModelPose();
    Pose entityPose = easyNPC.getEntity().getPose();

    // Adjust entity information for rendering
    modelData.setModelPose(ModelPose.CUSTOM);
    entity.setPose(Pose.STANDING);

    // Render Entity
    renderScaledEntityAvatar(
        guiGraphics, x, y, scale, yRot, xRot, easyNPC, easyNPC.getEasyNPCScaleData(), modelData);

    // Restore entity information
    modelData.setModelPose(entityModelPose);
    entity.setPose(entityPose);
  }

  public static void renderEntityAvatarForScaling(
      GuiGraphics guiGraphics,
      int x,
      int y,
      int scale,
      float yRot,
      float xRot,
      EasyNPC<?> easyNPC) {
    ModelData<?> modelData = easyNPC.getEasyNPCModelData();
    Entity entity = easyNPC.getEntity();

    // Backup entity information
    CustomRotation entityModelRootRotation = modelData.getModelRootRotation();
    boolean entityInvisible = entity.isInvisible();

    // Adjust entity information for rendering
    modelData.setModelRootRotation(new CustomRotation(0.0F, 0.0F, 0.0F));
    entity.setInvisible(false);

    // Render Entity
    renderEntity(guiGraphics, x, y, scale, yRot, xRot, easyNPC.getLivingEntity());

    // Restore entity information
    modelData.setModelRootRotation(entityModelRootRotation);
    entity.setInvisible(entityInvisible);
  }

  public static void renderEntityDialog(
      GuiGraphics guiGraphics, int x, int y, float yRot, float xRot, EasyNPC<?> easyNPC) {
    DialogData<?> dialogData = easyNPC.getEasyNPCDialogData();
    renderScaledEntityAvatar(
        guiGraphics,
        x,
        y,
        dialogData.getEntityDialogScaling(),
        yRot,
        xRot,
        easyNPC.getLivingEntity());
  }

  public static void renderEntityPlayerSkin(
      GuiGraphics guiGraphics,
      int x,
      int y,
      float yRot,
      float xRot,
      EasyNPC<?> easyNPC,
      UUID userUUID,
      SkinType skinType) {
    SkinData<?> skinData = easyNPC.getEasyNPCSkinData();
    GuiData<?> guiData = easyNPC.getEasyNPCGuiData();

    // Backup entity information
    SkinType entitySkinType = skinData.getSkinType();
    UUID entitySkinUUID = skinData.getSkinUUID();

    // Adjust entity information for rendering
    skinData.setSkinType(skinType);
    skinData.setSkinUUID(userUUID);

    // Render Entity
    renderScaledEntityAvatar(
        guiGraphics,
        x + guiData.getEntityGuiLeft(),
        y + guiData.getEntityGuiTop(),
        skinData.getEntitySkinScaling(),
        yRot,
        xRot,
        easyNPC,
        easyNPC.getEasyNPCScaleData(),
        easyNPC.getEasyNPCModelData());

    // Restore entity information
    skinData.setSkinType(entitySkinType);
    skinData.setSkinUUID(entitySkinUUID);
  }

  public static void renderEntityDefaultSkin(
      GuiGraphics guiGraphics,
      int x,
      int y,
      float yRot,
      float xRot,
      EasyNPC<?> easyNPC,
      Enum<?> variant,
      Profession profession) {
    SkinData<?> skinData = easyNPC.getEasyNPCSkinData();
    VariantData<?> variantData = easyNPC.getEasyNPCVariantData();
    ProfessionData<?> professionData = easyNPC.getEasyNPCProfessionData();
    GuiData<?> guiData = easyNPC.getEasyNPCGuiData();

    // Backup entity information
    SkinType entitySkinType = skinData.getSkinType();
    Enum<?> entityVariant = variantData.getVariant();
    Profession entityProfession = professionData.getProfession();

    // Adjust entity information for rendering
    skinData.setSkinType(SkinType.DEFAULT);
    variantData.setVariant(variant);
    professionData.setProfession(profession);

    // Render Entity
    renderScaledEntityAvatar(
        guiGraphics,
        x + guiData.getEntityGuiLeft(),
        y + guiData.getEntityGuiTop(),
        skinData.getEntitySkinScaling(),
        yRot,
        xRot,
        easyNPC.getLivingEntity());

    // Restore entity information
    skinData.setSkinType(entitySkinType);
    variantData.setVariant(entityVariant);
    professionData.setProfession(entityProfession);
  }
}
