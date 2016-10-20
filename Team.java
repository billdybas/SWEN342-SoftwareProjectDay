import java.util.List;

public class Team {

	public Team(TeamLead leader, List<Developer> developers) {
		if (developers.size() != 3) {
			throw new IllegalArgumentException("The List of Developers Must Have Exactly 3 Developers");
		}
	}
}
