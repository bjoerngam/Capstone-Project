package com.example.android.stolpersteinear.ar.position;

/**
 * Created by Krzysztof Jackowski on 24/09/15.
 * Instead of using an AR library I used the approach by Krzysztof Jackowski
 * <p>
 * source: https://www.netguru.co/blog/augmented-reality-mobile-android
 */

public interface OnAzimuthChangedListener {
    void onAzimuthChanged(float azimuthFrom, float azimuthTo);
}
