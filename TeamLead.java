import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

public class TeamLead extends Employee implements Knowledgeable, Curious {

	private Manager manager;
	private Queue<CyclicBarrier> waitingForAnswers = new ConcurrentLinkedQueue<CyclicBarrier>();
	private CyclicBarrier developerStandUpBarrier;
	private boolean hasEatenLunch;

	public TeamLead(Manager manager, int id) {
		this.manager = manager;
		this.id = id;
		
		final TeamLead me = this;
		this.developerStandUpBarrier = new CyclicBarrier(3, new Runnable() {
			@Override
			public void run() {
				ConferenceRoom.getReservation(me);
				try {
					Thread.sleep(15 * Time.MINUTE.getMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ConferenceRoom.releaseReservation(me);
			}
		});
	}

	@Override
	public void run() {

		this.arrive();

		// Tell the Manager They Have Arrived for the Stand Up Meeting
		try {
			manager.getStandUpBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

		// Wait for their Developers to arrive
		try {
			developerStandUpBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e1) {
			e1.printStackTrace();
		}

		// While the current time is before 4PM
		while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
			long delta = Workday.getDelta();

			if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				this.takeLunch();
			} else if (this.waitingForAnswers.size() > 0) {
				// Answer the question of the first Employee in the Queue
				this.answerQuestion(this.waitingForAnswers.poll());
			} else if (rng.nextDouble() < 0.1) {
				// A Question is Asked 10% of the Time
				this.askQuestion();
			} else {
				System.out.println("TeamLead X works.");
			}
		}

		try {
			manager.getStatusUpdateBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

		this.leave();
	}

	public CyclicBarrier getDeveloperStandUpBarrier() {
		return this.developerStandUpBarrier;
	}

	public Manager getManager() {
		return this.manager;
	}

	public void answerQuestion(CyclicBarrier questionMeeting) {
		if (rng.nextDouble() < 0.5) {
			this.askQuestion();
		}

		try {
			// Answer the Question
			questionMeeting.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void askQuestion() {
		CyclicBarrier managerQuestionMeeting = new CyclicBarrier(1, new Runnable() {
			public void run(){
				try {
					this.wait(10 * Time.MINUTE.getMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		// Ask the Manager a Question
		manager.knockOnDoor(managerQuestionMeeting);

		try {
			managerQuestionMeeting.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			System.out.println("TeamLead X Saves Their Question for Another Day");
		}
	}
}
