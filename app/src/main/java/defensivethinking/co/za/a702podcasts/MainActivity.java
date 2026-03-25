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
import androidx.core.content.ContextCompat;
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
    private PodcastIntentServiceReceiver podcastIntentServiceReceiver;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Log.d("MainActivity", "Registering receiver for: " + PodcastIntentServiceReceiver.PROCESS_RESPONSE);
        IntentFilter filter = new IntentFilter(PodcastIntentServiceReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        podcastIntentServiceReceiver = new PodcastIntentServiceReceiver();
        ContextCompat.registerReceiver(this, podcastIntentServiceReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);

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
        if (podcastIntentServiceReceiver != null) {
            unregisterReceiver(podcastIntentServiceReceiver);
        }
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

            int responseStatus = intent.getIntExtra(PodcastService.STATUS, 0);
            Log.d("MainActivity", "Broadcast received, status: " + responseStatus);

            if ( responseStatus == 1  ) {
                Log.d("MainActivity", "Cache size: " + Utility.podcastCache.size());
                podcastList.clear();
                podcastList.addAll(Utility.podcastCache);
                Log.d("MainActivity", "Local list size: " + podcastList.size());

                if (mPodcastAdapter == null) {
                    Log.d("MainActivity", "Creating new adapter");
                    mPodcastAdapter = new PodcastAdapter(podcastList);
                    mPodcastRecyclerView.setAdapter(mPodcastAdapter);
                } else {
                    Log.d("MainActivity", "Notifying adapter");
                    mPodcastAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
