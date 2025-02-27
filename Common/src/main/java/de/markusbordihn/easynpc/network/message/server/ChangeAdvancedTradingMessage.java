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
import de.markusbordihn.easynpc.data.trading.TradingValueType;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import de.markusbordihn.easynpc.entity.easynpc.data.TradingData;
import de.markusbordihn.easynpc.network.message.NetworkMessageRecord;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record ChangeAdvancedTradingMessage(
    UUID uuid, int tradingOfferIndex, TradingValueType tradingValueType, float tradingValue)
    implements NetworkMessageRecord {

  public static final ResourceLocation MESSAGE_ID =
      new ResourceLocation(Constants.MOD_ID, "change_advanced_trading");

  public static ChangeAdvancedTradingMessage create(final FriendlyByteBuf buffer) {
    return new ChangeAdvancedTradingMessage(
        buffer.readUUID(),
        buffer.readInt(),
        buffer.readEnum(TradingValueType.class),
        buffer.readFloat());
  }

  @Override
  public void write(final FriendlyByteBuf buffer) {
    buffer.writeUUID(this.uuid);
    buffer.writeInt(this.tradingOfferIndex);
    buffer.writeEnum(this.tradingValueType);
    buffer.writeFloat(this.tradingValue);
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

    // Validate trading offer index
    if (this.tradingOfferIndex < 0) {
      log.error(
          "Trading offer index {} is out of range (>= 0) for {}",
          this.tradingOfferIndex,
          serverPlayer);
      return;
    }

    // Validate trading value type
    if (this.tradingValueType == null) {
      log.error("Trading value type is unknown for {} from {}", easyNPC, serverPlayer);
      return;
    }

    // Validate trading value
    if (this.tradingValue < 0.0) {
      log.error(
          "Trading value {} for {} is out of range (>= 0) for {}",
          tradingValue,
          tradingValueType,
          serverPlayer);
      return;
    }

    // Validate trading data
    TradingData<?> tradingData = easyNPC.getEasyNPCTradingData();
    if (tradingData == null) {
      log.error("Trading data for {} is not available for {}", easyNPC, serverPlayer);
      return;
    }

    // Perform action.
    switch (this.tradingValueType) {
      case RESET_TRADING_EVERY_MIN:
        log.debug(
            "Set trading resets every min to {} for {} from {}",
            this.tradingValue,
            easyNPC,
            serverPlayer);
        tradingData.getTradingDataSet().setResetsEveryMin((int) this.tradingValue);
        break;
      case MAX_USES:
        log.debug(
            "Set advanced trading max uses {}# for {} to {} by {}",
            this.tradingOfferIndex,
            easyNPC,
            this.tradingValue,
            serverPlayer);
        tradingData.setAdvancedTradingMaxUses(this.tradingOfferIndex, (int) this.tradingValue);
        break;
      case REWARD_EXP:
        log.debug(
            "Set advanced trading xp {}# for {} to {} by {}",
            this.tradingOfferIndex,
            easyNPC,
            this.tradingValue,
            serverPlayer);
        tradingData.setAdvancedTradingXp(this.tradingOfferIndex, (int) this.tradingValue);
        break;
      case PRICE_MULTIPLIER:
        log.debug(
            "Set advanced trading price multiplier {}# for {} to {} by {}",
            this.tradingOfferIndex,
            easyNPC,
            this.tradingValue,
            serverPlayer);
        tradingData.setAdvancedTradingPriceMultiplier(this.tradingOfferIndex, this.tradingValue);
        break;
      case DEMAND:
        log.debug(
            "Set advanced trading demand {}# for {} to {} by {}",
            this.tradingOfferIndex,
            easyNPC,
            this.tradingValue,
            serverPlayer);
        tradingData.setAdvancedTradingDemand(this.tradingOfferIndex, (int) this.tradingValue);
        break;
      default:
        log.error(
            "Trading value type {} with value {}# for {} is unknown for {}",
            this.tradingValueType,
            this.tradingValue,
            this.tradingOfferIndex,
            serverPlayer);
    }
  }
}
