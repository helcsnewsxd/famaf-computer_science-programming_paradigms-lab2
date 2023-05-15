# Lector de Feeds y detección de Named Entities en Java

Integrantes:

Emanuel Nicolás Herrador

Guillermo de Ipola

Juan Bratti

# Introducción

En este laboratorio desarrollamos un lector automático de feeds en Java, siguiendo un enfoque orientado a objetos. Usando las URLs de páginas de interés, realizamos peticiones HTTP para extraer feeds específicos. A continuación, nos enfocamos en extraer los atributos más importantes como título, texto, descripción, fecha de publicación, etc. Finalmente, en el texto de cada artículo, identificamos las entidades nombradas y las clasificamos.

# Desarrollo

Las tareas a lograr fueron 4:

1. Leer un archivo de suscripciones JSON
    1. Para extraer la información del archivo, utilizamos la clase Subscriptions. En esta clase, además de definir una estructura base para la representación de las múltiples suscripciones de las cuales obtener los feeds, definimos un método llamado parse, en el cual implementamos la extracción de los campos url, urlParams y urlType del archivo JSON otorgado por la cátedra. Para ello usamos un objeto FileReader que sirve para leer el archivo en la ruta especificada. Éste devuelve el stream de caracteres que contiene el archivo. Una vez obtenido el stream, creamos un objeto JSONTokener que crea a partir de la cadena de FileReader una división de la misma en tokens de tipo JSON, tokens capaces de ser manipulados de forma tal que permiten el acceso a los distintos campos, como “url”, “urlParams” y “urlType”. Este objeto JSONTokener lo utilizamos para obtener un arreglo de dichos tokens y usando un ciclo for, buscamos en cada token las cadenas de los campos mencionados anteriormente. Agregamos dichas cadenas a otro objeto de tipo SimpleSubscription (en representación de una sola suscripción) para luego agregar cada una de ellas a una lista de subscriptions definida por la misma clase Subscription.
        
        El algoritmo principal es el siguiente:
        
        ```java
        public void parse(String subscriptionsFilePath) throws FileNotFoundException, JSONException {
        
                FileReader reader = new FileReader(subscriptionsFilePath); 
        
                JSONTokener token = new JSONTokener(reader);
                JSONArray arr = new JSONArray(token); // Contiene cada objeto
        
                for (int i = 0, szi = arr.length(); i < szi; i++) {
                    JSONObject obj = arr.getJSONObject(i); // Obtenemos uno de los objetos
        
                    SimpleSubscription simpleSubscription = new SimpleSubscription();
                    simpleSubscription.setUrl(obj.getString("url")); //extraemos url
                    String urlType = obj.getString("urlType"); //extraemos urltype
                    simpleSubscription.setUrlType(urlType);
        
                    // Inyectar parser adecuado
                    if(urlType.equals("rss")) {
                        simpleSubscription.setParser(new RssParser());
                    } else if(urlType.equals("reddit")) {
                        simpleSubscription.setParser(new RedditParser());
                    }
        
                    JSONArray arrUrlParams = obj.getJSONArray("urlParams");
                    for (int j = 0, szj = arrUrlParams.length(); j < szj; j++) {
                        simpleSubscription.addUrlParameter(arrUrlParams.getString(j));
                    }
        
                    this.addSimpleSubscription(simpleSubscription);
                }
            }
        ```
        
2. Realizar petición HTTP al servidor del feed
    1. Para las peticiones utilizamos el módulo httpRequester. Éste módulo proporciona en particular el método getResponse, que mediante la URL del feed que se parseo en el punto 1, y un objeto de tipo HttpURLConnection, establecemos la conexión HTTP y realizamos la petición deseada, obteniendo el contenido del feed en una cadena de texto. Ésto lo logramos también gracias a que la solicitud que enviamos mediante HTTP es del tipo GET. Para recibir la respuesta correctamente, con el contenido específico, desarrollamos un manejo de errores tal que se devuelvan los errores correspondientes en caso de no haber leído de forma exitosa el contenido.
        
        ```java
        private static String getResponse(String url) throws MalformedURLException, IOException, HttpRequestException {
                StringBuffer feedRssXml = new StringBuffer();
        
                URL urlObj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
                con.setRequestMethod("GET");
                con.setInstanceFollowRedirects(true); // si existe redireccionamiento, no se pierde la conexión y se continúa
        
                int status = con.getResponseCode();
        
                BufferedReader streamReader;
        
                Boolean reqError; // para control de error
        
                if ((reqError = status > 299)) {
                    streamReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                } else {
                    streamReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                }
                String inputLine;
        
                while ((inputLine = streamReader.readLine()) != null) { // se obtiene el contenido en formato XML
                    feedRssXml.append(inputLine);
                }
        
                if (reqError) {
                    throw new HttpRequestException(feedRssXml.toString()); 
                }
        
                return feedRssXml.toString(); // devuelve el contenido como string en formato XML
        
            }
        ```
        
