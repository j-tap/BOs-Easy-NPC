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

package de.markusbordihn.easynpc.compat.epicfight.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EpicFightEntityTypes {
  private static final MobCategory CATEGORY = MobCategory.MISC;
  private static final int CLIENT_TRACKING_RANGE = 12;

  // public static final EntityType<EpicFightPiglin> PIGLIN =
  //    EntityType.Builder.of(EpicFightPiglin::new, CATEGORY)
  //        .sized(0.6F, 1.95F)
  //        .clientTrackingRange(CLIENT_TRACKING_RANGE)
  //        .build(EpicFightPiglin.ID);
  // public static final EntityType<EpicFightSkeleton> SKELETON =
  //    EntityType.Builder.of(EpicFightSkeleton::new, CATEGORY)
  //        .sized(0.6F, 1.95F)
  //        .clientTrackingRange(CLIENT_TRACKING_RANGE)
  //        .build(EpicFightSkeleton.ID);
  public static final EntityType<EpicFightZombie> ZOMBIE =
      EntityType.Builder.of(EpicFightZombie::new, CATEGORY)
          .sized(0.6F, 1.95F)
          .clientTrackingRange(CLIENT_TRACKING_RANGE)
          .build(EpicFightZombie.ID);

  private EpicFightEntityTypes() {}
}
