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

	public Manager() {}

	public Manager(List<Team> teams) {
		if (teams.size() != 3) {
			throw new IllegalArgumentException("The List of Teams Must Have Exactly 3 Teams");
		}

		this.teams = teams;
		this.standUpBarrier = new CyclicBarrier(3);
		this.statusUpdateBarrier = new CyclicBarrier(3);
	}

	public void setTeams(List<Team> teams) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Meet for 15 Minutes
		try {
			Thread.sleep(15 * Time.MINUTE.getMillis());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// While the current time is before 4PM
		while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
			long delta = Workday.getDelta();

			if (delta >= Time.PM_TWO.getMillis()) {
				try {
					Thread.sleep(Time.PM_THREE.getMillis() - delta);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				// TODO: Figure out lunch
			} else if (delta >= Time.AM_TEN.getMillis()) {
				try {
					Thread.sleep(Time.AM_ELEVEN.getMillis() - delta);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (this.waitingForAnswers.size() > 0) {
				// Answer the question of the first Employee in the Queue
				this.answerQuestion(this.waitingForAnswers.poll());
			}
		}

		// Wait for all Team Leads to arrive
		try {
			System.out.println("Manager waits for Team Leads to arrive.");
			this.statusUpdateBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Meet for 15 Minutes
		try {
			Thread.sleep(15 * Time.MINUTE.getMillis());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.leave();


		// switch (currentTime)
		// case 8AM
		//	arrive at work
		//  wait for leads to arrive
		//	15 minute standup meeting
		// case 10AM - 11AM
		//  1 hr meeting
		// case 12PM - 1PM (1 hour closest to this time after 12PM)
		//  eat lunch
		// case 2PM - 3PM
		//  1 hr meeting
		// case 4:15PM
		//  15 minute meeting about project status
		// case 5PM
		//  leave (always last person out)
		// default
		//	work until questions are answered
		//  (finishes answering questions and then goes to meetings or lunch)
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
			Thread.sleep(10 * Time.MINUTE.getMillis());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void arrive() {
		// First to arrive at 8 AM

	}

	@Override
	public void takeLunch() {
		// Always takes an hour lunch starting closest to 12 - 1 PM
	}

	@Override
	public void leave() {
		// Ensures that all employees have left
		// Leaves at 5 PM
	}
}
