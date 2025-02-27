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

package de.markusbordihn.easynpc.network.message.server;

import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.data.dialog.DialogDataSet;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import de.markusbordihn.easynpc.entity.easynpc.data.DialogData;
import de.markusbordihn.easynpc.network.message.NetworkMessageRecord;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record SaveDialogSetMessage(UUID uuid, DialogDataSet dialogDataSet)
    implements NetworkMessageRecord {

  public static final ResourceLocation MESSAGE_ID =
      new ResourceLocation(Constants.MOD_ID, "save_dialog_set");

  public static SaveDialogSetMessage create(final FriendlyByteBuf buffer) {
    return new SaveDialogSetMessage(buffer.readUUID(), new DialogDataSet(buffer.readNbt()));
  }

  @Override
  public void write(final FriendlyByteBuf buffer) {
    buffer.writeUUID(this.uuid);
    buffer.writeNbt(this.dialogDataSet.createTag());
  }

  @Override
  public ResourceLocation id() {
    return MESSAGE_ID;
  }

  @Override
  public void handleServer(final ServerPlayer serverPlayer) {
    EasyNPC<?> easyNPC = getEasyNPCAndCheckAccess(this.uuid, serverPlayer);
    if (easyNPC == null) {
      return;
    }

    // Verify dialog data set.
    if (this.dialogDataSet == null) {
      log.error("Unable to get dialog data set with message {} from {}", easyNPC, serverPlayer);
      return;
    }

    // Validate Dialog data.
    DialogData<?> dialogData = easyNPC.getEasyNPCDialogData();
    if (dialogData == null) {
      log.error("Unable to get valid entity with UUID {} for {}", easyNPC, serverPlayer);
      return;
    }

    // Perform action.
    log.debug("Saving dialog {} for {} from {}", this.dialogDataSet, easyNPC, serverPlayer);
    dialogData.setDialogDataSet(this.dialogDataSet);
  }
}
