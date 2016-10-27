import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

public class TeamLead extends Employee implements Knowledgeable, Curious {

	private Manager manager;
	private Queue<Employee> waitingForAnswers = new ConcurrentLinkedQueue<Employee>();
	private CyclicBarrier developerStandUpBarrier;
	private boolean hasEatenLunch;
		
	public TeamLead(Manager manager) {
		this.manager = manager;
		
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

			if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) { // TODO: Can they take their lunch break before 12PM?
				this.takeLunch();
			} else if (this.waitingForAnswers.size() > 0) {
				// Answer the question of the first Employee in the Queue
				this.answerQuestion(this.waitingForAnswers.poll()); // TODO: Make sure employees in the Queue wait for their question to be answered
			} else {
				// TODO: Randomly ask a question to the Manager
				// TODO: Say that working
			}
		}

		try {
			manager.getStatusUpdateBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// After meeting, wait for conference room and all developers on same team present, enter room and have 15 minute meeting

	}

	public CyclicBarrier getDeveloperStandUpBarrier() {
		return this.developerStandUpBarrier;
	}

	public Manager getManager() {
		return this.manager;
	}

	public void answerQuestion(Employee whoHasQuestion) {
		if (rng.nextDouble() < 0.5) {
			// TODO: go to PM office w/ developer

			CyclicBarrier questionMeeting = new CyclicBarrier(2, new Runnable() {
				public void run(){
					try {
						this.wait(10 * Time.MINUTE.getMillis());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			
			manager.knockOnDoor(questionMeeting);
			try {
				questionMeeting.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch( BrokenBarrierException e){
				System.out.println("X saves their question for a different day");
			}

		}else{
			try {
				//takes 10 min to answer question
				this.wait(10*Time.MINUTE.getMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// TODO: return to work
	}

	@Override
	public void askQuestion() {
		// TODO: go to PM office to ask question

		CyclicBarrier questionMeeting = new CyclicBarrier(2, new Runnable() {
			public void run(){
				try {
					this.wait(10 * Time.MINUTE.getMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		// Ask the Manager the Developer's Question
		manager.knockOnDoor(questionMeeting);
		try {
			questionMeeting.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
