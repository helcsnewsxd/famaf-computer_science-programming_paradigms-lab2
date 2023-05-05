package httpRequest;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPRequester {
    public String getFeed(String urlFeed, String urlType) throws MalformedURLException, IOException {
        if (urlType.equals("rss"))
            return getFeedRss(urlFeed);
        else if (urlType.equals("reddit"))
            return getFeedReedit(urlFeed);
        else
            return null;
    }

    private String getFeedRss(String urlFeed) throws MalformedURLException, IOException {
        StringBuffer feedRssXml = new StringBuffer();

        URL url = new URL(urlFeed);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setInstanceFollowRedirects(true);

        int status = con.getResponseCode();

        BufferedReader streamReader;

        if (status > 299) {
            streamReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        } else {
            streamReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        String inputLine;

        while ((inputLine = streamReader.readLine()) != null) {
            feedRssXml.append(inputLine);
        }

        return feedRssXml.toString();
    }

    // TO COMPLETE
    private static String getFeedReedit(String urlFeed) {
        String feedReeditJson = null;
        return feedReeditJson;
    }
}
