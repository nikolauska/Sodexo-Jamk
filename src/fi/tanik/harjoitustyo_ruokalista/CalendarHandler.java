package fi.tanik.harjoitustyo_ruokalista;

import java.io.Serializable;
import java.util.Calendar;

/*
 * 	This class hold information of current date	
 * 
 *	It also handles calender functions like changing date,
 *	getting weekday etc. 
 */

public class CalendarHandler implements Serializable {	
	private static final long serialVersionUID = -1278383000993017611L;
	/***/
	public int day;
	public int month;
	public int year;
	public int week;
	private Calendar calendar;
	
	public CalendarHandler() {		
		calendar = Calendar.getInstance();
		
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
		
		week = calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	// Copy constructor
	public CalendarHandler(CalendarHandler calendar) {		
		this.calendar = calendar.getCalendar();
		
		day = calendar.day;
		month = calendar.month;
		year = calendar.year;
		
		week = calendar.week;
	}
	
	// Sets calendar back to current day
	public void SetToday() {
		calendar = Calendar.getInstance();
		
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
		
		week = calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	
	// check if date user is checking is today
	 public boolean DayToday() {
	  Calendar temp = Calendar.getInstance();
	  if(temp.get(Calendar.DAY_OF_MONTH) == day &&
	     (temp.get(Calendar.MONTH) + 1) == month &&
	   temp.get(Calendar.YEAR) == year)
	   return true;
	  else
	   return false;
	 }
	
	// Switch to next or last day
	public void AddDay(int amount) {
		calendar.add(Calendar.DAY_OF_YEAR, amount);
		
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
		
		week = calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	// Gets week day like monday
	public int getWeekDate() {
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	// return calendar (needed with copy constructor)
	public Calendar getCalendar() {
		return calendar;
	}
}
