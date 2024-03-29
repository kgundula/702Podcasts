package defensivethinking.co.za.a702podcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import defensivethinking.co.za.a702podcasts.adapters.PodcastAdapter;
import defensivethinking.co.za.a702podcasts.databinding.ActivityMainBinding;
import defensivethinking.co.za.a702podcasts.listener.RecyclerItemClickListener;
import defensivethinking.co.za.a702podcasts.model.Podcast;
import defensivethinking.co.za.a702podcasts.service.PodcastService;
import defensivethinking.co.za.a702podcasts.utility.RecylerViewDividerItemDecoration;
import defensivethinking.co.za.a702podcasts.utility.Utility;
import defensivethinking.co.za.a702podcasts.utility.XMLDOMParser;

public class MainActivity extends AppCompatActivity {
    String url = "";
    List<Podcast> podcastList = new ArrayList<>();
    private PodcastAdapter mPodcastAdapter;

    protected RecyclerView mPodcastRecyclerView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        *  When not using DataBinding you would set Activity Layout this way.
        *
        * setContentView(R.layout.activity_main);
        *
        */
        defensivethinking.co.za.a702podcasts.databinding.ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        url = Utility.URL;

        context = getApplicationContext();
        mPodcastRecyclerView = findViewById(R.id.podcast_recyler_view);
        mPodcastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPodcastRecyclerView.addItemDecoration(new RecylerViewDividerItemDecoration(context));
        mPodcastRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, float x, float y) {
                Podcast podcast = podcastList.get(position);
                String title = podcast.getItemTitle();
                String description = podcast.getItemDescription();
                String url = podcast.getItemUrl();

                navigateToDetail(title,description,url, view);

            }
        }));

        IntentFilter filter = new IntentFilter(PodcastIntentServiceReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        PodcastIntentServiceReceiver podcastIntentServiceReceiver = new PodcastIntentServiceReceiver();
        registerReceiver(podcastIntentServiceReceiver, filter);

        if (!"".equals(url)) {
            startService(createPodcastNetworkIntent(url));
        }
        else
        {
            Log.i("Ygritte", "You know nothing Jon Snow");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(podcastIntentServiceReceiver);
    }


    public void navigateToDetail(String title,String description, String podcast_url, View v) {

        Intent intent = new Intent(this, PodcastDetailActivity.class);
        intent.putExtra(PodcastDetailActivity.EXTRA_PARAM_TITLE, title);
        intent.putExtra(PodcastDetailActivity.EXTRA_PARAM_DESCRIPTION, description);
        intent.putExtra(PodcastDetailActivity.EXTRA_PARAM_URL, podcast_url);
        Pair<View, String> p1 = Pair.create(v, "podcast") ;
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, p1
        );

        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
    }

    private Intent createPodcastNetworkIntent(String url) {
        Intent intent = new Intent(MainActivity.this, PodcastService.class);
        intent.putExtra("url", url);

        return intent;
    }

    public class PodcastIntentServiceReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "defensivethinking.co.za.a702podcasts.intent.action.PROCESS_RESPONSE";


        @Override
        public void onReceive(Context context, Intent intent) {

            String dataXmlStr = intent.getStringExtra(PodcastService.RESPONSE_STRING);
            int responseStatus = intent.getIntExtra(PodcastService.STATUS, 0);
            Log.i("Ygritte", dataXmlStr);
            if ( responseStatus == 1  ) {
                XMLDOMParser parser = new XMLDOMParser();
                InputStream stream;

                stream =  new ByteArrayInputStream( dataXmlStr.getBytes() );
                Document doc = parser.getDocument(stream);

                if (doc != null) {
                    NodeList nl = doc.getElementsByTagName("item");

                    for (int i = 0; i < nl.getLength(); i++) {
                        Element element = (Element) nl.item(i);
                        Node node = nl.item(i);

                        String title = element.getElementsByTagName("title").item(0).getTextContent();
                        String description = element.getElementsByTagName("description").item(0).getTextContent();
                        String pubDate = element.getElementsByTagName("pubDate").item(0).getTextContent();
                        NamedNodeMap attrs = element.getElementsByTagName("enclosure").item(0).getAttributes();
                        String podcast_url = "";
                        String podcast_type = "";
                        for (int y = 0; y < attrs.getLength(); y++) {
                            Node attr = attrs.item(y);
                            if (attr.getNodeName().equalsIgnoreCase("url")) {
                                podcast_url = attr.getNodeValue();
                            } else if (attr.getNodeName().equalsIgnoreCase("type")) {
                                podcast_type = attr.getNodeValue();
                            }
                        }
                        Podcast podcast = new Podcast(title, description, pubDate, podcast_type, podcast_url);
                        podcastList.add(podcast);

                        if (mPodcastAdapter == null) {
                            mPodcastAdapter = new PodcastAdapter(podcastList);
                            //((defensivethinking.co.za.a702podcasts.a702Podcast) getApplication()).setPodcasts(podcastList);
                        } else {
                            //((defensivethinking.co.za.a702podcasts.a702Podcast) getApplication()).getPodcasts().clear();
                            //((defensivethinking.co.za.a702podcasts.a702Podcast) getApplication()).getPodcasts().addAll(podcastList);
                            mPodcastAdapter.notifyDataSetChanged();
                        }


                        mPodcastRecyclerView.setAdapter(mPodcastAdapter);

                    }
                }

            }

        }
    }
}
