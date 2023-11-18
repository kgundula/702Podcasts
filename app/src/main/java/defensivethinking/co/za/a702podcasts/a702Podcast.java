package defensivethinking.co.za.a702podcasts;

import android.app.Application;
import android.os.StrictMode;

import java.util.List;

import defensivethinking.co.za.a702podcasts.model.Podcast;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by Profusion on 2015-12-14.
 */
public class a702Podcast extends Application {

    private List<Podcast> mPodcasts;
    @Override
    public void onCreate() {
        super.onCreate();
        enabledStrictMode();
        //LeakCanary.install(this);
    }

    private void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                .detectAll() //
                .penaltyLog() //
                .penaltyDeath() //
                .build());
    }

    public List<Podcast> getPodcasts() {
        return mPodcasts;
    }

    public void setPodcasts(List<Podcast> mResultsist) {
        this.mPodcasts = mResultsist;
    }
}
