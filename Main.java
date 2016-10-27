import java.util.concurrent.CountDownLatch;

public class Main {

	public static void main(String[] args) {
		TeamLead[] leads = new TeamLead[3];
		Developer[] devs = new Developer[9];
		CountDownLatch latch = new CountDownLatch(1);
		Manager manager = new Manager(latch);
		for (int i = 0; i < 3; i++) {
			leads[i] = new TeamLead(manager, i + 1, latch);

			for (int j = 0; j < 3; j++) {
					devs[(i * 3) + j] = new Developer(leads[i], (((i+1)*10)+(j+1)), latch);
			}
		}
		(new Thread(manager)).start();
		for(TeamLead lead : leads){
			(new Thread(lead)).start();
		}
		for(Developer dev : devs){
			(new Thread(dev)).start();
		}
	}
}
