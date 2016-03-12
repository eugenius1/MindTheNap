package fbhackathon.com.tube.SoundReplayService;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

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