3. Parsear el archivo XML para poder extraer los atributos del feed que se desean.
    1. Como tenemos varios parsers, todos ellos (incluyendo el de XML) heredan sus métodos de un GeneralParser definido pero no implementado. 
        
        En específico, el módulo RssParser se encarga de parsear el contenido de los feeds en una clase Article, haciendo uso del formato del feed, es decir, de los campos <item>, <text>. Esto se logra utilizando un objeto del tipo Document, que permite acceder y manipular estos elementos.
        
        Básicamente:
        
        - `doc` es un objeto capaz de analizar el contenido XML proporcionado `builder`, donde builder es un objeto capaz de manipular elementos del archivo XML.
        - en `items` guardamos los elementos de tipo <item> que se encuentran dentro del Feed RSS.
        - `channel` se verifica si existe <channel>, para así obtener el nodo <title> para asignárselo a `siteName`.
        - Luego, en el ciclo for, sobre cada elemento <item> de `item` se extraen los campos más importantes del feed como el título, la fecha de publicación, contenido y enlace.
        - Luego, se crea un objeto `Article` en donde guardaremos la información obtenida de `item`.
        - Finalmente se devuelve `Feed`, que es el objeto que contiene todos los articulos parseados.
        
        ```java
        public Feed parse(String content) throws ParserConfigurationException, IOException, SAXException, ParseException {
                // Parsear xml
                Feed feed = new Feed("Unnamed Feed");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(content))); // Utilizado para manipular los elementos del archivo XML.
        
                // Conseguir los <item> dentro del feed rss y traducirlos a Article(s)
        
                NodeList items = doc.getElementsByTagName("item");
        
                Node channel = doc.getElementsByTagName("channel").item(0); // sirve para obtener metadatos del feed
                if (channel != null) {
                    Node siteName = doc.getElementsByTagName("title").item(0);
                    if (siteName != null && siteName.getParentNode() == channel) {
                        feed.setSiteName(siteName.getTextContent()); // metadato: nombre del sitio
        
                    }
                }
        
                for (int i = 0; i < items.getLength(); ++i) { // aquí se obtienen los contenidos de los campos deseados
                    Element item = (Element) items.item(i);
                    String title = item.getElementsByTagName("title").item(0).getTextContent();
                    String pubDateStr = item.getElementsByTagName("pubDate").item(0).getTextContent();
                    String text = item.getElementsByTagName("description").item(0).getTextContent();
                    String link = item.getElementsByTagName("link").item(0).getTextContent();
        
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH); // formato de fecha para objetos de tipo Date
                    Date date = dateFormat.parse(pubDateStr);
        
                    Article art = new Article(title, text, date, link); // se pasan los contenidos parseados a una "estructura" Article.
        
                    feed.addArticle(art); // Agrego todos los artículos al feed
                }
        
                return feed;
            }
        ```
        
