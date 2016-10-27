import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Developer extends Employee implements Curious {
    private TeamLead leader;

    public Developer(TeamLead leader) {
        this.leader = leader;
    }

    @Override
    public void run() {

    	this.arrive();

    	try {
			leader.getDeveloperStandUpBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

    	while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
    		long delta = Workday.getDelta();

			if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				this.takeLunch();
			} else if (rng.nextDouble() < 0.1) {
				// A Question is Asked 10% of the Time
				this.askQuestion();
			} else {
				System.out.println("Developer X works.");
			}
    	}

    	try {
			this.leader.getManager().getStatusUpdateBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

    	this.leave();
    }

  @Override
  public void askQuestion() {
      // Ask the Team Lead a Question

	  CyclicBarrier questionMeeting = new CyclicBarrier(1);

      leader.answerQuestion(questionMeeting);

      try {
		questionMeeting.await();
	} catch (InterruptedException | BrokenBarrierException e) {
		e.printStackTrace();
	}
  }
}
