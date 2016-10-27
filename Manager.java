import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.CountDownLatch;

public class Manager extends Employee implements Knowledgeable {

	private Queue<CyclicBarrier> waitingForAnswers = new ConcurrentLinkedQueue<CyclicBarrier>();
	private CyclicBarrier standUpBarrier;
	private CyclicBarrier statusUpdateBarrier;
	private CountDownLatch latch;
	
	public Manager(CountDownLatch latch) {
		this.latch = latch;
		// The 3 TeamLead's Have to Arrive Before the Stand Up Meeting Begins
		this.standUpBarrier = new CyclicBarrier(3, new Runnable() {
			// Once Everyone Arrives, Meet for 15 Minutes
			@Override
			public void run(){
				try {
					Thread.sleep(15 * Time.MINUTE.getMillis());
					System.out.println("The team leads and the Manager have their standup until " + Workday.timeString(Workday.getDelta()));
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
					System.out.println("The firm gives a status update to the manager");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void run() {

		// Arrive at 8AM
		this.arrive();

		// Wait for all Team Leads to Arrive, then Meet
		try {
			System.out.println("Manager waits for Team Leads to arrive.");
			this.standUpBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

		// While the current time is before 4PM, Do Normal Routine
		while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
			long delta = Workday.getDelta();

			if (delta >= Time.PM_TWO.getMillis()) {
				// After 2PM, the Manager Should Meet Until 3PM
				try {
					Thread.sleep(Time.PM_THREE.getMillis() - delta);

					System.out.println("The Manager stays in the meeting until " + Workday.timeString(Workday.getDelta()));
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
				this.answerQuestion(this.waitingForAnswers.poll());
			} else {
				// TODO: Say that the Manager is Working
			}
		}

		// Anyone who is still waiting for questions to be answered is told to come back tomorrow
		for(CyclicBarrier question : waitingForAnswers){
			question.reset();
		}
		
		// Wait for all Members to arrive
		try {
			System.out.println("Manager waits for Everyone to arrive.");
			this.statusUpdateBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

		// Leave for the Day
		this.leave();
	}

	/**
	 * Indicate to the Manager that Waiting for the Answer to a Question
	 * @param questionMeeting
	 */
	public void knockOnDoor(CyclicBarrier questionMeeting) {
		this.waitingForAnswers.add(questionMeeting);
	}

	public CyclicBarrier getStandUpBarrier() {
		return this.standUpBarrier;
	}

	public CyclicBarrier getStatusUpdateBarrier() {
		return this.statusUpdateBarrier;
	}

	@Override
	public void answerQuestion(CyclicBarrier questionMeeting) {
		// Answering a Question takes 10 minutes
		try {
			questionMeeting.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void arrive() {
		// TODO: First to arrive at 8 AM
		System.out.println("The manager arrives at the firm at 8:00am");
		latch.countDown();
		// TODO: Open latch for all other employees to arrive
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
		long delta = Workday.getDelta();
		while(delta <= Time.PM_FIVE.getMillis()){ // TODO: Do we need a loop here?
			try {
				Thread.sleep(Time.PM_FIVE.getMillis() - delta);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.leave();
	}
}
