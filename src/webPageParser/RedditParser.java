package webPageParser;

import feed.Article;
import feed.Feed;
import org.json.JSONTokener;

import java.io.StringReader;
import java.sql.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RedditParser {
    public Feed parse(String content) throws EmptyFeedException {
        return null;
    //     Feed feed = new Feed("Unnamed Feed");
    //     StringReader reader = new StringReader(content);
    //     JSONTokener tokener = new JSONTokener(reader);
    //     JSONObject listing = new JSONObject(tokener);
    //     JSONArray articles = listing.getJSONArray("children");

    //     if(articles.length() == 0) {
    //         throw new EmptyFeedException("Feed vacio");
    //     } else {
    //         JSONObject firstArticle = articles.getJSONObject(0);
    //         feed.setSiteName(firstArticle.getString("subreddit"));
    //     }

    //     for (int i = 0; i < articles.length(); i++) {
    //         JSONObject article = articles.getJSONObject(i);

    //         String title = article.getString("title");
    //         Number timestamp = article.getNumber("created");
    //         String text = article.getString("self_text");
    //         String link = article.getString("url");

    //         Date date = new Date(timestamp.longValue());

    //         Article art = new Article(title, text, date, link);

    //         feed.addArticle(art);
    //     }


    //     return feed;
    }
}
