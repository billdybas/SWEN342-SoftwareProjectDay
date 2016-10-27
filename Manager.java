import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

public class Manager extends Employee implements Knowledgeable {

	private List<Team> teams;
	private Queue<Employee> waitingForAnswers = new ConcurrentLinkedQueue<Employee>();
	private CyclicBarrier standUpBarrier;
	private CyclicBarrier statusUpdateBarrier;
	private boolean hasEatenLunch;
	
	public Manager() {

		// The 3 TeamLead's Have to Arrive Before the Stand Up Meeting Begins
		this.standUpBarrier = new CyclicBarrier(3, new Runnable() {
			// Once Everyone Arrives, Meet for 15 Minutes
			@Override
			public void run(){
				try {
					Thread.sleep(15 * Time.MINUTE.getMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		// The 3 TeamLead's and 9 Developer's Have to Arrive Before the Status Update Meeting Begins
		this.statusUpdateBarrier = new CyclicBarrier(12, new Runnable() {
			// Once Everyone Arrives, Meet for 15 Minutes
			@Override
			public void run(){
				try {
					Thread.sleep(15 * Time.MINUTE.getMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		this.hasEatenLunch = false;
	}

	public void setTeams(List<Team> teams) {
		if (teams.size() != 3) {
			throw new IllegalArgumentException("The List of Teams Must Have Exactly 3 Teams");
		}
		this.teams = teams;
	}

	@Override
	public void run() {

		// Arrive at 8AM
		this.arrive();

		// Wait for all Team Leads to arrive
		try {
			System.out.println("Manager waits for Team Leads to arrive.");
			this.standUpBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

		// While the current time is before 4PM
		while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
			long delta = Workday.getDelta();

			if (delta >= Time.PM_TWO.getMillis()) {
				// After 2PM, the Manager Should Meet Until 3PM
				try {
					Thread.sleep(Time.PM_THREE.getMillis() - delta);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				// After 12PM, the Manager Should Eat Lunch if He Hasn't Already
				this.takeLunch();
			} else if (delta >= Time.AM_TEN.getMillis()) {
				// After 10AM, the Manager Should Meet Until 11AM
				try {
					Thread.sleep(Time.AM_ELEVEN.getMillis() - delta);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (this.waitingForAnswers.size() > 0) {
				// Answer the question of the first Employee in the Queue
				this.answerQuestion(this.waitingForAnswers.poll()); // TODO: Make sure employees in the Queue wait for their question to be answered
			} else {
				// TODO: Say that the Manager is Working
			}
		}

		// Wait for all Team Leads to arrive
		try {
			System.out.println("Manager waits for Team Leads to arrive.");
			this.statusUpdateBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

		this.leave();
	}

	public void knockOnDoor(Employee whoIsKnocking) {
		this.waitingForAnswers.add(whoIsKnocking);
	}

	public CyclicBarrier getStandUpBarrier() {
		return this.standUpBarrier;
	}

	public CyclicBarrier getStatusUpdateBarrier() {
		return this.statusUpdateBarrier;
	}

	@Override
	public void answerQuestion(Employee whoHasQuestion) {
		// Answering a Question takes 10 minutes
		try {
			this.wait(10 * Time.MINUTE.getMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void arrive() {
		// TODO: First to arrive at 8 AM

	}

	@Override
	public void takeLunch() {
		// Eating Lunch Always Takes an Hour
		this.hasEatenLunch = true;
		try {
			Thread.sleep(Time.HOUR.getMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void leave() {
		// TODO: Ensures that all employees have left
		// TODO: Leaves at 5 PM
	}
}
