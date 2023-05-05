import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import feed.Feed;
import httpRequest.httpRequester;
import parser.RssParser;
import parser.SubscriptionParser;
import subscription.SingleSubscription;
import subscription.Subscription;

public class FeedReaderMain {

	private static void printHelp(){
		System.out.println("Please, call this program in correct way: FeedReader [-ne]");
	}
	
	public static void main(String[] args) throws JSONException, MalformedURLException, IOException, ParserConfigurationException, SAXException, ParseException {
		System.out.println("************* FeedReader version 1.0 *************");
		if (args.length == 0) {

			/*
			Leer el archivo de suscription por defecto;
			Llamar al httpRequester para obtenr el feed del servidor
			Llamar al Parser especifico para extrar los datos necesarios por la aplicacion 
			Llamar al constructor de Feed
			LLamar al prettyPrint del Feed para ver los articulos del feed en forma legible y amigable para el usuario
			*/
			SubscriptionParser parser = new SubscriptionParser();
			Subscription subscription = parser.parse("config/subscriptions.json");

			RssParser rssParser = new RssParser();

			for (int i = 0; i < subscription.getSubscriptionAmount(); i++) {
				SingleSubscription singleSub = subscription.getSingleSubscription(i);
				for (int j = 0; j < singleSub.getUlrParamsSize(); j++) {
					try {
						String rssContent = httpRequester.getFeedRss(singleSub.getUrlForParam(j));
						Feed feed = rssParser.parse(rssContent);
						feed.prettyPrint();
					} catch(MalformedURLException e) {
						System.out.println("Malformed URL exception en subscripcion " + singleSub.getUrlForParam(j));
					} catch(IOException e) {
						System.out.println("IO exception en subscripcion " + singleSub.getUrlForParam(j));
					} catch(SAXParseException e) {
						System.out.println("Parse exception en subscripcion " + singleSub.getUrlForParam(j));
					}
				}
			}
			
		} else if (args.length == 1){
			
			/*
			Leer el archivo de suscription por defecto;
			Llamar al httpRequester para obtenr el feed del servidor
			Llamar al Parser especifico para extrar los datos necesarios por la aplicacion 
			Llamar al constructor de Feed
			Llamar a la heuristica para que compute las entidades nombradas de cada articulos del feed
			LLamar al prettyPrint de la tabla de entidades nombradas del feed.
			 */
			
		}else {
			printHelp();
		}
	}

}
