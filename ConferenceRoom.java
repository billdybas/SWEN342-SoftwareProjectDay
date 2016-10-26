import java.util.LinkedList;
import java.util.Queue;

public class ConferenceRoom {

	private Queue<Employee> resList  = new LinkedList<>();
	
	public ConferenceRoom(){
	}
	
	synchronized public void getRes(Employee e) throws InterruptedException{
		resList.add(e);
		while(true){
			if(!resList.peek().equals(e)){
				wait();
			}
		}
	}
	
	synchronized public void relRes(Employee e){
		resList.poll();
		notifyAll();
	}
}