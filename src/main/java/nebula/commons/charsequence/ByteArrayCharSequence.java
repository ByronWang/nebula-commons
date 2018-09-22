/*
 * Copyright 2000-2016 JetBrains s.r.o.
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
package nebula.commons.charsequence;

import java.util.Arrays;

import nebula.commons.annotations.NotNull;
import nebula.commons.text.StringUtil;

public class ByteArrayCharSequence implements CharSequenceWithStringHash {
  private final int myStart;
  private final int myEnd;
  private transient int hash;
  private final byte[] myChars;

  private ByteArrayCharSequence(@NotNull byte[] chars) {
    this(chars, 0, chars.length);
  }
  public ByteArrayCharSequence(@NotNull byte[] chars, int start, int end) {
    myChars = chars;
    myStart = start;
    myEnd = end;
  }

  @Override
  public int hashCode() {
    int h = hash;
    if (h == 0) {
      hash = h = StringUtil.stringHashCode(this, myStart, myEnd);
    }
    return h;
  }

  @Override
  public final int length() {
    return myEnd - myStart;
  }

  @Override
  public final char charAt(int index) {
    return (char)myChars[index + myStart];
  }

  @NotNull
  @Override
  public CharSequence subSequence(int start, int end) {
    return start == 0 && end == length() ? this : new CharSequenceSubSequence(this, start, end);
  }

  @Override
  @NotNull
  public String toString() {
    return new String(myChars, myStart, length(), CharsetToolkit.ISO_8859_1_CHARSET);
  }

  @NotNull
  public static CharSequence convertToBytesIfAsciiString(@NotNull String name) {
    return convertToBytesIfAsciiString((CharSequence)name);
  }

  @NotNull
  public static CharSequence convertToBytesIfAsciiString(@NotNull CharSequence name) {
    int length = name.length();
    if (length == 0) return "";

    byte[] bytes = new byte[length];
    for (int i = 0; i < length; i++) {
      char c = name.charAt(i);
      if (c >= 128) {
        //noinspection RedundantStringConstructorCall
        return new String(name.toString()); // So we don't hold whole char[] buffer of a lengthy path on JDK 6
      }

      bytes[i] = (byte)c;
    }
    return new ByteArrayCharSequence(bytes);
  }

  @NotNull
  byte[] getBytes() {
    return myStart == 0 && myEnd == myChars.length ? myChars : Arrays.copyOfRange(myChars, myStart , myEnd);
  }
}
