package com.example.android.stolpersteinear.ar.position;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Krzysztof Jackowski on 24/09/15.
 * Instead of using an AR library I used the approach by Krzysztof Jackowski
 * <p>
 * source: https://www.netguru.co/blog/augmented-reality-mobile-android
 */
public class MyCurrentAzimuth implements SensorEventListener {

    Context mContext;
    private SensorManager sensorManager;
    private Sensor sensor;
    private int azimuthFrom = 0;
    private int azimuthTo = 0;
    private OnAzimuthChangedListener mAzimuthListener;
    private int accureny;

    public MyCurrentAzimuth(OnAzimuthChangedListener azimuthListener, Context context) {
        mAzimuthListener = azimuthListener;
        mContext = context;
    }

    public void start() {
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_UI);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public int getAccureny() {
        return accureny;
    }

    private void setAccureny(int accureny) {
        this.accureny = accureny;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        azimuthFrom = azimuthTo;

        float[] orientation = new float[3];
        float[] rMat = new float[9];
        SensorManager.getRotationMatrixFromVector(rMat, event.values);
        azimuthTo = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        mAzimuthListener.onAzimuthChanged(azimuthFrom, azimuthTo);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        setAccureny(accuracy);
    }


}
