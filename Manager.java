import java.util.List;

public class Manager extends Employee {

	private List<Team> teams;

	public Manager(List<Team> teams) {
		if (teams.size() != 3) {
			throw new IllegalArgumentException("The List of Teams Must Have Exactly 3 Teams");
		}

		this.teams = teams;
	}

	@Override
	public void run() {
		// Arrives at 8 AM each day
		// - Engages in planning and then waits until all team leads have arrived
		// - When they all arrive, has 15 minute standup meeting

	}

}
