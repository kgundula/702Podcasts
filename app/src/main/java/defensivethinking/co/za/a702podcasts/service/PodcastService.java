package defensivethinking.co.za.a702podcasts.service;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import defensivethinking.co.za.a702podcasts.MainActivity;

/**
 * Created by Profusion on 2015-12-14.
 */
public class PodcastService extends IntentService {

    public static String podcast = PodcastService.class.getSimpleName();
    public static final String STATUS = "status";
    public static final String RESPONSE_STRING = "response_string";

    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_FAILED = 2;
;
    public static final String REQUEST_METHOD  = "request_method";


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
        String dataXmlStr = "";
        BufferedReader reader = null;

        //final String rec = intent.getStringExtra("receiver");
        try {
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.connect();
            InputStream inputStream = con.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }

            dataXmlStr = buffer.toString();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.PodcastIntentServiceReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_STRING, dataXmlStr);
        broadcastIntent.putExtra(STATUS, STATUS_FINISHED);
        sendBroadcast(broadcastIntent);


    }



}
