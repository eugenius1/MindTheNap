package fbhackathon.com.tube.SoundReplayService;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import fbhackathon.com.tube.R;

public class SoundReplayService extends IntentService {

    public SoundReplayService() {
        super("SoundReplayService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String data = workIntent.getDataString();
        // Do work here, based on the contents of dataString

        MediaPlayer mp = new MediaPlayer();
        //mp.setDataSource(data);
        //mp.prepare();
        //mp.setLooping(true);
        mp.start();
    }
}