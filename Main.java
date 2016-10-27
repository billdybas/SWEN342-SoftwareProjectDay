import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		Manager manager = new Manager();
		
		List<Team> teams = new ArrayList<Team>();
		for (int i = 0; i < 3; i++) {
			TeamLead leader = new TeamLead(manager, null);

			List<Developer> developers = new ArrayList<Developer>();
			for (int j = 0; j < 3; j++) {
					developers.add(new Developer(leader));
			}

			teams.add(new Team(leader, developers));
		}

		manager.setTeams(teams);
	}
}
