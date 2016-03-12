package fbhackathon.com.tube.SoundReplayService;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;


import java.io.IOException;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Locale;

import fbhackathon.com.tube.R;

public class SoundReplayService extends IntentService {
    static TextToSpeech tts;
    static Boolean initialized = false;
    public SoundReplayService(){super("SoundReplayService");}

    public SoundReplayService(Context c) {
        super("SoundReplayService");
        try {
            tts = new TextToSpeech(c, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int arg0) {
                    initialized = true;
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    protected void onHandleIntent(Intent workIntent) {
        Uri dataString = workIntent.getData();
        while(!initialized) {
            try {
                Thread.sleep(100,0);
            } catch(Exception e) {
               e.printStackTrace();
            }
        }
        tts.setLanguage(Locale.UK);
        tts.speak("Arriving at " + dataString.getLastPathSegment() + " station.", TextToSpeech.QUEUE_FLUSH, null);
    }
}