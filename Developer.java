import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Developer extends Employee implements Curious {
    private TeamLead leader;

    public Developer(TeamLead leader, int id, CountDownLatch latch) {
        this.leader = leader;
        this.id = id;
        this.latch = latch;
    }
    

    @Override
    public void run() {
    	// Arrive at the Office
    	this.arrive();

    	// Wait for the Stand Up to Start and then Meet for 15 min
    	try {
			leader.getDeveloperStandUpBarrier().await();
			System.out.println(Workday.timeString(Workday.getDelta()) + ": Developer  " + this.id + " goes to Standup");
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

    	// Until 4PM, Normal Routine
		System.out.println(Workday.timeString(Workday.getDelta()) + ": Developer  " + this.id + " goes back to work");
    	while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
    		long delta = Workday.getDelta();

    		// If at least 12PM and hasn't eaten lunch, take lunch
			if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				System.out.println(Workday.timeString(Workday.getDelta()) + ": Developer  " + this.id + " takes lunch");
				this.takeLunch();
				System.out.println(Workday.timeString(Workday.getDelta()) + ": Developer  " + this.id + " comes back from lunch");
			} else if (rng.nextDouble() < 0.1) {
				// A Question is Asked 10% of the Time
				this.askQuestion();
			} else {
				// Otherwise, work
				System.out.println(Workday.timeString(Workday.getDelta())+ " Developer  " + this.id + "  works.");
			}
    	}

    	// Wait for the Status Update to Start and then Meet for 15 min
    	try {
			this.leader.getManager().getStatusUpdateBarrier().await();
			System.out.println(Workday.timeString(Workday.getDelta()) + ": Developer  " + this.id + "  goes to the conference room for status update");
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

    	// Leave for the Day
    	this.leave();
    	System.out.println(Workday.timeString(Workday.getDelta()) + ": Developer  " + this.id + "  leaves for the day");
    }
    
    @Override
    public void askQuestion() {
    	CyclicBarrier questionMeeting = new CyclicBarrier(1);
    	System.out.println(Workday.timeString(Workday.getDelta()) + ": Developer " + this.id + "  goes to the team lead for a question");
    	
        // Ask the Team Lead a Question
        leader.answerQuestion(questionMeeting);
        
        try {
        	questionMeeting.await();
	    } catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
	    }
        System.out.println(Workday.timeString(Workday.getDelta()) + ": Developer " + this.id + "  goes back to work");
    }
}
