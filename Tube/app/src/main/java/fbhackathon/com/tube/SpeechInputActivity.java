package fbhackathon.com.tube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import fbhackathon.com.tube.SoundReplayService.SoundReplayService;

/**
 * Created by Chaiyong on 3/12/16.
 */
public class SpeechInputActivity extends Activity {
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private HashMap<String, Boolean> stationMap = new HashMap<String, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_recognition);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    private void setupStationNames() {
        // initialize all the station names
        stationMap.put("acton", true);
        stationMap.put("aldgate", true);
        stationMap.put("aldgate", true);
        stationMap.put("alperton", true);
        stationMap.put("amersham", true);
        stationMap.put("angel", true);
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-UK");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Got results back");
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String announcement = result.get(0);
                    txtSpeechInput.setText(announcement);
                    System.out.println("tubeapp: announcement = " + announcement);
                    String[] splitText = announcement.split("\\s");
                    System.out.println("tubeapp: split size = " + splitText.length);
                    for (int i=0; i<splitText.length; i++) {
                        System.out.println("tubeapp: split = " + splitText[i].toLowerCase());
                       if (stationMap.get(splitText[i].toLowerCase())!=null) {
                           System.out.println("tubeapp: found in map");
                           Intent intent = new Intent(this, SoundReplayService.class);
                           intent.setData(Uri.parse("file://tubeapp/shotgun.mp3"));
                           this.startService(intent);
                       }
                    }
                }
                break;
            }

        }
    }
}
