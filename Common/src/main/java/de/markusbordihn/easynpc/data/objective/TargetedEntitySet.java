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

package de.markusbordihn.easynpc.data.objective;

import java.util.HashSet;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class TargetedEntitySet {

  public static final StreamCodec<RegistryFriendlyByteBuf, HashSet<UUID>> STREAM_CODEC =
      new StreamCodec<>() {
        @Override
        public HashSet<UUID> decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
          HashSet<UUID> values = new HashSet<>();
          while (registryFriendlyByteBuf.isReadable()) {
            values.add(registryFriendlyByteBuf.readUUID());
          }
          return values;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf registryFriendlyByteBuf, HashSet<UUID> values) {
          for (UUID value : values) {
            registryFriendlyByteBuf.writeUUID(value);
          }
        }
      };

  private TargetedEntitySet() {}
}
