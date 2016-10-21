
public class Developer extends Employee {
    private TeamLead leader;

    public Developer(TeamLead leader) {
        this.leader = leader;
    }

    @Override
    public void run() {
        // 1. Arrive
        // 2. Enter Conference Room w/ Team Members
    }

}
