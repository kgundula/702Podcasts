package defensivethinking.co.za.a702podcasts;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PodcastDetailActivity extends AppCompatActivity {


    public static final String EXTRA_PARAM_TITLE= "podcast:title";
    public static final String EXTRA_PARAM_DESCRIPTION = "podcast:descprition";
    public static final String EXTRA_PARAM_URL = "podcast:url";

    public static final String VIEW_NAME_HEADER_TITLE = "podcast:header:title";

    public String title = "";
    public String description = "";
    public String url = "";

    TextView podcast_title,podcast_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);

        if ( getIntent().getExtras() != null ) {

            title = getIntent().getStringExtra(EXTRA_PARAM_TITLE);
            description = getIntent().getStringExtra(EXTRA_PARAM_DESCRIPTION);
            url = getIntent().getStringExtra(EXTRA_PARAM_URL);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        podcast_title = (TextView) findViewById(R.id.podcast_title);
        podcast_description = (TextView) findViewById(R.id.podcast_description);
        podcast_description.setText(description);


    }

}
