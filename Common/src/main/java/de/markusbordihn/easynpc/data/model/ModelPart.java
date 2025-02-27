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

package de.markusbordihn.easynpc.data.model;

public enum ModelPart {
  ARMS("Arms"),
  BODY("Body"),
  HEAD("Head"),
  LEFT_ARM("LeftArm"),
  LEFT_LEG("LeftLeg"),
  RIGHT_ARM("RightArm"),
  RIGHT_LEG("RightLeg"),
  ROOT("Root"),
  BOOTS("Boots"),
  CHESTPLATE("Chestplate"),
  HELMET("Helmet"),
  LEGGINGS("Leggings"),
  UNKNOWN("Unknown");

  public final String tagName;

  ModelPart(String tagName) {
    this.tagName = tagName;
  }

  public static ModelPart get(String modelPart) {
    if (modelPart == null || modelPart.isEmpty()) {
      return ModelPart.UNKNOWN;
    }
    try {
      return ModelPart.valueOf(modelPart);
    } catch (IllegalArgumentException e) {
      return ModelPart.UNKNOWN;
    }
  }

  public String getTagName() {
    return this.tagName;
  }
}
