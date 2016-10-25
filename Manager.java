import java.util.List;

public class Manager extends Employee implements Knowledgeable {

	private List<Team> teams;

	public Manager() {}

	public Manager(List<Team> teams) {
		if (teams.size() != 3) {
			throw new IllegalArgumentException("The List of Teams Must Have Exactly 3 Teams");
		}

		this.teams = teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	@Override
	public void run() {
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

	@Override
	public void answerQuestion(Employee whoHasQuestion) {
		// TODO Auto-generated method stub

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
