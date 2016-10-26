
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
}
