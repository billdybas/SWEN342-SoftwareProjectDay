import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TeamLead extends Employee implements Knowledgeable, Curious {

	private Manager manager;
	private Random rng = new Random();
	private CyclicBarrier developerStandUpBarrier;
	private ConferenceRoom confRoom;
	
	public TeamLead(){
		
	}
	
	public TeamLead(Manager manager, ConferenceRoom confRoom) {
		this.manager = manager;
		this.confRoom = confRoom;
		this.developerStandUpBarrier = new CyclicBarrier(3);
	}

	@Override
	public void run() {

		this.arrive();

		// Tell the Manager they have arrived
		try {
			manager.getStandUpBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		// Wait for their Developers to arrive
		try {
			developerStandUpBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			confRoom.getRes(this);
			this.wait(15*Time.MINUTE.getMillis());
			confRoom.relRes(this);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		// Try to get into Conference Room
		// while conference room is busy
		//		wait (people who leave conference room notify people)

		// Meet for 15 Minutes
		try {
			Thread.sleep(15 * Time.MINUTE.getMillis());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// notifyAll?


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



		// switch (currentTime)
		// case 8 - 8:30 AM
		//  arrive
		//  wait for all other leads to arrive
		//  knock on manager door
		//  15 minute standup
		//  wait for members of team to arrive
		//  wait for conference room to be available
		//  15 minute standup with team
		// case 4 - 4:15PM
		//  go into conference room
		//  15 minute meeting
		// case 4:30 - 5PM
		//  leave (as long as has worked 8 hours)
		// default
		//  answer dev's question
		//  or ask question (self or dev's) to manager


		// When they arrive, knock on Manager door and do 15 minute meeting
		// After meeting, wait for conference room and all developers on same team present, enter room and have 15 minute meeting

	}

	public CyclicBarrier getDeveloperStandUpBarrier() {
		return this.developerStandUpBarrier;
	}

	public Manager getManager() {
		return this.manager;
	}

	@Override
	public void answerQuestion(Employee whoHasAQuestion) {
		if (rng.nextDouble() < 0.5) {
			// TODO: go to PM office w/ developer

			// Ask the Manager the Developer's Question
			manager.answerQuestion(whoHasAQuestion);
		}else{
			try {
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

		// Ask the Manager a Question
		manager.answerQuestion(this);
	}
}
