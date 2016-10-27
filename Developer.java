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

    	this.arrive();

    	try {
			leader.getDeveloperStandUpBarrier().await();
			System.out.println(Workday.timeString(getDelta) + ": Developer goes to Standup");
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

		System.out.println(Workday.timeString(getDelta) + ": Developer goes back to work");

    	while(Workday.getDelta() < Time.PM_FOUR.getMillis()) {
    		long delta = Workday.getDelta();

			if (delta >= Time.PM_TWELVE.getMillis() && !this.hasEatenLunch) {
				System.out.println(Workday.timeString(getDelta) + ": Developer takes lunch");
				this.takeLunch();
				System.out.println(Workday.timeString(getDelta) + ": Developer comes back from lunch");
			} else if (rng.nextDouble() < 0.1) {
				// A Question is Asked 10% of the Time
				this.askQuestion();
			} else {
				System.out.println("Developer X works.");
			}
    	}

    	try {
			this.leader.getManager().getStatusUpdateBarrier().await();
			System.out.println(Workday.timeString(getDelta) + ": Employee goes to the conference room for status update");
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}


    	this.leave();
    	System.out.println(Workday.timeString(getDelta) + ": Developer leaves for the day");
    }

  @Override
  public void askQuestion() {
      // Ask the Team Lead a Question

	  CyclicBarrier questionMeeting = new CyclicBarrier(1);
	  System.out.println(Workday.timeString(getDelta) + ": Developer goes to the team lead for a question");
      leader.answerQuestion(questionMeeting);

      try {
		questionMeeting.await();
	} catch (InterruptedException | BrokenBarrierException e) {
		e.printStackTrace();
	}
	System.out.println(Workday.timeString(getDelta) + ": Developer goes back to work");
  }
}
