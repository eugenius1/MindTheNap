package fbhackathon.com.tube;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import fbhackathon.com.tube.MapData.MapMaker;
import fbhackathon.com.tube.SoundReplayService.SoundReplayService;

public class MainActivity extends AppCompatActivity implements ServiceCallbacks {

    private boolean bound;
    private Accelerometer accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoundReplayService svc = new SoundReplayService(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // This code should be called to
        // Intent intent = new Intent(this, SoundReplayService.class);
        // intent.setData(Uri.parse("file://tubeapp/shotgun"));
        // this.startService(intent);

        FloatingActionButton record = (FloatingActionButton) findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          openAudioRecording();

                                      }
                                  }

        );

        FloatingActionButton speech = (FloatingActionButton) findViewById(R.id.speech);
        speech.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View view) {
                                          openSpeechRecognizer();
                                      }
                                  }

        );

        FloatingActionButton speechnew = (FloatingActionButton) findViewById(R.id.speechnew);
        speechnew.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View view) {
                                          openNewSpeechRecognizer();
                                      }
                                  }

        );


        FloatingActionButton mapButton = (FloatingActionButton) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View view) {
                                             goToJourneyPlanner();
                                         }
                                     }

        );



    }

    public void goToJourneyPlanner() {
        Intent intent = new Intent(this, MapMaker.class);
        startActivity(intent);
    }
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Accelerometer.AccelerometerBinder binder = (Accelerometer.AccelerometerBinder) service;
            accelerometer = binder.getService();
            accelerometer.setServiceCallbacks(MainActivity.this);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, Accelerometer.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void openAudioRecording() {
        Intent intent = new Intent(this, AudioRecordTest.class);
        startActivity(intent);
    }

    public void openSpeechRecognizer() {
        Intent intent = new Intent(this, SpeechInputActivity.class);
        startActivity(intent);
    }

    public void openNewSpeechRecognizer() {
        Intent intent = new Intent(this, SpeechInputNewActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeText(double magnitude) {

    }
}
