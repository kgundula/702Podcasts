package defensivethinking.co.za.a702podcasts.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Created by kgundula on 16/03/16.
 */
public class PodcastMusicService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
