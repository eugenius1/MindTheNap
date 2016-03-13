package fbhackathon.com.tube;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Chaiyong on 3/13/16.
 */
public class ProgressPageActivity extends Activity {
    private String[] stops;
    private TextView result;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        result = (TextView) findViewById(R.id.result);
        callSpeechRecognition();
    }

    private void callSpeechRecognition() {
        stops = getIntent().getExtras().getStringArray("stops");
        Intent intent = new Intent(ProgressPageActivity.this, SpeechInputNewActivity.class);
        intent.putExtra("stops", stops);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String resultString = data.getStringExtra("result");
                result.setText(resultString);
                if (!resultString.equals(stops[stops.length-1])) {
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            callSpeechRecognition();
                        }
                    }, 3000);
                }
            } else {
                result.setText("NOT FOUND :-(");
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        callSpeechRecognition();
                    }
                }, 3000);
            }
        }
    }
}