4. Por último, se implementó la funcionalidad de búsqueda, extracción y clasificación de las entidades nombradas presentes en los textos de cada artículo.
    1. Para cada entidad usamos un objeto de tipo NamedEntity, en el que guardamos sus metadatos (nombres, categoría, frecuencia, etc). Luego, tenemos un módulo Heuristic y dos formas de encontrar named entities: QuickHeuristic y RandomHeurístic. 
        
        El módulo Heuristic es una clase abstracta que define el esqueleto base para la clasificación de entidades. Dentro de la clase, tenemos dos métodos claves: uno es un mapa para poder clasificar las entidades que aparecen en los textos de los artículos y la otra es una función que sirve para poder decidir si una palabra es o no una entidad. Éste último método es implementado de forma distinta por los dos módulos a continuación.
        
        - RandomHeuristic: es una clase que extiende la de Heuristic. Esta heuristica es random a la hora de considerar a una palabra como entidad.
        - QuickHeuristic: también extiende a Heuristic e implementa una forma rápida para determinar si una palabra es entidad o no. Tiene un mapa de palabras que NO son entidades, las demás sí lo serán.
        
        Son estas dos heuristicas las que deciden qué palabras son entidades y cuáles no lo son. Luego, se clasifican de la siguiente forma:
        
        - Como dijimos antes, las entidades son objetos de la clase NamedEntity. Luego, tenemos otras clases que nos sirven para clasificar las NamedEntity usando un “tipo” como por ejemplo: Persona, Lugar, Organización, etc. A su vez, cada uno de estos “tipos” tienen “subtipos”, representados en Java como subclases. Éstos subtipos son mas específicos a la hora de clasificar la entidad. Por ejemplo, Persona tiene el “subtipo” nombre, apellido, etc. Estas clases y subclases heredan de NamedEntity.
        - Además de los “tipos”, tenemos una clase para los temas. Algunos de los temas son: Deportes, Política, Cultura, etc. Cada tema también tiene un subtema. Por ejemplo, Deportes tiene Fútbol, Basquet, etc. Nuevamente, cada tema y subtema son representados en clases distintas. Estas clases y subclases heredan de la interfaz Theme.
        - Luego tenemos las combinaciones entre los “tipos” de las NamedEntity y los “temas” de Theme. Con las combinaciones pueden conseguirse clasificaciones como NombreFutbol, categoría de una entidad como “Lionel”, haciendo referencia a Messi. Estas combinaciones también están definidas por clases, una clase para cada implementación. Cada clase hereda de NamedEntity (para poder usar los métodos de NamedEntity y poder guardar la categoría, etc) e implementa a los subtemas de Theme.
        
        Entonces, cada entidad puede ser clasificada como una clase de entidad con una clase o subclase de tema, o una subclase de entidad con una clase o subclase de tema. Si una entidad no cae sobre alguna de las clases definidas, se la define como Other en cada caso.
        
        Finalmente, en el módulo de Article, el método de computeNamedEntities es la encargada de procesar las palabras, y según la heuristica que se use, se identifican las entidades y se buscan las categorías usando el mapa del módulo Heuristic. Se setean los atributos de categoría con la categoría correspondiente indicada por el categoryMap y se aumenta en 1 la frecuencia en la que esa entidad fue encontrada.
        
        En el mapa categoryMap de Heuristic es donde nosotros detallaremos cuál es la categoría para cada palabra. Nosotros definimos el mapa a modo de ejemplo de esta forma:
        
        ```java
        private static Map<String, Class<? extends NamedEntity>> categoryMap = Map.ofEntries(
                    entry("Dybala", LastnameFootball.class),
                    entry("Apple", CompanyOtherThemes.class),
                    entry("Google", CompanyOtherThemes.class),
                    entry("Musk", LastnameCulture.class),
                    entry("Biden", LastnameInternational.class),
                    entry("Trump", LastnameInternational.class),
                    entry("Lionel", FirstnameFootball.class),
                    entry("Messi", LastnameFootball.class),
                    entry("Federer", LastnameTennis.class),
                    entry("USA", CountryInternational.class),
                    entry("Russia", CountryInternational.class),
                    entry("Twitter", CompanyOtherThemes.class),
                    entry("FBI", OtherEntityNational.class),
                    entry("Pitt", LastnameCinema.class),
                    entry("Reynolds", LastnameCinema.class),
                    entry("Franchella", LastnameCinema.class),
                    entry("IBM", CompanyOtherThemes.class),
                    entry("Tini", FirstnameMusic.class),
                    entry("Tiktok", CompanyOtherThemes.class),
                    entry("Beijing", CityNational.class),
                    entry("Washington", CityNational.class),
                    entry("Taiwan", CountryInternational.class),
                    entry("Sam", FirstnameOtherThemes.class),
                    entry("Transformers", OtherEntityCinema.class),
                    entry("Texas", CityNational.class),
                    entry("Tesla", CompanyOtherThemes.class),
                    entry("Hollywood", AddressCinema.class),
                    entry("May", DateOtherThemes.class),
                    entry("Linda", FirstnameOtherThemes.class),
                    entry("China", CountryInternational.class),
                    entry("Yaccarino", LastnamePolitics.class)
                    );
        ```
        

# Conclusiones

Algunas conclusiones importantes a las que llegamos fueron:

- Fue muy útil el concepto de “heredar” de un objeto ya que nos sirvió a la hora de definir las clases y subclases de las entidades y temas. Esta herencia nos brindó una noción de jerarquía a la hora de definir y usar métodos. También fue útil para poder usar las dos heuristicas dadas por la materia de forma correcta, ya que si no heredaran los métodos de Heuristic (clase padre) sería incompatible usarlas en el código debido a que algunos métodos en común son definidos de forma distinta.
- Nos introdujimos en el concepto de entidades y de la dificultad que tienen para ser implementadas. Esto también nos introdujo en el uso de interfaces y fortaleció los conceptos que teníamos sobre las herencias.
- Ganamos experiencia sobre el manejo de archivos JSON y archivos con contenido XML y su parseo en objetos definidos en Java. Además repasamos conceptos de la materia de Redes a la hora de realizar la petición HTTP de los feeds. Vimos como se realizan peticiones con método GET y el manejo de buffers para la obtención de los contenidos en Java.
- Conocimos y fortalecimos la lógica detrás de la programación orientada a objetos, haciendo uso de múltiples clases, definición de métodos, interfaces, etc.