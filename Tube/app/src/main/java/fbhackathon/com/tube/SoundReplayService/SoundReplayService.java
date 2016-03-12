package fbhackathon.com.tube.SoundReplayService;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import java.io.IOException;

import fbhackathon.com.tube.R;

public class SoundReplayService extends IntentService {

    public SoundReplayService() {
        super("SoundReplayService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        Uri data = workIntent.getData();
        // Do work here, based on the contents of dataString

        MediaPlayer mp = new MediaPlayer();
        try {
            // InputStream ins = getResources().openRawResource(R.raw.shotgun);
            // mp.setDataSource("/sdcard/tubeapp/shotgun.mp3");
            AssetFileDescriptor afd = this.getApplicationContext().getResources().openRawResourceFd(R.raw.shotgun);
            mp.setDataSource(afd.getFileDescriptor());
            afd.close();
            // mp.setDataSource(, R.raw.shotgun);
            mp.prepare();
        }catch(IOException e){
            e.printStackTrace();
        }
        //mp.setLooping(true);
        mp.start();
    }
}