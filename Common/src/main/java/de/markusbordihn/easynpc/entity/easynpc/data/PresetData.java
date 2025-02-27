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

package de.markusbordihn.easynpc.entity.easynpc.data;

import de.markusbordihn.easynpc.data.model.ModelPose;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;

public interface PresetData<T extends PathfinderMob> extends EasyNPC<T> {

  default CompoundTag exportPresetData() {
    CompoundTag compoundTag = this.serializePresetData();

    // Remove spawner UUID to avoid side effects.
    if (compoundTag.contains(SpawnerData.DATA_SPAWNER_UUID_TAG)) {
      compoundTag.remove(SpawnerData.DATA_SPAWNER_UUID_TAG);
    }
    return compoundTag;
  }

  default void importPresetData(CompoundTag compoundTag) {

    // Skip import if no data is or no entity is available.
    if (compoundTag == null || compoundTag.isEmpty() || this.getEntity() == null) {
      return;
    }

    // Reset specific data to avoid side effects
    if (this.getEntity() != null) {
      this.getEntity().setPose(Pose.STANDING);
    }
    if (this.getEasyNPCModelData() != null) {
      this.getEasyNPCModelData().setModelPose(ModelPose.DEFAULT);
    }
    if (this.getEasyNPCActionEventData() != null) {
      this.getEasyNPCActionEventData().clearActionEventSet();
    }
    if (this.getEasyNPCDialogData() != null) {
      this.getEasyNPCDialogData().clearDialogDataSet();
    }

    // If preset contains id and pos then we can import it directly, otherwise we
    // need to merge it with existing data.
    if (!compoundTag.contains(Entity.UUID_TAG) || !compoundTag.contains("Pos")) {
      CompoundTag existingCompoundTag = this.serializePresetData();

      // Remove existing dialog data.
      if (existingCompoundTag.contains(DialogData.DATA_DIALOG_DATA_TAG)) {
        existingCompoundTag.remove(DialogData.DATA_DIALOG_DATA_TAG);
      }

      // Remove existing model data.
      if (existingCompoundTag.contains(ModelData.EASY_NPC_DATA_MODEL_DATA_TAG)) {
        existingCompoundTag.remove(ModelData.EASY_NPC_DATA_MODEL_DATA_TAG);
      }

      // Remove existing skin data.
      if (existingCompoundTag.contains(SkinData.EASY_NPC_DATA_SKIN_DATA_TAG)) {
        existingCompoundTag.remove(SkinData.EASY_NPC_DATA_SKIN_DATA_TAG);
      }

      // Remove existing render data.
      if (existingCompoundTag.contains(RenderData.DATA_RENDER_DATA_TAG)) {
        existingCompoundTag.remove(RenderData.DATA_RENDER_DATA_TAG);
      }

      // Remove existing action data.
      if (existingCompoundTag.contains(ActionEventData.DATA_ACTION_DATA_TAG)) {
        existingCompoundTag.remove(ActionEventData.DATA_ACTION_DATA_TAG);
      }

      log.debug(
          "Merging preset {} with existing data {} for {}", compoundTag, existingCompoundTag, this);
      compoundTag = existingCompoundTag.merge(compoundTag);
    } else {
      log.debug("Importing full preset {} for {}", compoundTag, this);
    }

    // Remove motion tag to avoid side effects.
    if (compoundTag.contains("Motion")) {
      compoundTag.remove("Motion");
    }

    // Import preset data to entity.
    this.getEntity().load(compoundTag);
  }

  default CompoundTag serializePresetData() {
    CompoundTag compoundTag = new CompoundTag();
    if (this.getEntity() == null) {
      return compoundTag;
    }
    String entityTypeId = this.getEntityTypeId();
    if (entityTypeId != null) {
      compoundTag.putString(Entity.ID_TAG, entityTypeId);
    }
    return this.getEntity().saveWithoutId(compoundTag);
  }
}
