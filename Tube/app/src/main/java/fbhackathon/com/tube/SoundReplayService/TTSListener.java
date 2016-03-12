package fbhackathon.com.tube.SoundReplayService;

import android.speech.tts.TextToSpeech;

/**
 * Created by johannth on 12/03/16.
 */
public class TTSListener implements TextToSpeech.OnInitListener {
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            //TextToSpeech.setLanguage(Locale.getDefault());

        } else {
            //tts = null;
            //Toast.makeText(mContext, "Failed to initialize TTS engine.",
            //        Toast.LENGTH_SHORT).show();
        }
    }
}
