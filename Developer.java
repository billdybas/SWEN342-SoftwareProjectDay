import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Developer extends Employee implements Curious {
    private TeamLead leader;

    public Developer(TeamLead leader) {
        this.leader = leader;
    }

    @Override
    public void run() {
    	// Arrive at the Office
    	this.arrive();

    	// Wait for the Stand Up to Start and then Meet for 15 min
    	try {
			leader.getDeveloperStandUpBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

    	// Until 4PM, Normal Routine
    	while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
    		long delta = Workday.getDelta();

    		// If at least 12PM and hasn't eaten lunch, take lunch
			if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				this.takeLunch();
			} else if (rng.nextDouble() < 0.1) {
				// A Question is Asked 10% of the Time
				this.askQuestion();
			} else {
				// Otherwise, work
				System.out.println("Developer X works.");
			}
    	}

    	// Wait for the Status Update to Start and then Meet for 15 min
    	try {
			this.leader.getManager().getStatusUpdateBarrier().await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

    	// Leave for the Day
    	this.leave();
    }
    
    @Override
    public void askQuestion() {
    	CyclicBarrier questionMeeting = new CyclicBarrier(1);
    	
        // Ask the Team Lead a Question
        leader.answerQuestion(questionMeeting);
        
        try {
        	questionMeeting.await();
	    } catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
	    }
    }
}
