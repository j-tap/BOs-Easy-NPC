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

package de.markusbordihn.easynpc.entity.easynpc.npc;

import de.markusbordihn.easynpc.data.skin.SkinModel;
import de.markusbordihn.easynpc.data.sound.SoundDataSet;
import de.markusbordihn.easynpc.data.sound.SoundType;
import de.markusbordihn.easynpc.entity.EasyNPCBaseModelEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class Piglin extends EasyNPCBaseModelEntity<Piglin> {

  public static final String ID = "piglin";
  public static final String ID_BRUTE = "piglin_brute";
  public static final String ID_ZOMBIFIED = "piglin_zombified";

  public Piglin(EntityType<? extends PathfinderMob> entityType, Level level) {
    this(entityType, level, Variant.PIGLIN);
  }

  public Piglin(EntityType<? extends PathfinderMob> entityType, Level level, Enum<?> variant) {
    super(entityType, level, variant);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.FOLLOW_RANGE, 32.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.5F)
        .add(Attributes.ATTACK_DAMAGE, 1.0D)
        .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
        .add(Attributes.ATTACK_SPEED, 0.0D)
        .add(Attributes.ARMOR, 0.0D)
        .add(Attributes.ARMOR_TOUGHNESS, 0.0D);
  }

  @Override
  public SkinModel getSkinModel() {
    return SkinModel.PIGLIN;
  }

  @Override
  public Enum<?>[] getVariants() {
    return Variant.values();
  }

  @Override
  public Enum<?> getDefaultVariant() {
    return Variant.PIGLIN;
  }

  @Override
  public Enum<?> getVariant(String name) {
    try {
      return Variant.valueOf(name);
    } catch (IllegalArgumentException e) {
      return getDefaultVariant();
    }
  }

  @Override
  public SoundDataSet getDefaultSoundDataSet(SoundDataSet soundDataSet, String variantName) {
    Variant soundVariant = Variant.valueOf(variantName);
    switch (soundVariant) {
      case PIGLIN_BRUTE:
        soundDataSet.addSound(SoundType.AMBIENT, SoundEvents.PIGLIN_BRUTE_AMBIENT);
        soundDataSet.addSound(SoundType.HURT, SoundEvents.PIGLIN_BRUTE_HURT);
        soundDataSet.addSound(SoundType.DEATH, SoundEvents.PIGLIN_BRUTE_DEATH);
        soundDataSet.addSound(SoundType.STEP, SoundEvents.PIGLIN_BRUTE_STEP);
        break;
      case ZOMBIFIED_PIGLIN:
        soundDataSet.addSound(SoundType.AMBIENT, SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT);
        soundDataSet.addSound(SoundType.HURT, SoundEvents.ZOMBIFIED_PIGLIN_HURT);
        soundDataSet.addSound(SoundType.DEATH, SoundEvents.ZOMBIFIED_PIGLIN_DEATH);
        soundDataSet.addSound(SoundType.STEP, SoundEvents.PIGLIN_STEP);
        break;
      default:
        soundDataSet.addSound(SoundType.AMBIENT, SoundEvents.PIGLIN_AMBIENT);
        soundDataSet.addSound(SoundType.HURT, SoundEvents.PIGLIN_HURT);
        soundDataSet.addSound(SoundType.DEATH, SoundEvents.PIGLIN_DEATH);
        soundDataSet.addSound(SoundType.STEP, SoundEvents.PIGLIN_STEP);
    }
    soundDataSet.addSound(SoundType.TRADE, SoundEvents.VILLAGER_TRADE);
    soundDataSet.addSound(SoundType.TRADE_YES, SoundEvents.VILLAGER_YES);
    soundDataSet.addSound(SoundType.TRADE_NO, SoundEvents.VILLAGER_NO);
    return soundDataSet;
  }

  public enum Variant {
    PIGLIN,
    PIGLIN_BRUTE,
    ZOMBIFIED_PIGLIN
  }
}
