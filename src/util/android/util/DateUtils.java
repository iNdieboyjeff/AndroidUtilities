/**
 * Copyright © 2013-2014 Jeff Sutton.
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

import java.net.InetAddress;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

	/**
	 * The masks used to validate and parse the input to an Atom date. These are a lot more forgiving than what the Atom
	 * spec allows.
	 */
	private static final String[] atomMasks = {
			"yyyy-MM-dd'T'HH:mm:ss.SSSz", "yyyy-MM-dd't'HH:mm:ss.SSSz", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			"yyyy-MM-dd't'HH:mm:ss.SSS'z'", "yyyy-MM-dd'T'HH:mm:ssz", "yyyy-MM-dd't'HH:mm:ssz",
			"yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd't'HH:mm:ss'z'", "yyyy-MM-dd'T'HH:mmz", "yyyy-MM-dd't'HH:mmz",
			"yyyy-MM-dd'T'HH:mm'Z'", "yyyy-MM-dd't'HH:mm'z'", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss",
			"yyyy-MM-dd", "yyyy MM dd", "yyyy-MM", "yyyy"
	};

	private static final String[] ordinalMasks = {
			"EEEE d MMMM yyyy HH:mm:ss", "EEEE d MMMM yyyy"
	};

	private static final String[] timeMasks = {
			"HH:mm:ss.SSS", "HH:mm:ss", "HH:mm"
	};

	static String[] suffixes = {
			// 0 1 2 3 4 5 6 7 8 9
			"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
			// 10 11 12 13 14 15 16 17 18 19
			"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
			// 20 21 22 23 24 25 26 27 28 29
			"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
			// 30 31
			"th", "st"
	};

	public static final int TIME_FORMAT_MICRO = 0;
	public static final int TIME_FORMAT_SECONDS = 1;
	public static final int TIME_FORMAT_MINUTES = 2;
	public static final String[] TIME_SERVER = {"2.android.pool.ntp.org", "time.nist.gov", "pool.ntp.org"};
	
	public static final String getNTPServer() {
		int min = 0;
		int max = TIME_SERVER.length;

		Random r = new Random();
		int i1 = r.nextInt(max - 1);
		
		return TIME_SERVER[i1];
	}

	/**
	 * <p>
	 * Parse the supplied date and return an ISO/SQL compatable date string in the format <i>yyyy-mm-dd</i>.
	 * </p>
	 * 
	 * @param inDate
	 * @return String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateAsString(Date inDate) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd");
		return sdf.format(inDate);
	}

	/**
	 * <P>
	 * Given a supplied day in the month value, return the correct number suffix for use in display strings.
	 * </p>
	 * <p>
	 * <i>e.g.</i> 1st, 2nd, 3rd, 4th...
	 * 
	 * @param dayOfMonth
	 * @return String suffix (st, rd, th, nd)
	 */
	public static String getDaySuffix(int dayOfMonth) {
		return suffixes[dayOfMonth];
	}

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

	public static final String formatLongDate(Date inDate) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(ordinalMasks[1]);
		return sdf.format(inDate);
	}

	public static final String formatDateName(Date inDate) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("EEEE");
		return sdf.format(inDate);
	}

	/**
	 * Parse an Atom date String into Date object. This is a fairly lenient parse and does not require the date String
	 * to conform exactly.
	 * 
	 * @param dateString
	 * @return Date
	 * @throws IllegalArgumentException
	 */
	@SuppressLint("SimpleDateFormat")
	public static final Date parseAtomDate(String dateString, TimeZone timezone) throws IllegalArgumentException {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		for (int n = 0; n < atomMasks.length; n++) {
			try {
				sdf.applyPattern(atomMasks[n]);
				sdf.setTimeZone(timezone);
				sdf.setLenient(true);
				d = sdf.parse(dateString, new ParsePosition(0));
				if (d != null)
					break;
			} catch (Exception e) {
			}
		}
		if (d == null) {
			Log.e("DateUtils", "Cannot parse: " + dateString);
			throw new IllegalArgumentException();
		}
		return d;
	}

	public static final Date parseAtomDate(String dateString) {
		return parseAtomDate(dateString, TimeZone.getTimeZone("UTC"));
	}

	@SuppressLint("SimpleDateFormat")
	public static final Date parseTime(String dateString) throws IllegalArgumentException {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		for (int n = 0; n < timeMasks.length; n++) {
			try {
				sdf.applyPattern(timeMasks[n]);
				// sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
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
	public static final String formatTime(Date time, int format, TimeZone timezone) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(timeMasks[format]);
		sdf.setTimeZone(timezone);
		return sdf.format(time);
	}

	public static final String formatTime(Date time, int format) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(timeMasks[format]);
		return sdf.format(time);
	}

	/**
	 * <p>
	 * Try to parse an ordinal date in the format:
	 * </p>
	 * 
	 * <p>
	 * Monday 1st July 2013
	 * </p>
	 * 
	 * <p>
	 * SimpleDateFormat doesn't like st, nd, th, rd in dates so we modify the input String before processing.
	 * </p>
	 * 
	 * <p>
	 * This function assumes GMT timezone.
	 * </p>
	 * 
	 * @param dateString
	 * @return Date
	 */
	public static final Date parseOrdinalDate(String dateString) throws IllegalArgumentException {
		return parseOrdinalDate(dateString, TimeZone.getTimeZone("GMT"));
	}

	public static Date getTwitterDate(String date) throws ParseException {
		final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		sf.setLenient(true);
		return sf.parse(date);
	}

	/**
	 * Try to parse an ordinal date in the format:
	 * 
	 * Monday 1st July 2013
	 * 
	 * SimpleDateFormat doesn't like st, nd, th, rd in dates so we modify the input String before processing.
	 * 
	 * @param dateString
	 * @param timezone
	 * @return Date
	 */
	@SuppressLint("SimpleDateFormat")
	public static final Date parseOrdinalDate(String dateString, TimeZone timezone) throws IllegalArgumentException {
		dateString = dateString.trim().replaceAll("([0-9]+)(?:st|nd|rd|th)?", "$1").replace("  ", " ");
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

	public static final String getDurationString(Date start, Date end) {
		return getDurationString(end.getTime() - start.getTime());
	}

	public static final String getDurationString(long duration) {
		duration = duration / 1000;
		int hour = (int) (duration / 3600);
		int min = (int) ((duration - hour * 3600) / 60);
		int seconds = (int) ((duration - hour * 3600) % 60);
		StringBuilder builder = new StringBuilder();
		if (hour != 0 && hour == 1)
			builder.append(hour).append(" hour");
		if (hour != 0 && hour > 1)
			builder.append(hour).append(" hours");
		if (hour != 0 && min != 0)
			builder.append(" ");
		if (min != 0)
			builder.append(min).append(" mins");

		if (hour < 1 && min < 1 && seconds != 0)
			builder.append(seconds).append(" seconds");
		return builder.toString();
	}

	public static final Date getTomorrow() {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, 1); // <--
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 00);
		Date tomorrow = cal.getTime();

		return tomorrow;
	}

	public static final Date getToday() {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, 0); // <--
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 00);
		Date tomorrow = cal.getTime();

		return tomorrow;
	}

	public static final Date getTodayPlus(int d) {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, d); // <--
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 00);
		Date tomorrow = cal.getTime();

		return tomorrow;
	}

	public static final Date getDatePlus(Date d, int days) {
		Date now = d;
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, days); // <--
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 00);
		Date tomorrow = cal.getTime();

		return tomorrow;
	}

	public static final Date getDate(String d) {
		Date now = util.android.util.DateUtils.parseAtomDate(d);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, 0); // <--
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 00);
		Date tomorrow = cal.getTime();

		return tomorrow;
	}

	public static final boolean isYesterday(Date date) {
		Calendar c1 = Calendar.getInstance(); // today
		c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

		Calendar c2 = Calendar.getInstance();
		c2.setTime(date); // your date

		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
			return true;
		}
		return false;
	}

	public static final boolean isToday(Date date) {
		Calendar c1 = Calendar.getInstance(); // today
		c1.add(Calendar.DAY_OF_YEAR, 0); // yesterday

		Calendar c2 = Calendar.getInstance();
		c2.setTime(date); // your date

		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
			return true;
		}
		return false;
	}

	public static final boolean isTomorrow(Date date) {
		Calendar c1 = Calendar.getInstance(); // today
		c1.add(Calendar.DAY_OF_YEAR, 1); // yesterday

		Calendar c2 = Calendar.getInstance();
		c2.setTime(date); // your date

		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
			return true;
		}
		return false;
	}

	public static final Date getNTPTime() {
		NTPUDPClient timeClient = new NTPUDPClient();

		TimeInfo timeInfo;
		try {
			InetAddress inetAddress = InetAddress.getByName(getNTPServer());
			timeInfo = timeClient.getTime(inetAddress);

			long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
			Date time = new Date(returnTime);
			Log.i("DateUtils", "---- getNTPTime() " + inetAddress.getCanonicalHostName() + " (" + inetAddress.getHostAddress() + ") ----------");
			Log.i("DateUtils", "---- getNTPTime() " + formatAtomDate(time) + " ----------");
			Log.i("DateUtils", "---- getNTPTime() delay: " + timeInfo.getDelay() + ", offset: " + timeInfo.getOffset()
					+ " ----------");
			Log.i("DateUtils", "---- getNTPTime() " + new String(timeInfo.getMessage().getDatagramPacket().getData())
					+ " ----------");
			return time;
		} catch (Exception e) {
			Log.e("DateUtils", "---- getNTPTime() ERROR ----------");
			e.printStackTrace();
			return new Date();
		}
	}

}
