/*
 * Copyright 2000-2017 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */
package nebula.commons.text;

import java.nio.charset.Charset;

import nebula.commons.annotations.NotNull;
import nebula.commons.annotations.Nullable;

/**
 * <p>
 * Identifies a line separator: either Unix ({@code \n}), Windows (@{code \r\n})
 * or (possible not actual anymore) Classic Mac ({@code \r}).
 * </p>
 * <p/>
 * <p>
 * The intention is to use this class everywhere, where a line separator is
 * needed, instead of just Strings.
 * </p>
 *
 * @author Kirill Likhodedov
 */
public enum LineSeparator {
	LF("\n"), CRLF("\r\n"), CR("\r");

	private final String mySeparatorString;
	private final byte[] myBytes;

	LineSeparator(@NotNull String separatorString) {
		mySeparatorString = separatorString;
		myBytes = separatorString.getBytes(Charset.forName("utf-8"));
	}

	@NotNull
	public static LineSeparator fromString(@NotNull String string) {
		for (LineSeparator separator : values()) {
			if (separator.getSeparatorString().equals(string)) {
				return separator;
			}
		}
		return getSystemLineSeparator();
	}

	@NotNull
	public String getSeparatorString() {
		return mySeparatorString;
	}

	@NotNull
	public byte[] getSeparatorBytes() {
		return myBytes;
	}

	public static boolean knownAndDifferent(@Nullable LineSeparator separator1, @Nullable LineSeparator separator2) {
		return separator1 != null && separator2 != null && !separator1.equals(separator2);
	}

	@NotNull
	public static LineSeparator getSystemLineSeparator() {
		return fromString(System.getProperty("line.separator"));
	}
}