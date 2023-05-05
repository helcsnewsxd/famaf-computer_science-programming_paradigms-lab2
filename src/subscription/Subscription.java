package subscription;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import parser.SubscriptionParser;

/* Esta clase abstrae el contenido del archivo de suscripcion (json) */
public class Subscription {
	private List<SingleSubscription> suscriptionsList;

	
	public Subscription() {
		super();
		this.suscriptionsList = new ArrayList<SingleSubscription>();
	}

	public List<SingleSubscription> getSubscriptionsList(){
		return this.suscriptionsList;
	}

	public void addSingleSubscription(SingleSubscription s) {
		this.suscriptionsList.add(s);
	}

	public int getSubscriptionAmount() {
		return this.suscriptionsList.size();
	}
	
	public SingleSubscription getSingleSubscription(int i){
		return this.suscriptionsList.get(i);
	}

	@Override
	public String toString() {
		String str ="";
		for (SingleSubscription s: getSubscriptionsList()){
			str += s.toString();
		}
		return "[" + str + "]";
	}	
	
	public void prettyPrint(){
		System.out.println(this.toString());
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		SubscriptionParser p = new parser.SubscriptionParser();
		Subscription s = p.parse("config/subscriptions.json");
	
		s.prettyPrint();
	}
}
