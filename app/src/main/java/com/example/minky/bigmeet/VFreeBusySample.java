package com.example.minky.bigmeet;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VFreeBusy;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.FreeBusy;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class VFreeBusySample {

	private static TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance()
			.createRegistry();
	private static VTimeZone vtimezone = registry.getTimeZone("Australia/Melbourne")
			.getVTimeZone();
	private static TimeZone timezone = new TimeZone(vtimezone);

	/*public static void requestBusy(Calendar calendar) {
		// request all busy times between today and 1 week from now..
		DateTime start = new DateTime();
		DateTime end = new DateTime(start.getTime() + 1000 * 60 * 60 * 24 * 7);

		VFreeBusy request = new VFreeBusy(start, end);
		VFreeBusy reply = new VFreeBusy(request, calendar.getComponents());
		System.out.println(reply);
		FreeBusy values = (FreeBusy) reply.getProperties().getProperty(
				"FREEBUSY");
		PeriodList periodList = values.getPeriods();
		Iterator iteratorTimes = periodList.iterator();
		while (iteratorTimes.hasNext()) {
			Period period = (Period) iteratorTimes.next();
			System.out.println("Start " + period.getRangeStart());
			System.out.println("End " + period.getRangeEnd());
		}

	}*/

	// Time Range
	// duration of meeting

	public static void requestFree(String startTimeStamp, String endTimeStamp,
								   String duration, Context mcontext, net.fortuna.ical4j.model.Calendar calendar) throws ParseException {
		// request all free cStartTime between (startTimeStamp & endTimeStamp) from
		// today till 1 week for a "duration" hours.

		// 1. Start the calendar for today
		DateTime start = new DateTime(getCalendar(startTimeStamp).getTime());
		start.setTimeZone(timezone);

		DateTime end = new DateTime(getCalendar(endTimeStamp).getTime());
		end.setTimeZone(timezone);

		VFreeBusy request = new VFreeBusy(start, end, new Dur(0,
				getHours(duration), getMinutes(duration), 0));

		//ReadCalendar reader = new ReadCalendar();
		//Calendar calendar = reader.createCalendar("C:\\ics\\File.ics");
/*		Calendar endOfDay = Calendar.getInstance();
		SimpleDateFormat formatterr = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
		Date dateCCC = formatterr.parse("04/27/2013 23:59:59");
		endOfDay.setTime(dateCCC);*/
		long calId = 2;
		Calendar cal = new GregorianCalendar(2012, 11, 14);
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long lstart = cal.getTimeInMillis();
		ContentValues cvalues = new ContentValues();
		cvalues.put(CalendarContract.Events.DTSTART, lstart);
		cvalues.put(CalendarContract.Events.DTEND, lstart);
		cvalues.put(CalendarContract.Events.RRULE,
				"FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
		cvalues.put(CalendarContract.Events.TITLE, "Some title");
		cvalues.put(CalendarContract.Events.EVENT_LOCATION, "Münster");
		cvalues.put(CalendarContract.Events.CALENDAR_ID, calId);
		cvalues.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Berlin");
		cvalues.put(CalendarContract.Events.DESCRIPTION,
				"The agenda or some description of the event");
// reasonable defaults exist:
		cvalues.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
		cvalues.put(CalendarContract.Events.SELF_ATTENDEE_STATUS,
				CalendarContract.Events.STATUS_CONFIRMED);
		cvalues.put(CalendarContract.Events.ALL_DAY, 1);
		cvalues.put(CalendarContract.Events.ORGANIZER, "some.mail@some.address.com");
		cvalues.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
		cvalues.put(CalendarContract.Events.GUESTS_CAN_MODIFY, 1);
		cvalues.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
		if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Uri uri = mcontext.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, cvalues);
		long eventId = new Long(uri.getLastPathSegment());
		VFreeBusy response = new VFreeBusy(request, calendar.getComponents());
		System.out.println(response);

		FreeBusy values = (FreeBusy) response.getProperties().getProperty(
				"FREEBUSY");
		PeriodList periodList = values.getPeriods();
		PeriodList compareLists = new PeriodList();

		Iterator iteratorTimes = periodList.iterator();
		while (iteratorTimes.hasNext()) {
			Period period = (Period) iteratorTimes.next();
			period.setTimeZone(timezone);
			System.out.println("Start " + period.getRangeStart());
			System.out.println("End " + period.getRangeEnd());
			System.out.println("duration " + period.getDuration());

			// Second while loop starts

			DateTime timeChunkStart = new DateTime(period.getRangeStart());
			timeChunkStart.setTimeZone(timezone);

			DateTime timeChunkEnd = null;
			// new DateTime(period.getRangeEnd());
			// timeChunkEnd.setTimeZone(timezone);

			DateTime periodEnd = new DateTime(period.getRangeEnd());

			while (timeChunkStart.before(periodEnd)) {
				timeChunkEnd = new DateTime(getupdatedTimeChunk(timeChunkStart,
						duration));
				if (timeChunkEnd.after(periodEnd))
					break;
				Period timeChunk = new Period(timeChunkStart, timeChunkEnd);
				compareLists.add(timeChunk);
				timeChunkStart = timeChunkEnd;
			}

		}
		System.out.println(compareLists.size());
		iteratorTimes = compareLists.iterator();
		while (iteratorTimes.hasNext()) {
			Period period = (Period) iteratorTimes.next();
			period.setTimeZone(timezone);
			System.out.println("Start " + period.getRangeStart());
			System.out.println("End " + period.getRangeEnd());
			System.out.println("duration " + period.getDuration());
		}
	}

	private static int getHours(String HoursMinutes) {
		Integer hours = null;
		if (HoursMinutes != null) {
			String hrs = HoursMinutes.substring(0, 2);
			hours = new Integer(hrs);
		}
		return hours;

	}

	private static int getMinutes(String HoursMinutes) {
		Integer minutes = null;
		if (HoursMinutes != null) {
			String min = HoursMinutes.substring(2, 4);
			minutes = new Integer(min);
		}
		return minutes;

	}

	private static Calendar getCalendar(String hhmm) {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTimeZone(timezone);
		if (hhmm != null) {
			startCalendar.set(Calendar.HOUR_OF_DAY, getHours(hhmm));
			startCalendar.set(Calendar.MINUTE, getMinutes(hhmm));
			startCalendar.set(Calendar.SECOND, 0);
		}
		return startCalendar;
	}


	private static Date getupdatedTimeChunk(Date date,
			String hhmmaddition) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(timezone);
		if (date != null && hhmmaddition != null) {
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, getHours(hhmmaddition));
			calendar.add(Calendar.MINUTE, getMinutes(hhmmaddition));
		}
		return calendar.getTime();
	}

}
