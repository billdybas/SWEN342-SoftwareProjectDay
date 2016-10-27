import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class TeamLead extends Employee implements Knowledgeable, Curious {

	private Manager manager;
	private Queue<CyclicBarrier> waitingForAnswers = new ConcurrentLinkedQueue<CyclicBarrier>();
	private CyclicBarrier developerStandUpBarrier;
	private boolean hasEatenLunch;

	public TeamLead(Manager manager, int id, CountDownLatch latch) {
		this.manager = manager;
		this.id = id;
		this.latch = latch;
		
		final TeamLead me = this;
		this.developerStandUpBarrier = new CyclicBarrier(3, new Runnable() {
			@Override
			public void run() {
				ConferenceRoom.getReservation(me);
				try {
					System.out.println(Workday.timeString(getDelta) + ": The team starts the meeting");
					Thread.sleep(15 * Time.MINUTE.getMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Workday.timeString(getDelta) + ": The team leaves the conference room");
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
				System.out.println(Workday.timeString(getDelta) + ": TeamLead takes lunch");
				this.takeLunch();
				System.out.println(Workday.timeString(getDelta) + ": TeamLead comes back from lunch");
			} else if (this.waitingForAnswers.size() > 0) {
				// Answer the question of the first Employee in the Queue
				this.answerQuestion(this.waitingForAnswers.poll());
			} else if (rng.nextDouble() < 0.1) {
				// A Question is Asked 10% of the Time
				System.out.println(Workday.timeString(getDelta) + ": TeamLead goes to the manager for a question");
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
			System.out.println(Workday.timeString(getDelta) + ": TeamLead answers the question");
		}

		try {
			System.out.println(Workday.timeString(getDelta) + ": TeamLead goes to the manager to ask a question");
			// Answer the Question
			questionMeeting.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println(Workday.timeString(getDelta) + ": TeamLead comes back with an answer for the developer");
	}

	@Override
	public void askQuestion() {
		CyclicBarrier managerQuestionMeeting = new CyclicBarrier(1, new Runnable() {
			public void run(){
				try {
					System.out.println(Workday.timeString(getDelta) + ": Team lead asks the manager the question");
					this.wait(10 * Time.MINUTE.getMillis());
					System.out.println(Workday.timeString(getDelta) + ": Team lead leaves with their question answered");
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
