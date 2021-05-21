package com.zoo.ninestar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	private static ThreadLocal<SimpleDateFormat> tlPartWith = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		};

		public SimpleDateFormat get() {
			SimpleDateFormat sdf = super.get();
			if (sdf == null) {
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			}
			return sdf;
		};
	};

	private static ThreadLocal<SimpleDateFormat> tlPartNo = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		};

		public SimpleDateFormat get() {
			SimpleDateFormat sdf = super.get();
			if (sdf == null) {
				sdf = new SimpleDateFormat("yyyyMMdd");
			}
			return sdf;
		};
	};

	private static ThreadLocal<SimpleDateFormat> tlAllWith = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		};

		public SimpleDateFormat get() {
			SimpleDateFormat sdf = super.get();
			if (sdf == null) {
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			return sdf;
		};
	};

	private static ThreadLocal<SimpleDateFormat> tlAllNo = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		};

		public SimpleDateFormat get() {
			SimpleDateFormat sdf = super.get();
			if (sdf == null) {
				sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			}
			return sdf;
		};
	};

	public static Date parsePartWith(String source) throws ParseException {
		return tlPartWith.get().parse(source);
	}

	public static String formatPartWith(Date source) {
		return tlPartWith.get().format(source);
	}

	public static Date parsePartNo(String source) throws ParseException {
		return tlPartNo.get().parse(source);
	}

	public static String formatPartNo(Date source) {
		return tlPartNo.get().format(source);
	}

	public static Date parseAllWith(String source) throws ParseException {
		return tlAllWith.get().parse(source);
	}

	public static String formatAllWith(Date source) {
		return tlAllWith.get().format(source);
	}

	public static Date parseAllNo(String source) throws ParseException {
		return tlAllNo.get().parse(source);
	}

	public static String formatAllNo(Date source) {
		return tlAllNo.get().format(source);
	}

	/**
	 * 将时间秒转换成日期形式（例：19000101）
	 * 
	 * @param timeSecond
	 * @return
	 */
	public static int convertTimeSecondsToDate(int timeSecond) {
		int rs = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		long timeLong = timeSecond * 1000L;
		String rsStr = df.format(new Date(timeLong));
		rs = Integer.parseInt(rsStr);
		return rs;
	}
	public static int convertDateToTimeSeconds(int date) {
		int rs = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date time=null;
		try {
			time = df.parse(""+date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (int) (time.getTime()/1000);
	}

	/**
	 * 获取当前的日期（19000101）
	 * 
	 * @return
	 */
	public static int getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(df.format(new Date()));
	}

	public static int getCurrentSecondsInt() {
		int current = 0;
		current = (int) (new Date().getTime() / 1000);
		return current;
	}

	/**
	 * 获取当前的日期（1900-01-01）
	 * 
	 * @return
	 */
	public static String getCurrentDateStr() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date());
	}

	public static int getCurrentTimeMinutes(){
		SimpleDateFormat df=new SimpleDateFormat("mm");
		return Integer.parseInt(df.format(new Date()));
	}
	
	/**
	 * 获取指定日期向前或者向后天数
	 * 
	 * @param date
	 *            格式：yyyy-MM-dd
	 * @return 返回格式为：yyyy-MM-dd
	 * @throws ParseException
	 */
	public static String getDateChangeDay(String date, int changeDay) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date tmpDate = sdf.parse(date);
		Calendar c = Calendar.getInstance();
		c.setTime(tmpDate);
		c.add(Calendar.DAY_OF_MONTH, changeDay);
		tmpDate = c.getTime();
		return sdf.format(tmpDate);
	}

	/**
	 * 获取指定日期向前或者向后天数
	 * 
	 * @param date
	 *            格式：yyyyMMdd
	 * @return 返回格式为：yyyyMMdd
	 * @throws ParseException
	 */
	public static String getDateChangeDay(int date, int changeDay) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date tmpDate = sdf.parse(date + "");
		Calendar c = Calendar.getInstance();
		c.setTime(tmpDate);
		c.add(Calendar.DAY_OF_MONTH, changeDay);
		tmpDate = c.getTime();
		return sdf.format(tmpDate);
	}

	/**
	 * 将字符串日期转换为秒时
	 * 
	 * @param time
	 *            格式：yyyyMMdd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public static int convertDateToTimeSeconds(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		if (time == null) {
			return 0;
		}
		int second = (int) (sdf.parse(time).getTime() / 1000);
		return second;
	}

	/**
	 * 时间格式化
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDateTime(String time) throws ParseException {
		String format = "HH:mm:ss";
		if (time.trim().split(":").length == 2) {
			format = "HH:mm";
		}

		Date nowTime = new SimpleDateFormat(format).parse(time);
		return nowTime;

	}

	public static Date formatDateStr(String time) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date nowTime = format.parse(time);
		return nowTime;

	}

	public static Date formatDatetime(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date nowTime = format.parse(date);
		return nowTime;

	}

	public static Date formatDatetime1(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowTime = format.parse(date);
		return nowTime;

	}

	public static Date formatDateTime(Date time) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String nowTime = format.format(time);
		return format.parse(nowTime);

	}

	public static String convertDateToString(Date time) {
		String timeStr = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStr = format.format(time);
		return timeStr;
	}

	public static String nowDate() {
		String timeStr = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStr = format.format(new Date());
		return timeStr;
	}
	/**
	 * 判断时间是否在时间段内
	 * 
	 * @return
	 * @author xzz
	 * @time 2019/12/18
	 */
	public static boolean belongCalendar(Date cydate, String startTime, String endTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Date now = null;
		Date beginDate = null;
		Date endDate = null;
		try {
			now = df.parse(df.format(cydate));
			beginDate = df.parse(startTime);
			endDate = df.parse(endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar date = Calendar.getInstance();
		date.setTime(now);

		Calendar begin = Calendar.getInstance();
		begin.setTime(beginDate);

		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}
	public static void main(String[] args) {

	}

}
