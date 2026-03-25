package defensivethinking.co.za.a702podcasts;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by Profusion on 2015-12-14.
 */
public class a702Podcast extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        enabledStrictMode();
    }

    private void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                .detectAll() //
                .penaltyLog() //
                .penaltyDeath() //
                .build());
    }
}
