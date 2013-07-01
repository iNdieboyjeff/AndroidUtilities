/**
 * Copyright © 2013 Jeff Sutton.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package util.android.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;

public class DateUtils {

	/**
	 * The masks used to validate and parse the input to this Atom date. These
	 * are a lot more forgiving than what the Atom spec allows. The forms that
	 * are invalid according to the spec are indicated.
	 */
	private static final String[] masks = { "yyyy-MM-dd'T'HH:mm:ss.SSSz",
			"yyyy-MM-dd't'HH:mm:ss.SSSz", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			"yyyy-MM-dd't'HH:mm:ss.SSS'z'", "yyyy-MM-dd'T'HH:mm:ssz",
			"yyyy-MM-dd't'HH:mm:ssz", "yyyy-MM-dd'T'HH:mm:ss'Z'",
			"yyyy-MM-dd't'HH:mm:ss'z'", "yyyy-MM-dd'T'HH:mmz",
			"yyyy-MM-dd't'HH:mmz", "yyyy-MM-dd'T'HH:mm'Z'",
			"yyyy-MM-dd't'HH:mm'z'", "yyyy-MM-dd", "yyyy MM dd", "yyyy-MM",
			"yyyy" };

	@SuppressLint("SimpleDateFormat")
	public static final Date parseAtomDate(String dateString) throws IllegalArgumentException {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		for (int n = 0; n < masks.length; n++) {
			try {
				sdf.applyPattern(masks[n]);
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				sdf.setLenient(true);
				d = sdf.parse(dateString, new ParsePosition(0));
				if (d != null)
					break;
			} catch (Exception e) {
			}
		}
		if (d == null)
			throw new IllegalArgumentException();
		return d;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static final String formatAtomDate(Date inDate) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(masks[0]);
		return sdf.format(inDate);
	}
}
