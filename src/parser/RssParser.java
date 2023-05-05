package parser;
import feed.Feed;
import feed.Article;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/* Esta clase implementa el parser de feed de tipo rss (xml)
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm 
 * */
import java.util.Locale;

public class RssParser extends GeneralParser {
    public Feed parse(String content) throws ParserConfigurationException, IOException, SAXException, ParseException {
        // Parsear xml 
        Feed feed = new Feed("Unnamed Feed");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(content)));

        // Conseguir los <item> dentro del feed rss y traducirlos a Article(s)

        NodeList items = doc.getElementsByTagName("item");

        Node channel = doc.getElementsByTagName("channel").item(0);
        if(channel != null) {
            Node siteName = doc.getElementsByTagName("title").item(0);
            if(siteName != null && siteName.getParentNode() == channel) {
                feed.setSiteName(siteName.getTextContent());

            }
        }

        for (int i = 0; i < items.getLength(); ++i) {
            Element item = (Element) items.item(i);
            String title = item.getElementsByTagName("title").item(0).getTextContent();
            String pubDateStr = item.getElementsByTagName("pubDate").item(0).getTextContent();
            String text = item.getElementsByTagName("description").item(0).getTextContent();
            String link = item.getElementsByTagName("link").item(0).getTextContent();

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            Date date = dateFormat.parse(pubDateStr);

            Article art = new Article(title, text, date, link);

            feed.addArticle(art);
        }

        return feed;
    }
}
