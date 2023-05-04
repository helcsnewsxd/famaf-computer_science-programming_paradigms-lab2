package httpRequest;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* Esta clase se encarga de realizar efectivamente el pedido de feed al servidor de noticias
 * Leer sobre como hacer una http request en java
 * https://www.baeldung.com/java-http-request
 * */

public class httpRequester {
	public static String getFeedRss(String urlFeed) throws MalformedURLException, IOException {
		StringBuffer feedRssXml = new StringBuffer();
		
		// Creamos URL de solicitud y abrimos conexión HTTP
		URL url = new URL(urlFeed);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setInstanceFollowRedirects(true);
				
		// Leer status code y conseguir contenido
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

	public static String getFeedReedit(String urlFeed) {
		String feedReeditJson = null;
		return feedReeditJson;
	}
}
