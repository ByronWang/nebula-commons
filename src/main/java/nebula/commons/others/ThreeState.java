/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * @author max
 */
package nebula.commons.others;

import nebula.commons.annotations.NotNull;

public enum ThreeState {
  YES, NO, UNSURE;

  public static ThreeState fromBoolean(boolean value) {
    return value ? YES : NO;
  }

  /**
   * Combine two different ThreeState values yielding UNSURE if values are different
   * and itself if values are the same.
   *
   * @param other other value to combine with this value
   * @return a result of combination of two ThreeState values
   */
  @NotNull
  public ThreeState merge(ThreeState other) {
    return this == other ? this : UNSURE;
  }

  public boolean toBoolean() {
    if (this == UNSURE) {
      throw new IllegalStateException("Must be or YES, or NO");
    }
    return this == YES;
  }
}