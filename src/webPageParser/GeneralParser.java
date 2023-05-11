package webPageParser;

import feed.Feed;

import java.io.IOException;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class GeneralParser {
    public Feed parse(String content, String urlType)
            throws ParserConfigurationException, IOException, SAXException, ParseException, EmptyFeedException {
        if (urlType.equals("rss")) {
            RssParser rssParser = new RssParser();
            return rssParser.parse(content);

        } else if (urlType.equals("reddit")) {
            RedditParser redditParser = new RedditParser();
            return redditParser.parse(content);

        } else
            return null;
    }
}
