package com.example.android.stolpersteinear.ar.position;

/**
 * Created by Krzysztof Jackowski on 24/09/15.
 * Instead of using an AR library I used the approach by Krzysztof Jackowski
 * <p>
 * source: https://www.netguru.co/blog/augmented-reality-mobile-android
 */

public class AugmentedPOI {
    private int mId;
    private double mLatitude;
    private double mLongitude;

    public AugmentedPOI(double newLatitude, double newLongitude) {
        this.mLatitude = newLatitude;
        this.mLongitude = newLongitude;
    }

    public int getPoiId() {
        return mId;
    }

    public void setPoiId(int poiId) {
        this.mId = poiId;
    }

    public double getPoiLatitude() {
        return mLatitude;
    }

    public void setPoiLatitude(double poiLatitude) {
        this.mLatitude = poiLatitude;
    }

    public double getPoiLongitude() {
        return mLongitude;
    }

    public void setPoiLongitude(double poiLongitude) {
        this.mLongitude = poiLongitude;
    }
}
