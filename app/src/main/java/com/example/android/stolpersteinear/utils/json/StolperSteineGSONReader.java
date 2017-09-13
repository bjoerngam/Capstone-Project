package com.example.android.stolpersteinear.utils.json;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.stolpersteinear.data.StolperSteine;
import com.example.android.stolpersteinear.data.gson.Stolpersteinedata;
import com.example.android.stolpersteinear.utils.URLBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

/**
 * Created by Bjoern on 22.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description:
 * The basic class for getting the JSON data.
 */

class StolperSteineGSONReader{

    private static final String TAG = StolperSteineGSONReader.class.getSimpleName();

    private static final int CURRENT_NUMBERS_OF_VICTIM = 1633;          // The current maximal number of victims
    private static final double GPS_TOLERANCE = 0.3;                    // Tolerance value for the difference between the OpenStreetData and the mobile device
    String name = "";
    private Stolpersteinedata mStoredStolperSteineData;                 // Here we are storing the result of the gson.fromJSON call
    /**
     * Inside the method getJSONData we are getting the whole JSON list from the FireBase source
     * @throws IOException if there is no connection possible.
     */
    StolperSteineGSONReader() throws IOException {

        Gson gson = new GsonBuilder().create();
        mStoredStolperSteineData = gson.fromJson(getPlainData(URLBuilder.BASE_URL), Stolpersteinedata.class);
        getFireBaseStorage();
        //mStoredStolperSteineData = gson.fromJson(getmJSONReturnValue(), Stolpersteinedata.class);
    }

    /**
     * Getting the JSON plain file via FireBase and setting up a HTTPS connection.
     *
     * @param url The fixed FireBase URL
     * @return The JSON string
     * @throws IOException throws an exception if its not possible to open the URL
     */
    private static String getPlainData(URL url) throws IOException {

        final int TIMEOUT = 20000;

        // For HTTPS connection
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private void getFireBaseStorage() {

        String returnValue = "";

        String path ="";
        FirebaseStorage storage =
                FirebaseStorage.getInstance();
        StorageReference storageRef =
                storage.getReferenceFromUrl("gs://stolpersteinar.appspot.com/")
                        .child("stolpersteine-cologne.json");

        final File localFile;

        try {
            localFile = File.createTempFile("stolpersteine", "json");
            storageRef.getFile(localFile).addOnSuccessListener(
                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG, "File is present Loader " + localFile.getAbsolutePath());
                    Log.i(TAG, "File-Size: " + Long.toString(localFile.length()));
                    StolperSteineGSONReader.this.name = localFile.getAbsolutePath();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i(TAG, "File is not there Loader");
                }
            });

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        Log.i(TAG, name);
    }

    /**
     * Looking if at our current position there is a stolperstein
     * @param latitude double latitude parameter of the current position
     * @param longitude double longitude parameter of the current position
     * @return a HashSet with at least one element
     */
    ArrayList<StolperSteine> getStolperSteine(Double latitude, Double longitude) {

        // We will return this HashSet with the found values
        ArrayList<StolperSteine> mStolperSteineList = new ArrayList<>();

        // The information we want to show the user
        StolperSteine mStolperStein;
        Double victimLatitude;
        Double victimLongitude;
        String victimName;
        String victimImage;
        String victimStreet;
        String victimDescription;
        String victimWebSite;
        String victimDateOfBirth;
        String victimDateOfDeath;

        for (int i = 0; i < CURRENT_NUMBERS_OF_VICTIM ; i++) {
                // If the current GPS position can be found in JSON file
                if ((checkTolerance(mStoredStolperSteineData.getFeatures().get(i).getGeometry().getCoordinates().get(0),
                        mStoredStolperSteineData.getFeatures().get(i).getGeometry().getCoordinates().get(1),
                        latitude,
                        longitude))) {
                    // Getting all of the JSON elements
                    victimName = mStoredStolperSteineData
                            .getFeatures().get(i).getProperties().getName();
                    victimImage = mStoredStolperSteineData
                            .getFeatures().get(i).getProperties().getImage();
                    victimStreet = mStoredStolperSteineData
                            .getFeatures().get(i).getProperties().getMemorialAddr();
                    victimDescription = mStoredStolperSteineData
                            .getFeatures().get(i).getProperties().getMemorialText();
                    victimWebSite = mStoredStolperSteineData
                            .getFeatures().get(i).getProperties().getWebsite();
                    victimDateOfBirth = mStoredStolperSteineData
                            .getFeatures().get(i).getProperties().getPersonDateOfBirth();
                    victimDateOfDeath = mStoredStolperSteineData
                            .getFeatures().get(i).getProperties().getPersonDateOfDeath();
                    victimLatitude = mStoredStolperSteineData
                            .getFeatures().get(i).getGeometry().getCoordinates().get(0);
                    victimLongitude = mStoredStolperSteineData
                            .getFeatures().get(i).getGeometry().getCoordinates().get(1);

                    // Creating a new object
                    mStolperStein
                            = new StolperSteine(victimName, victimWebSite, victimStreet, victimImage,
                            victimDescription, victimDateOfBirth, victimDateOfDeath, victimLatitude, victimLongitude);
                    //Adding the object to the list
                    mStolperSteineList.add(mStolperStein);
                    }
            }
        // returns the list of the found StolperSteine.F
        return mStolperSteineList;
    }

    /**
     * Comparing the longitude or latitude OpenStreetMap data (out of JSON data) with the GPS data of the mobile device.
     * The idea is it if there is a difference between the OpenStreetMap data and the GPS data less than
     * a constant GPS_TOLERANCE than I assuming the StolperStein is very close to my current position.
     *
     * The formula for calulate the distance between geo points is:
     * acos(sin(Latitude 1) x sin(Latitude 2)
     * + cos(Latitude 1) x cos(Latitude 2) x cos(Longitude 1 - Longitude 2) ) * earth radius
     * Note: We have to do transform latitude and longitude into radian values by divide the value
     * by 180*PI. I used a constant factor for this: radian_translation.
     *
     * @param openStreetData_latitude The latitude value out of the JSON object.
     * @param openStreetData_longitude The longitude value out of the JSON object.
     * @param gpsData_longitude The longitude value of our mobile device.
     * @param gpsData_latitude The latitude value of our mobile device.
     * @return true if our current position is inside the tolerance radius.
     */
    private boolean checkTolerance (double openStreetData_latitude, double openStreetData_longitude,
                                    double gpsData_latitude, double gpsData_longitude) {

        final int earth_radius = 6371;
        final double radian_translation = 180/Math.PI;

        double distance = Math.acos(Math.sin(openStreetData_latitude/radian_translation)
                * Math.sin(gpsData_latitude/radian_translation) + Math.cos(openStreetData_latitude/radian_translation)
                * Math.cos(gpsData_latitude/radian_translation)
                * Math.cos (openStreetData_longitude/radian_translation - gpsData_longitude/radian_translation)) * earth_radius;

        return (distance < GPS_TOLERANCE);
    }
}
