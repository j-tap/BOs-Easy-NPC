/**
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

package de.markusbordihn.easynpc.client.screen;

import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.entity.EasyNPCEntity;
import de.markusbordihn.easynpc.menu.DialogMenu;

@OnlyIn(Dist.CLIENT)
public class DialogScreen extends AbstractContainerScreen<DialogMenu> {

  protected final EasyNPCEntity entity;

  private float xMouse;
  private float yMouse;

  private List<FormattedCharSequence> cachedDialogComponents = Collections.emptyList();

  public DialogScreen(DialogMenu menu, Inventory inventory, Component component) {
    super(menu, inventory, component);
    this.entity = menu.getEntity();
  }

  protected void renderAvatar(PoseStack poseStack, int x, int y) {
    int positionTop = 75;
    if (this.entity != null) {
      int left = this.leftPos + 40;
      int top = this.topPos + 60 + positionTop;
      ScreenHelper.renderEntityAvatar(left, top,
          Math.round(left - 90 - (this.xMouse * 0.25)),
          Math.round(top - 120 - (this.yMouse * 0.5)), this.entity);
    }
  }

  protected void renderDialog(PoseStack poseStack, int x, int y) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, Constants.TEXTURE_DIALOG);

    // Dialog text.
    TextComponent dialogComponent = this.entity.getDialogComponent();
    this.cachedDialogComponents = this.font.split(dialogComponent, 176);
    int numberOfLines = Math.min(128 / font.lineHeight, this.cachedDialogComponents.size());

    // Dialog background according numbers of lines.
    int minNumberOfLines = Math.max(2, numberOfLines);
    int backgroundShift = minNumberOfLines * (font.lineHeight + 2);
    this.blit(poseStack, leftPos + 70, topPos + 25 + 30, 0,130 - backgroundShift,
        200, Math.min(120, backgroundShift));
    this.blit(poseStack, leftPos + 70, topPos + 25, 0, 0, 200, 30);

    // Distribute text for the across the lines.
    for (int line = 0; line < numberOfLines; ++line) {
      FormattedCharSequence formattedCharSequence = this.cachedDialogComponents.get(line);
      this.font.draw(poseStack, formattedCharSequence, leftPos + 87f,
          topPos + 32 + (line * (font.lineHeight + 2)), 0);
    }
  }

  @Override
  public void init() {
    super.init();

    // Default stats
    this.imageHeight = 170;
    this.imageWidth = 275;

    // Basic Position
    this.titleLabelX = 8;
    this.titleLabelY = 6;
    this.topPos = (this.height - this.imageHeight) / 2;
    this.leftPos = (this.width - this.imageWidth) / 2;
  }

  @Override
  public void render(PoseStack poseStack, int x, int y, float partialTicks) {
    super.render(poseStack, x, y, partialTicks);
    this.xMouse = x;
    this.yMouse = y;
    renderAvatar(poseStack, x, y);
    renderDialog(poseStack, x, y);
  }

  @Override
  protected void renderLabels(PoseStack poseStack, int x, int y) {
    this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, Constants.TEXTURE_DEMO_BACKGROUND);

    // Main screen
    this.blit(poseStack, leftPos, topPos, 0, 0, 250, 170);
    this.blit(poseStack, leftPos + 243, topPos, 215, 0, 35, 170);
  }

}
