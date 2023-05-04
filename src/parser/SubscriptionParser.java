package parser;

import java.util.ArrayList;
import java.util.List;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import subscription.SingleSubscription;
import subscription.Subscription;

/*
 * Esta clase implementa el parser del  archivo de suscripcion (json)
 * Leer https://www.w3docs.com/snippets/java/how-to-parse-json-in-java.html
 * */

public class SubscriptionParser extends GeneralParser{
    public Subscription parse(String subscriptionFilePath) throws FileNotFoundException, JSONException {
        Subscription s = new Subscription();

        FileReader reader = new FileReader(subscriptionFilePath);

        JSONTokener token = new JSONTokener(reader);
        JSONArray arr = new JSONArray(token);

        for(int i = 0; i < arr.length(); i++){
            JSONObject obj = arr.getJSONObject(i);

            SingleSubscription ss = new SingleSubscription();
            ss.setUrl(obj.getString("url"));
            ss.setUrlType(obj.getString("urlType"));

            JSONArray arrUrlParams = obj.getJSONArray("urlParams");
            for(int j = 0; j < arrUrlParams.length(); j++){
                ss.setUlrParams(arrUrlParams.getString(j));
            }

            s.addSingleSubscription(ss);
        }

        return s;
    }
}
