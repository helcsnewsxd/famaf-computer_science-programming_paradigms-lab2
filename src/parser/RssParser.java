package parser;
import feed.Feed;
import feed.Article;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/* Esta clase implementa el parser de feed de tipo rss (xml)
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm 
 * */

public class RssParser extends GeneralParser {
    Feed parse(String siteName, String content) throws ParserConfigurationException, IOException, SAXException, ParseException {
        // Parsear xml 
        Feed feed = new Feed(siteName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(content);

        // Conseguir los <item> dentro del feed rss y traducirlos a Article(s)

        NodeList items = doc.getElementsByTagName("item");

        for (int i = 0; i < items.getLength(); ++i) {
            Element item = (Element) items.item(i);
            String title = item.getElementsByTagName("title").item(0).getTextContent();
            String pubDateStr = item.getElementsByTagName("pubDate").item(0).getTextContent();
            String text = item.getElementsByTagName("description").item(0).getTextContent();
            String link = item.getElementsByTagName("link").item(0).getTextContent();

            SimpleDateFormat dateFormat = new SimpleDateFormat("DDD, DD MMM YYYY h:m:s +0000");
            Date date = dateFormat.parse(pubDateStr);

            Article art = new Article(title, text, date, link);

            feed.addArticle(art);
        }

        return feed;
    }
}
