import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Main {

	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(1);
		Manager manager = new Manager(latch);
		for (int i = 0; i < 3; i++) {
			TeamLead leader = new TeamLead(manager, i);

			List<Developer> developers = new ArrayList<Developer>();
			for (int j = 0; j < 3; j++) {
					developers.add(new Developer(leader, ((i*10)+j)));
			}
		}
	}
}
