
public abstract class Employee implements Runnable {

	private boolean isEmployeeInFirm = false;

	public boolean isInFirm() {
		return isEmployeeInFirm;
	}

	public void arrive() {
		// Randomly Sleep Between 0 and 30 Minutes, and then arrive
		try {
			// TODO: Make Random
			Thread.sleep(15 * Time.MINUTE.getMillis());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.isEmployeeInFirm = true;
	}

	public void takeLunch() {

	}

	public void leave() {
		this.isEmployeeInFirm = false;
	}
}
