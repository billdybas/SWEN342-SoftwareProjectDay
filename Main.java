import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		Manager manager = new Manager();
		for (int i = 0; i < 3; i++) {
			TeamLead leader = new TeamLead(manager);

			List<Developer> developers = new ArrayList<Developer>();
			for (int j = 0; j < 3; j++) {
					developers.add(new Developer(leader));
			}

		}
	}
}
