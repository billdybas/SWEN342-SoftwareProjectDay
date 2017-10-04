import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class TeamLead extends Employee implements Knowledgeable, Curious {

	private Manager manager;
	private Queue<CyclicBarrier> waitingForAnswers = new ConcurrentLinkedQueue<CyclicBarrier>();
	private CyclicBarrier developerStandUpBarrier;
	private boolean hasMetWithManager = false;
	
	public TeamLead(Manager manager, int id, CountDownLatch latch) {
		this.manager = manager;
		this.id = id;
		this.latch = latch;
		
		final TeamLead me = this;
		// A TeamLead's 3 Developers (and the TeamLead itself) Have to Arrive Before the Stand Up Begins
		this.developerStandUpBarrier = new CyclicBarrier(4, new Runnable() {
			@Override
			public void run() {
				// They wait for the ConferenceRoom to be available
				ConferenceRoom.getReservation(me);
				// Then, they meet for 15 min
				try {
					System.out.println(Workday.timeString(Workday.getDelta()) + ": The team starts the meeting");
					Thread.sleep(15 * Time.MINUTE.getMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Let other teams know the ConferenceRoom is available
				System.out.println(Workday.timeString(Workday.getDelta()) + ": The team leaves the conference room");
				ConferenceRoom.releaseReservation(me);
			}
		});
	}

	@Override
	public void run() {
		// Arrive at the Office
		this.arrive();

		// Tell the Manager They Have Arrived for the Stand Up Meeting and then Meet for 15 min
		try {
			manager.getStandUpBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		
		this.hasMetWithManager = true;

		// Wait for their Developers to Arrive and then Meet for 15 min
		try {
			developerStandUpBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e1) {
			e1.printStackTrace();
		}

		// While the current time is before 4PM, Do Normal Routine
		while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
			long delta = Workday.getDelta();

			// If at least 12PM and hasn't eaten lunch, take lunch
			if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead  " + this.id + " goes to lunch");
				this.takeLunch();
				System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead  " + this.id + " comes back from lunch");
			} else if (this.waitingForAnswers.size() > 0) {
				// Answer the question of the first Employee in the Queue
				this.answerQuestion(this.waitingForAnswers.poll());
			} else if (rng.nextDouble() < 0.1) {
				// A Question is Asked 10% of the Time
				System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead " + this.id + " goes to the Manager for a question");
				this.askQuestion();
			} else {
				// Otherwise, work
				System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead " + this.id + " works.");
			}
		}

		while(!this.manager.readyForStatusMeeting()) {}
		
		// Wait for the Status Update to Start and then Meet for 15 min
		try {
			manager.getStatusUpdateBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

    	// Leave for the Day
		this.leave();
	}

	public CyclicBarrier getDeveloperStandUpBarrier() {
		return this.developerStandUpBarrier;
	}

	public synchronized Manager getManager() {
		return this.manager;
	}
	
	public synchronized boolean hasMetWithManager() {
		return this.hasMetWithManager;
	}

	@Override
	public void answerQuestion(CyclicBarrier questionMeeting) {
		// 50% of the Time, the Team Lead Can't Answer the Question and has to Ask the Manager
		if (rng.nextDouble() < 0.5) {
			this.askQuestion();
			System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead " + this.id + " answers the question");
		}

		try {
			// Answer the (Original) Question
			System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead " + this.id + " goes to the Manager to ask a question");
			questionMeeting.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead " + this.id + " comes back with an answer for the Developer");
	}

	@Override
	public void askQuestion() {
		// Questions Asked to the Manager take 10 min
		final TeamLead me = this;
		CyclicBarrier managerQuestionMeeting = new CyclicBarrier(1, new Runnable() {
			public void run(){
				try {
					System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead " + me.id + " asks the manager the question");
					Thread.sleep(10 * Time.MINUTE.getMillis());
					System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead " + me.id + " leaves with their question answered");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});


		// Ask the Manager a Question
		manager.knockOnDoor(managerQuestionMeeting);

		try {
			// Wait for Manager's Response
			managerQuestionMeeting.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			System.out.println(Workday.timeString(Workday.getDelta()) + ": TeamLead " + this.id + " Saves Their Question for Another Day");
		}
	}
}
