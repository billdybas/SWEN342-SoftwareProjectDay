
public class Workday {
	// Workday is a Singleton
	private static final Workday instance = new Workday();
	// The time when this program started
	private static long startTimeMillis;

	private Workday() {
		startTimeMillis = System.currentTimeMillis();
	}

	public static Workday getInstance() {
		return instance;
	}

	/**
	 * @return How Much Time Has Elapsed Since Program Start
	 */
	public static long getDelta() {
		return System.currentTimeMillis() - startTimeMillis;
	}

	/**
	 * @param time - Time to Format
	 * @return Formatted Workday Time - XX:XX AM/PM
	 */
	public static String timeString(long time) {
		String amOrPm; //AM or PM
		String hourString;
		String minuteString;

		int hour = (int)Math.floor(time / Time.HOUR.getMillis()) + 8;
		if (hour > 11) {
			amOrPm = "PM";
		}
		else {
			amOrPm = "AM";
		}
		if (hour > 12) {
			hour -= 12;
		}

		hourString = Long.toString(hour);

		int minute = (int)(time % Time.HOUR.getMillis()) / 10;
		minuteString = String.format("%02d", minute);

		return hourString + ":" + minuteString + " " + amOrPm;
	}
}
