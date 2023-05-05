package subscriptions;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubscriptionParser {
    // PARSE FUNCTION
    public Subscriptions parse(String subscriptionsFilePath) throws FileNotFoundException, JSONException {
        Subscriptions subscriptions = new Subscriptions();

        FileReader reader = new FileReader(subscriptionsFilePath);

        JSONTokener token = new JSONTokener(reader);
        JSONArray arr = new JSONArray(token);

        for (int i = 0, szi = arr.length(); i < szi; i++) {
            JSONObject obj = arr.getJSONObject(i);

            SimpleSubscription simpleSubscription = new SimpleSubscription();
            simpleSubscription.setUrl(obj.getString("url"));
            simpleSubscription.setUrlType(obj.getString("urlType"));

            JSONArray arrUrlParams = obj.getJSONArray("urlParams");
            for (int j = 0, szj = arrUrlParams.length(); j < szj; j++) {
                simpleSubscription.addUrlParameter(arrUrlParams.getString(j));
            }

            subscriptions.addSimpleSubscription(simpleSubscription);
        }

        return subscriptions;
    }

    // MAIN
    public static void main(String args[]) throws FileNotFoundException {
        SubscriptionParser subscriptionParser = new SubscriptionParser();
        Subscriptions subscriptions = subscriptionParser.parse("config/subscriptions.json");
        subscriptions.prettyPrint();
    }
}
