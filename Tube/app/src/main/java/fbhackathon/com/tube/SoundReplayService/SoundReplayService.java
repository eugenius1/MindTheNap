package fbhackathon.com.tube.SoundReplayService;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;


import java.io.IOException;
import java.util.HashMap;

import fbhackathon.com.tube.R;

public class SoundReplayService extends IntentService {

    public SoundReplayService() {
        super("SoundReplayService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        //Uri data = workIntent.getData();
        // Do work here, based on the contents of dataString

        TextToSpeech tts = new TextToSpeech(this,new TTSListener());
        String dataString = workIntent.getDataString();
        String utteranceId=this.hashCode() + "";
        tts.speak(dataString, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        /*
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource("/sdcard/tubeapp/shotgun.mp3");
            mp.prepare();
        }catch(IOException e){
            e.printStackTrace();
        }
        */
        //mp.setLooping(true);
        //mp.start();
    }
}