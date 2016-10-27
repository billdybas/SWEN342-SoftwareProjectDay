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
	private boolean firstMeeting = false;
	private boolean secondMeeting = false;
	private boolean readyForStatusMeeting = false;
	
	public Manager(CountDownLatch latch) {
		this.latch = latch;
		
		// The 3 TeamLead's Have to Arrive Before the Stand Up Meeting Begins
		this.standUpBarrier = new CyclicBarrier(4, new Runnable() {
			// Once Everyone Arrives, Meet for 15 Minutes
			@Override
			public void run(){
				try {
					System.out.println(Workday.timeString(Workday.getDelta()) + ": The Manager leads the stand-up meeting");
					Thread.sleep(15 * Time.MINUTE.getMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		// The 3 TeamLead's and 9 Developer's Have to Arrive Before the Status Update Meeting Begins
		this.statusUpdateBarrier = new CyclicBarrier(13, new Runnable() {
			// Once Everyone Arrives, Meet for 15 Minutes
			@Override
			public void run(){
				try {
					System.out.println(Workday.timeString(Workday.getDelta()) + ": The Manager gives the daily status meeting");
					Thread.sleep(15 * Time.MINUTE.getMillis());
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
			System.out.println(Workday.timeString(Workday.getDelta()) + ": The Manager waits for Team Leads to arrive.");
			this.standUpBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

		// While the current time is before 4PM, Do Normal Routine
		while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
			long delta = Workday.getDelta();

			if (delta >= Time.PM_TWO.getMillis() && !this.secondMeeting) {
				// After 2PM, the Manager Should Meet Until 3PM
				try {
					synchronized(this){
						System.out.println(Workday.timeString(delta) + ": The Manager has his 2PM meeting");
						this.secondMeeting = true;
						Thread.sleep(Time.PM_THREE.getMillis() - delta);
					}

					System.out.println("The Manager stays in the meeting until " + Workday.timeString(delta));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				// After 12PM, the Manager Should Eat Lunch if He Hasn't Already
				this.takeLunch();
			} else if (delta >= Time.AM_TEN.getMillis() && !this.firstMeeting) {
				// After 10AM, the Manager Should Meet Until 11AM
				try {
					synchronized(this){
						System.out.println(Workday.timeString(delta) + ": The Manager has his 10AM meeting");
						this.firstMeeting = true;
						Thread.sleep(Time.AM_ELEVEN.getMillis() - delta);
						System.out.println("The Manager stays in the meeting until " + Workday.timeString(delta));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (this.waitingForAnswers.size() > 0) {
				// Answer the question of the first Employee in the Queue
				this.answerQuestion(this.waitingForAnswers.poll());
			} else {
				System.out.println(Workday.timeString(Workday.getDelta()) + ": The Manager browses WOOT.com");
				try {
					Thread.sleep(Time.MINUTE.getMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		// Anyone who is still waiting for questions to be answered is told to come back tomorrow
		for(CyclicBarrier question : waitingForAnswers){
			question.reset();
		}
		
		this.readyForStatusMeeting = true;
		
		// Wait for all Members to arrive
		try {
			System.out.println(Workday.timeString(Workday.getDelta()) + ": The Manager waits for everyone to arrive to the status meeting.");
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
	
	public boolean readyForStatusMeeting() {
		return this.readyForStatusMeeting;
	}

	@Override
	public void answerQuestion(CyclicBarrier questionMeeting) {
		// Answering a Question takes 10 minutes
		try {
			System.out.println(Workday.timeString(Workday.getDelta()) + ": The Manager answers a question");
			questionMeeting.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void arrive() {
		// The Manager is the First to Arrive
		System.out.println(Workday.timeString(Workday.getDelta()) + ": The Manager arrives at the firm at 8:00am");
		// Let Other Employees In
		latch.countDown();
	}

	@Override
	public void takeLunch() {
		// Eating Lunch Always Takes an Hour
		this.hasEatenLunch = true;
		try {
			System.out.println(Workday.timeString(Workday.getDelta()) + ": The Manager takes his hour lunch break");
			Thread.sleep(Time.HOUR.getMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void leave() {
		long delta = Workday.getDelta();
		try {
			Thread.sleep(Time.PM_FIVE.getMillis() - delta);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(Workday.timeString(Workday.getDelta()) + ": The Manager leaves for the day");
		super.leave();
	}
}
