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

package de.markusbordihn.easynpc.client.texture;

import de.markusbordihn.easynpc.data.skin.SkinModel;
import java.util.Objects;
import java.util.UUID;

public class TextureModelKey {

  private final UUID uuid;
  private final String subType;
  private final SkinModel skinModel;
  private final String resourceName;

  public TextureModelKey(UUID uuid, SkinModel skinModel) {
    this(uuid, skinModel, "");
  }

  public TextureModelKey(UUID uuid, SkinModel skinModel, String resourceName) {
    this.uuid = uuid;
    this.subType = skinModel != null ? skinModel.name() : "";
    this.skinModel = skinModel;
    this.resourceName = resourceName != null ? resourceName : "";
  }

  public UUID getUUID() {
    return this.uuid;
  }

  public String getSubType() {
    return this.subType;
  }

  public SkinModel getSkinModel() {
    return this.skinModel;
  }

  public String getResourceName() {
    return this.resourceName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.uuid, this.subType);
  }

  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof TextureModelKey textureModelKey)) {
      return false;
    }

    return this.uuid.equals(textureModelKey.uuid) && this.subType.equals(textureModelKey.subType);
  }

  @Override
  public String toString() {
    return "TextureModelKey{"
        + "uuid="
        + this.uuid
        + ", skinModel="
        + this.skinModel
        + ", subType='"
        + this.subType
        + ", resourceName='"
        + this.resourceName
        + '\''
        + '}';
  }
}
