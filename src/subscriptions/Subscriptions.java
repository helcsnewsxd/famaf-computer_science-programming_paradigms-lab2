package subscriptions;

import java.util.ArrayList;
import java.util.List;

public class Subscriptions {
    private List<SimpleSubscription> subscriptionsList;

    // INITIALIZATION

    public Subscriptions() {
        super();
        this.subscriptionsList = new ArrayList<SimpleSubscription>();
    }

    // GET
    public int getSubscriptionListSize() {
        return subscriptionsList.size();
    }

    public List<SimpleSubscription> getSubscriptionList() {
        return subscriptionsList;
    }

    public SimpleSubscription getSubscriptionList(int index) {
        return subscriptionsList.get(index);
    }

    // SET

    public void addSimpleSubscription(SimpleSubscription simpleSubscription) {
        this.subscriptionsList.add(simpleSubscription);
    }

    // PRINT
    @Override
    public String toString() {
        String str = "";
        for (SimpleSubscription s : subscriptionsList) {
            str += s.toString();
        }
        return "[" + str + "]";
    }

    public void prettyPrint() {
        System.out.println(this.toString());
    }

    // MAIN

    public static void main(String[] args) {
        Subscriptions a = new Subscriptions();

        SimpleSubscription s0 = new SimpleSubscription(
                "https://www.chicagotribune.com/arcio/rss/category/%s/?query=display_date:[now-2d+TO+now]&sort=display_date:desc",
                null, "rss");
        s0.addUrlParameter("business");

        SimpleSubscription s1 = new SimpleSubscription("https://rss.nytimes.com/services/xml/rss/nyt/%s.xml", null,
                "rss");
        s1.addUrlParameter("Business");
        s1.addUrlParameter("Technology");

        a.addSimpleSubscription(s0);
        a.addSimpleSubscription(s1);
        a.prettyPrint();
    }
}
