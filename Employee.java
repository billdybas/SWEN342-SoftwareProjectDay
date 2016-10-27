import java.util.Random;

public abstract class Employee implements Runnable {

	protected boolean isEmployeeInFirm = false;
	protected boolean hasEatenLunch = false;
	protected Random rng = new Random();
	protected int id;

	public boolean isInFirm() {
		return isEmployeeInFirm;
	}

	public void arrive() {
		// Randomly Sleep Between 0 and 30 Minutes, and then arrive
		
		try {
			Thread.sleep(rng.nextInt((int)(Time.HALF_HOUR.getMillis())));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.isEmployeeInFirm = true;
	}

	public void takeLunch() {
		this.hasEatenLunch = true;
		try {
			// Takes Lunch for a Random Amount of Time Between 30 min and 1 Hour
			Thread.sleep(rng.nextInt((int)(Time.HOUR.getMillis() - Time.HALF_HOUR.getMillis())) + Time.HALF_HOUR.getMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void leave() {
		this.isEmployeeInFirm = false;
	}
}
