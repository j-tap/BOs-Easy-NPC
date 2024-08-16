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

package de.markusbordihn.easynpc.network;

import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.network.message.NetworkMessageRecord;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.NetworkRegistry.ChannelBuilder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.simple.SimpleChannel;

public class NetworkHandler implements NetworkHandlerInterface {

  private static final int PROTOCOL_VERSION = 21;
  public static final SimpleChannel INSTANCE =
      ChannelBuilder.named(new ResourceLocation(Constants.MOD_ID, "network"))
          .clientAcceptedVersions(version -> version.equals(String.valueOf(PROTOCOL_VERSION)))
          .serverAcceptedVersions(version -> version.equals(String.valueOf(PROTOCOL_VERSION)))
          .networkProtocolVersion(() -> String.valueOf(PROTOCOL_VERSION))
          .simpleChannel();

  private static int id = 0;

  public NetworkHandler() {
    log.info("{} NetworkHandler ...", Constants.LOG_REGISTER_PREFIX);
  }

  public static void registerNetworkHandler(final FMLCommonSetupEvent event) {
    log.info(
        "{} Network Handler for {} with version {} ...",
        Constants.LOG_REGISTER_PREFIX,
        INSTANCE,
        PROTOCOL_VERSION);

    event.enqueueWork(
        () -> {
          NetworkHandlerManager.registerClientNetworkHandler();
          NetworkHandlerManager.registerServerNetworkHandler();
        });
  }

  @Override
  public <M extends NetworkMessageRecord> void sendToServer(M networkMessageRecord) {
    try {
      INSTANCE.send(PacketDistributor.SERVER.noArg(), networkMessageRecord);
    } catch (Exception e) {
      log.error("Failed to send {} to server: {}", networkMessageRecord, e);
    }
  }

  @Override
  public <M extends NetworkMessageRecord> void sendToPlayer(
      M networkMessageRecord, ServerPlayer serverPlayer) {
    try {
      INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), networkMessageRecord);
    } catch (Exception e) {
      log.error(
          "Failed to send {} to player {}: {}",
          networkMessageRecord,
          serverPlayer.getName().getString(),
          e);
    }
  }

  @Override
  public void sendToAllPlayers(final NetworkMessageRecord networkMessageRecord) {
    try {
      INSTANCE.send(PacketDistributor.ALL.noArg(), networkMessageRecord);
    } catch (Exception e) {
      log.error("Failed to send {} to all players: {}", networkMessageRecord, e);
    }
  }

  @Override
  public <M extends NetworkMessageRecord> void registerClientNetworkMessageHandler(
      ResourceLocation messageID, Class<M> networkMessage, Function<FriendlyByteBuf, M> creator) {
    int registrationID = id++;
    log.debug(
        "Registering client network message handler for {} with ID {}", messageID, registrationID);
    INSTANCE
        .messageBuilder(networkMessage, registrationID, PlayNetworkDirection.PLAY_TO_CLIENT)
        .encoder(M::write)
        .decoder(creator::apply)
        .consumerNetworkThread(
            (message, context) -> {
              context.enqueueWork(
                  () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> message::handleClient));
              context.setPacketHandled(true);
            })
        .add();
  }

  @Override
  public <M extends NetworkMessageRecord> void registerServerNetworkMessageHandler(
      ResourceLocation messageID, Class<M> networkMessage, Function<FriendlyByteBuf, M> creator) {
    int registrationID = id++;
    log.debug(
        "Registering server network message handler for {} with ID {}", messageID, registrationID);
    INSTANCE
        .messageBuilder(networkMessage, registrationID, PlayNetworkDirection.PLAY_TO_SERVER)
        .encoder(M::write)
        .decoder(creator::apply)
        .consumerNetworkThread(
            (message, context) -> {
              context.enqueueWork(() -> message.handleServer(context.getSender()));
              context.setPacketHandled(true);
            })
        .add();
  }
}