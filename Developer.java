import java.util.concurrent.BrokenBarrierException;

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

			if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) { // TODO: Can they take their lunch break before 12PM?
				this.takeLunch();
			} else {
				// TODO: Randomly ask a question to the TeamLead
				// TODO: Say that working
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
      leader.answerQuestion(this); // TODO: Change to Barrier
  }
}
