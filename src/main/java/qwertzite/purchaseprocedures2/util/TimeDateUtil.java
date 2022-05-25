package qwertzite.purchaseprocedures2.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import qwertzite.purchaseprocedures2.magicconst.ConstDate;

public class TimeDateUtil {
	public static int getFinantialYear() {
		ZonedDateTime zdt = new Date().toInstant().atZone(ZoneId.systemDefault());
		return zdt.getYear() - (zdt.getMonthValue() < ConstDate.BEGINING_OF_FINANTIAL_YEAR ? -1 : 0);
	}
	
//	public static ZonedDateTime today() {
//		return ZonedDateTime.of(LocalDate.now(), LocalTime.MIN, ZoneId.systemDefault());
//	}
	
	public static int getLengthOfMonth(int year, int month) {
		if (year <= 0 || month <= 0) { return 31; }
		return Month.of(month).length(Year.isLeap(year));
	}
	
	public static String toISOString(LocalDate date) {
		return date.format(DateTimeFormatter.ISO_DATE);
	}
	
	public static LocalDate fromISOString(String string) {
		return LocalDate.parse(string);
	}
	
	private static final String[] JP_WEEK = new String[] { "月", "火", "水", "木", "金", "土", "日" };
	public static String jpWeek(DayOfWeek dayOfWeek) {
		return JP_WEEK[dayOfWeek.getValue() -1];
	}
	
	public static LocalDate last(LocalDate d1, LocalDate d2) {
		return (d1.isAfter(d2)) ? d1 : d2;
	}
}
