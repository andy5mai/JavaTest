package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil
{
	private static final int nSecOfMillis = 1000;
	
	private static Logger log = LoggerFactory.getLogger(DateUtil.class);

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat sdfDayFormat = new SimpleDateFormat("yyyyMMdd");
	
	public static long getMilliSecs(long nSecs)
	{
		return nSecs * nSecOfMillis;
	}
	
	public static long getMinMilliSecs(long nMins)
	{
		return nMins * 60 * nSecOfMillis;
	}
	
	public static long getHourMilliSecs(long nHours)
	{
		return nHours * getMinMilliSecs(60);
	}
	
	public static long getDayMilliSecs(long nDays)
	{
		return getHourMilliSecs(nDays * 24);
	}

	public static Date parse(String dateString)
	{
		try
		{
			return simpleDateFormat.parse(dateString);
		} 
		catch (ParseException e)
		{
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static String toString(Date date)
	{
		return simpleDateFormat.format(date);
	}
	
	public static String getDayStr(Date date)
	{
		return sdfDayFormat.format(date);
	}
	
	public static Date getDateByDiffDays(Calendar calendarStandard, Calendar calendarFormat, int nDiffDays)
	{
		calendarStandard.set(calendarStandard.get(Calendar.YEAR)
							 , calendarStandard.get(Calendar.MONTH)
							 , calendarStandard.get(Calendar.DATE) + nDiffDays
							 , calendarFormat.get(Calendar.HOUR_OF_DAY)
							 , calendarFormat.get(Calendar.MINUTE)
							 , calendarFormat.get(Calendar.SECOND));
		
		return calendarStandard.getTime();
	}
}
