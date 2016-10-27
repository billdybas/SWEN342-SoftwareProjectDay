
public enum Time {

	MINUTE(10),
	HALF_HOUR(30 * MINUTE.getMillis()),
	HOUR(60 * MINUTE.getMillis()),
	WORKDAY(9 * HOUR.getMillis()),
	AM_TEN(2 * HOUR.getMillis()),
	AM_ELEVEN(3 * HOUR.getMillis()),
	PM_TWELVE(4 * HOUR.getMillis()),
	PM_ONE(5* HOUR.getMillis()),
	PM_TWO(6 * HOUR.getMillis()),
	PM_THREE(7 * HOUR.getMillis()),
	PM_FOUR(8 * HOUR.getMillis()),
	PM_FIVE(9 * HOUR.getMillis());
	
	long millis;

	Time(long millis) {
		this.millis = millis;
	}

	public long getMillis() {
		return this.millis;
	}
}
