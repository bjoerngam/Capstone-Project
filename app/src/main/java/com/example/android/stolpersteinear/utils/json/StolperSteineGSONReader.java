package com.example.android.stolpersteinear.utils.json;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.stolpersteinear.data.StolperSteine;
import com.example.android.stolpersteinear.data.gson.Stolpersteinedata;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
    private static String returnValue = "";
    private Stolpersteinedata mStoredStolperSteineData;                 // Here we are storing the result of the gson.fromJSON call

    /**
     * Inside the method getJSONData we are getting the whole JSON list from the FireBase source
     * @throws IOException if there is no connection possible.
     */
    StolperSteineGSONReader() throws IOException {
        Gson gson = new GsonBuilder().create();
        // mStoredStolperSteineData = gson.fromJson(getPlainData(URLBuilder.BASE_URL), Stolpersteinedata.class);
        // mStoredStolperSteineData = gson.fromJson(getFireBaseStorage();)
        getFireBaseStorage();
    }


    private void getFireBaseStorage() {
        FirebaseStorage storage =
                FirebaseStorage.getInstance();
        StorageReference storageRef =
                storage.getReferenceFromUrl("gs://stolpersteinar.appspot.com/")
                        .child("stolpersteine-cologne.json");

        try {
            final File localFile = File.createTempFile("stolpersteine", "json");
            StorageTask<FileDownloadTask.TaskSnapshot> result = storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.i("FireBase", "File is present " + Long.toString(localFile.length()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("FireBase", "File is not there");
                }
            });
            // Log.i("FireBase", result.());

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    private void openFile(File localFile) {

        try {
            FileReader fr = new FileReader(localFile);
            BufferedReader br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                returnValue += sCurrentLine;
            }
            System.out.println(returnValue);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println(returnValue.length());
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
