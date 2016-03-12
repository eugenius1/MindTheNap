package fbhackathon.com.tube;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

public class Accelerometer extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private double magnitude;
    private Activity activity;
    private final AccelerometerBinder binder = new AccelerometerBinder();
    private ServiceCallbacks serviceCallbacks;

    public class AccelerometerBinder extends Binder {
        public Accelerometer getService() {
            return Accelerometer.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        registerListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterListener();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void registerListener() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        System.out.println(1);
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            magnitude = findMagnitude(event.values);
            if (serviceCallbacks != null) {
                serviceCallbacks.changeText(magnitude);
  //              System.out.println(magnitude);
            }
        }
    }

    public double getMagnitude() {
        return magnitude;
    }

    private double findMagnitude(float[] values) {
        float magnitude = 0;
        for (int i = 0; i < values.length; i++) {
            magnitude += values[i] * values[i];
        }
        return Math.sqrt(magnitude);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    public void setServiceCallbacks(ServiceCallbacks serviceCallbacks) {
        this.serviceCallbacks = serviceCallbacks;
    }

}
