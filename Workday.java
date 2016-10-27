
public class Workday {
	private static final Workday instance = new Workday();
	private static long startTimeMillis;

	private Workday() {
		startTimeMillis = System.currentTimeMillis();
	}

	public static Workday getInstance() {
		return instance;
	}

	public static long getDelta() {
		return System.currentTimeMillis() - startTimeMillis;
	}

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
