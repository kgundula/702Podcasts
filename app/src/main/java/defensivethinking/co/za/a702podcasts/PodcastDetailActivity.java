package defensivethinking.co.za.a702podcasts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class PodcastDetailActivity extends AppCompatActivity {


    public static final String EXTRA_PARAM_TITLE= "podcast:title";
    public static final String EXTRA_PARAM_DESCRIPTION = "podcast:descprition";
    public static final String EXTRA_PARAM_URL = "podcast:url";

    public static final String VIEW_NAME_HEADER_TITLE = "podcast:header:title";

    public String title = "";
    public String description = "";
    public String url = "";

    TextView podcast_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if ( getIntent().getExtras() != null ) {

            title = getIntent().getStringExtra(EXTRA_PARAM_TITLE);
            description = getIntent().getStringExtra(EXTRA_PARAM_DESCRIPTION);
            url = getIntent().getStringExtra(EXTRA_PARAM_URL);

        }

        podcast_title = (TextView) findViewById(R.id.podcast_title);
        podcast_title.setText(title);


    }

}
