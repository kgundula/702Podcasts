package defensivethinking.co.za.a702podcasts.service;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

import defensivethinking.co.za.a702podcasts.MainActivity;
import defensivethinking.co.za.a702podcasts.model.Podcast;
import defensivethinking.co.za.a702podcasts.utility.Utility;
import defensivethinking.co.za.a702podcasts.utility.XMLDOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgundula on 2015-12-14.
 */
public class PodcastService extends IntentService {

    public static String podcast = PodcastService.class.getSimpleName();
    public static final String STATUS = "status";
    public static final String RESPONSE_STRING = "response_string";

    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_FAILED = 2;


    public PodcastService() {
        super(podcast);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PodcastService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String url = intent.getStringExtra("url");
        if (url == null || !url.startsWith("https://www.omnycontent.com/")) {
            Log.w(podcast, "Ignoring intent with untrusted URL: " + url);
            return;
        }

        String dataXmlStr = "";
        BufferedReader reader = null;

        //final String rec = intent.getStringExtra("receiver");
        try {
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.connect();
            InputStream inputStream = con.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return;
            }

            dataXmlStr = buffer.toString();
            inputStream.close();

            // Parse the XML here to avoid TransactionTooLargeException in broadcast
            XMLDOMParser parser = new XMLDOMParser();
            InputStream stream = new ByteArrayInputStream(dataXmlStr.getBytes());
            Document doc = parser.getDocument(stream);

            List<Podcast> podcastList = new ArrayList<>();
            if (doc != null) {
                NodeList nl = doc.getElementsByTagName("item");

                for (int i = 0; i < nl.getLength(); i++) {
                    Element element = (Element) nl.item(i);
                    String title = element.getElementsByTagName("title").item(0).getTextContent();
                    String description = element.getElementsByTagName("description").item(0).getTextContent();
                    String pubDate = element.getElementsByTagName("pubDate").item(0).getTextContent();
                    
                    Node enclosureNode = element.getElementsByTagName("enclosure").item(0);
                    String podcast_url = "";
                    String podcast_type = "";
                    
                    if (enclosureNode != null) {
                        NamedNodeMap attrs = enclosureNode.getAttributes();
                        for (int y = 0; y < attrs.getLength(); y++) {
                            Node attr = attrs.item(y);
                            if (attr.getNodeName().equalsIgnoreCase("url")) {
                                podcast_url = attr.getNodeValue();
                            } else if (attr.getNodeName().equalsIgnoreCase("type")) {
                                podcast_type = attr.getNodeValue();
                            }
                        }
                    } else {
                        Log.w("PodcastService", "Item has no enclosure tag");
                    }
                    Podcast podcast = new Podcast(title, description, pubDate, podcast_type, podcast_url);
                    podcastList.add(podcast);
                }
            } else {
                Log.e("PodcastService", "Parsed document is null");
            }
            Utility.podcastCache = podcastList;
            Log.d("PodcastService", "Cache updated with " + podcastList.size() + " podcasts");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.PodcastIntentServiceReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.setPackage(getPackageName()); // Ensure it stays within the app
        // Removed RESPONSE_STRING to avoid TransactionTooLargeException
        broadcastIntent.putExtra(STATUS, STATUS_FINISHED);
        sendBroadcast(broadcastIntent);
    }
}