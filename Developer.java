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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// TODO: Loop until 4
    	// Copy Manager-esque stuff
    	// Take Lunch
    	// Randomly Ask Question to TeamLead
    	// 'Code'

    	try {
			this.leader.getManager().getStatusUpdateBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Meet for 15 Minutes
		try {
			Thread.sleep(15 * Time.MINUTE.getMillis());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // 1. Arrive
        // 2. Wait for TeamLead to say to go to the meeting
        // 3. Wait for the Conference Room
        // 4. Meet w/ Team for 15 min
        // 5. Work w/ Randomly Asked Questions
        // TODO
    }

  @Override
  public void askQuestion() {
      // Ask the Team Lead a Question
      leader.answerQuestion(this);
  }
}
