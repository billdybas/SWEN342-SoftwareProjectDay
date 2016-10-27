import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class TeamLead extends Employee implements Curious {

	private Manager manager;
	private Random rng = new Random();
	private CyclicBarrier developerStandUpBarrier;
	private boolean hasEatenLunch;
	
	public TeamLead(){
		
	}
	
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

			if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				try {
					this.hasEatenLunch = true;
					Thread.sleep(ThreadLocalRandom.nextInt(Time.MINUTE.getMillis() * 30, Time.HOUR.getMillis() + 1));
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
		// TODO: Copy Manager logic for taking lunch and answering Developer Questions
		// Lunch will be random and happen once
		// Lunch >= 30 min && < 60 min
		// Randomly ask question to manager

		try {
			manager.getStatusUpdateBarrier().await();
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
		


		// When they arrive, knock on Manager door and do 15 minute meeting
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

			CyclicBarrier questionMeeting = new CyclicBarrier(2,
					new Runnable() {
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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch( BrokenBarrierException e){
				System.out.println("X saves their question for a different day");
			}

		}else{
			try {
				//takes 10 min to answer question
				this.wait(10*Time.MINUTE.getMillis());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// TODO: return to work
	}

	@Override
	public void askQuestion() {
		// TODO: go to PM office to ask question

		CyclicBarrier questionMeeting = new CyclicBarrier(2,
				new Runnable() {
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
