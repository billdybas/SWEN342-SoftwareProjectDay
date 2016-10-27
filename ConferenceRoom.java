import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConferenceRoom {
	
	// ConferenceRoom is a Singleton
	private static final ConferenceRoom instance = new ConferenceRoom();
	// Queue of Employees Waiting to Use the ConferenceRoom
	private static Queue<Employee> reservationList  = new ConcurrentLinkedQueue<Employee>();
	
	private ConferenceRoom(){}
	
	public static ConferenceRoom getInstance() {
		return instance;
	}
	
	/**
	 * Submit a Request for Reserving the ConferenceRoom
	 * @param whoWantsRoom
	 */
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
	
	/**
	 * Release the Reservation if Employee has ConferenceRoom
	 * @param whoHasRoom
	 */
	public static void releaseReservation(Employee whoHasRoom) {
		if (reservationList.peek().equals(whoHasRoom)) {
			reservationList.poll();
			reservationList.notifyAll();
		}
	}
}