
public abstract class Employee implements Runnable {

	private boolean isEmployeeInFirm = false;

	public boolean isInFirm() {
		return isEmployeeInFirm;
	}

	public void arrive() {
		this.isEmployeeInFirm = true;
	}

	public void takeLunch() {

	}

	public void leave() {
		this.isEmployeeInFirm = false;
	}
}
