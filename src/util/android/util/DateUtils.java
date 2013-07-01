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
	 * The masks used to validate and parse the input to an Atom date. These are
	 * a lot more forgiving than what the Atom spec allows.
	 */
	private static final String[] atomMasks = { "yyyy-MM-dd'T'HH:mm:ss.SSSz",
			"yyyy-MM-dd't'HH:mm:ss.SSSz", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			"yyyy-MM-dd't'HH:mm:ss.SSS'z'", "yyyy-MM-dd'T'HH:mm:ssz",
			"yyyy-MM-dd't'HH:mm:ssz", "yyyy-MM-dd'T'HH:mm:ss'Z'",
			"yyyy-MM-dd't'HH:mm:ss'z'", "yyyy-MM-dd'T'HH:mmz",
			"yyyy-MM-dd't'HH:mmz", "yyyy-MM-dd'T'HH:mm'Z'",
			"yyyy-MM-dd't'HH:mm'z'", "yyyy-MM-dd", "yyyy MM dd", "yyyy-MM",
			"yyyy" };

	private static final String[] ordinalMasks = { "EEEE d MMMM yyyy HH:mm:ss",
			"EEEE d MMMM yyyy" };

	/**
	 * Format a Date object as an Atom Date/Time String.
	 * 
	 * @param inDate
	 * @return String
	 */
	@SuppressLint("SimpleDateFormat")
	public static final String formatAtomDate(Date inDate) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(atomMasks[0]);
		return sdf.format(inDate);
	}

	/**
	 * Parse an Atom date String into Date object. This is a fairly lenient
	 * parse and does not require the date String to conform exactly.
	 * 
	 * @param dateString
	 * @return Date
	 * @throws IllegalArgumentException
	 */
	@SuppressLint("SimpleDateFormat")
	public static final Date parseAtomDate(String dateString)
			throws IllegalArgumentException {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		for (int n = 0; n < atomMasks.length; n++) {
			try {
				sdf.applyPattern(atomMasks[n]);
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

	/**
	 * Try to parse an ordinal date in the format:
	 * 
	 * Monday 1st July 2013
	 * 
	 * SimpleDateFormat doesn't like st, nd, th, rd in dates so we modify the
	 * input String before processing.
	 * 
	 * This function assumes GMT timezone.
	 * 
	 * @param dateString
	 * @return Date
	 */
	public static final Date parseOrdinalDate(String dateString)
			throws IllegalArgumentException {
		return parseOrdinalDate(dateString, TimeZone.getTimeZone("GMT"));
	}

	/**
	 * Try to parse an ordinal date in the format:
	 * 
	 * Monday 1st July 2013
	 * 
	 * SimpleDateFormat doesn't like st, nd, th, rd in dates so we modify the
	 * input String before processing.
	 * 
	 * @param dateString
	 * @param timezone
	 * @return Date
	 */
	public static final Date parseOrdinalDate(String dateString,
			TimeZone timezone) throws IllegalArgumentException {
		dateString = dateString.trim()
				.replaceAll("([0-9]+)(?:st|nd|rd|th)?", "$1")
				.replace("  ", " ");
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		for (int n = 0; n < ordinalMasks.length; n++) {
			try {
				sdf.applyPattern(ordinalMasks[n]);
				sdf.setTimeZone(timezone);
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
}
