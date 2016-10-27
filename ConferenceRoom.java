import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConferenceRoom {

	private static final ConferenceRoom instance = new ConferenceRoom();
	
	private static Queue<Employee> reservationList  = new ConcurrentLinkedQueue<>();
	
	private ConferenceRoom(){}
	
	public static ConferenceRoom getInstance() {
		return instance;
	}
	
	public static void getReservation(Employee whoWantsRoom) {
		reservationList.add(whoWantsRoom);
		while(!reservationList.peek().equals(whoWantsRoom)) {
				try {
					whoWantsRoom.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static void releaseReservation(Employee whoHasRoom) {
		reservationList.poll();
		reservationList.notifyAll();
	}
}