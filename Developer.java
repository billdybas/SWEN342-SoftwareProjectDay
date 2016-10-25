
public class Developer extends Employee implements Curious {
    private TeamLead leader;

    public Developer(TeamLead leader) {
        this.leader = leader;
    }

    @Override
    public void run() {
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
