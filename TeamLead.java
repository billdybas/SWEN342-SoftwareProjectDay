import java.util.Random;

public class TeamLead extends Employee implements Knowledgeable {

	private Manager manager;
	private Random rng = new Random();

	public TeamLead(Manager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {

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

	@Override
	public void answerQuestion(Employee whoHasQuestion) {
		if (rng.nextDouble() < 0.5) {
			// TODO: go to PM office w/ developer

			// Ask the Manager the Developer's Question
			manager.answerQuestion(whoHasQuestion);
		}

		// TODO: return to work
	}

	public void askQuestion() {
		// TODO: go to PM office to ask question

		// Ask the Manager a Question
		manager.answerQuestion(this);
	}
}
