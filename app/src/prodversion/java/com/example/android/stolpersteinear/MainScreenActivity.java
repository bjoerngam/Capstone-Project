package com.example.android.stolpersteinear;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.stolpersteinear.ar.position.AugmentedPOI;
import com.example.android.stolpersteinear.ar.position.MyCurrentAzimuth;
import com.example.android.stolpersteinear.ar.position.OnAzimuthChangedListener;
import com.example.android.stolpersteinear.data.StolperSteine;
import com.example.android.stolpersteinear.data.database.StolperSteineContract;
import com.example.android.stolpersteinear.utils.dialog.AboutDialog;
import com.example.android.stolpersteinear.utils.json.JSONLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjoern on 08.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The main activity of my stolpersteineAR project.
 */

public class MainScreenActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<StolperSteine>>, OnAzimuthChangedListener{

    private final static int LOADER_ID = 100;
    private static final String TAG = MainScreenActivity.class.getSimpleName();
    private FirebaseAnalytics mFirebaseAnalytics;

    /*** GUI related ***/
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.icon)
    ImageView mImageView;
    @BindView(R.id.texture)
    TextureView mTextureView;

    /*** For the permissions +**/
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    /*** Everything around GPS ***/
    public LocationManager mLocationManager;
    public String mLocationProvider;
    public LocationListener mLocationListener;
    public double latitude;
    public double longitude;
    private final static int MIN_DISTANCE = 0;
    private final static int MIN_TIME = 400;

    /*** Everything around Camera ***/
    private CameraDevice cameraDevice;
    private Size imageDimension;
    protected CaptureRequest.Builder captureRequestBuilder;
    protected CameraCaptureSession cameraCaptureSessions;
    public Handler mBackgroundHandler;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    String cameraId;

    /*** Everything around the correct position **/
    private AugmentedPOI mPoi;
    double mAzimuthReal = 0;
    double mAzimuthTheoretical = 0;
    static double AZIMUTH_ACCURACY = 5;
    double mMyLatitude = 0;
    double mMyLongitude = 0;
    private MyCurrentAzimuth myCurrentAzimuth;

    private final int FULL_CIRCLE = 360;
    private final int HALF_CIRCLE = 180;

    public final static String CURRENT_OBJECT = "Current_Object";
    private ArrayList<StolperSteine> mListStolperSteine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Making the toolbar nice
        mToolbar.setTitle("");
        mToolbar.showOverflowMenu();
        mToolbar.setElevation(4);
        setSupportActionBar(mToolbar);
        // End of the toolbar declaration

        setupListeners();
        StrictMode.ThreadPolicy policy
                    = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        assert mTextureView != null;
        mTextureView.setSurfaceTextureListener(textureListener);

        // Check the internet connection
        if (!isOnline()) {
             Snackbar.make(findViewById(android.R.id.content),
                    getResources().getString(R.string.snackbar_no_internet_connection_error),
                    Snackbar.LENGTH_LONG).show();
        }else {
            // If the internet connection is working we check if the APP has the correct permissions
            // for the access_fine_Location and the camera
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }
        getCurrentPosition();
        setStartPosition();
        getLoaderManager().initLoader(LOADER_ID, null, MainScreenActivity.this);
    }

    void setStartPosition(){
        mPoi = new AugmentedPOI( getLatitude(), getLongitude());
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //Open the camera
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    /** Everything around the camera **/
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                manager.openCamera(cameraId, stateCallback, null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            //This is called when the camera is open
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Collections.singletonList(surface),
                    new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Snackbar.make(findViewById(android.R.id.content)
                            , getString(R.string.camera_change),
                            Toast.LENGTH_LONG).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updatePreview() {
        if(null == cameraDevice) {
            Snackbar.make(getCurrentFocus().findViewById(android.R.id.content),
            "There is a problem with the updatePreview", Snackbar.LENGTH_LONG).show();
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Camera end


    /** The menu of our main screen **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "preferences" option
            case R.id.action_saved_items:
                // Show the saved items
                Intent fav_intent = new Intent(MainScreenActivity.this, FavActivity.class);
                startActivity(fav_intent);
                return true;
            case R.id.action_clear_local_database:
                conformationDialog();
                return true;
            case R.id.action_about_dialog:
                // Show the about dialog
                Intent about_intent = new Intent(MainScreenActivity.this, AboutDialog.class);
                startActivity(about_intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<StolperSteine>> onCreateLoader(int i, Bundle bundle) {
        return new JSONLoader(getApplicationContext(), getLongitude(), getLatitude());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<StolperSteine>> loader, ArrayList<StolperSteine> result) {
        mListStolperSteine = result;
        mPoi = new AugmentedPOI( getLongitude(), getLatitude() );
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<StolperSteine>> loader) {
    }

    public double getLatitude (){ return  latitude; }
    public double getLongitude () { return  longitude; }

    /**
     * Check if we are having a working internet connection
     * Returns true if so and False if not.
     * @return true or false
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestLocationUpdates
                        (mLocationProvider, MIN_TIME, MIN_DISTANCE, mLocationListener);
                myCurrentAzimuth.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLocationManager.removeUpdates(mLocationListener);
            }
        }
    }

    /**
     * Gets the current Position
     */

    public void getCurrentPosition(){
        mLocationManager = getSystemService(LocationManager.class);

        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Creating a criteria with the best accuracy but also with the highest battery usage.
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            mLocationProvider = mLocationManager.getBestProvider(criteria, true);
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        getLoaderManager().restartLoader(LOADER_ID, null, MainScreenActivity.this);
                    } else {
                        Snackbar.make(findViewById(android.R.id.content),
                                getString(R.string.no_gps_signal),
                                Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };

        }else {
            Snackbar.make(findViewById(android.R.id.content),
                    getResources().getString(R.string.error_gps_not_present)
                    , Snackbar.LENGTH_LONG).show();

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // If there was no location change
                if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                mLocationManager.requestLocationUpdates(mLocationProvider, 0, 0, mLocationListener);
                Location lastLocation = mLocationManager.getLastKnownLocation(mLocationProvider);
                if (lastLocation != null) {
                    latitude = lastLocation.getLatitude();
                    longitude = lastLocation.getLongitude();
                    getLoaderManager().restartLoader(LOADER_ID, null, MainScreenActivity.this);

                } else { Snackbar.make(findViewById(android.R.id.content),
                        getResources().getString(R.string.error_gps_not_present)
                        , Snackbar.LENGTH_LONG).show();}
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    if ((requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) && (grantResults.length > 0
                && grantResults[0]==PackageManager.PERMISSION_GRANTED
                && grantResults[1]==PackageManager.PERMISSION_GRANTED)){
                    getCurrentPosition();
                    openCamera();
        }
    }
    private void setupListeners() {
        myCurrentAzimuth = new MyCurrentAzimuth(this, this);
        myCurrentAzimuth.start();
    }

    /**
     * In here the theoretical azimuth will be calculated.
     * 180 degree = half circle
     * 360 degree = full circle
     * @return the phi angle.
     */
    public double calculateTheoreticalAzimuth() {

        double dX = mPoi.getPoiLatitude() - mMyLatitude;
        double dY = mPoi.getPoiLongitude() - mMyLongitude;

        double phiAngle;
        double tanPhi;

        tanPhi = Math.abs(dY / dX);
        phiAngle = Math.atan(tanPhi);
        phiAngle = Math.toDegrees(phiAngle);

        if (dX > 0 && dY > 0) {
            // We are in the first quarter of the circle.
            return phiAngle;
        } else if (dX < 0 && dY > 0) {
            // We are in the second quarter of the circle.
            return HALF_CIRCLE - phiAngle;
        } else if (dX < 0 && dY < 0) {
            // We are in the third quarter of the circle.
            return HALF_CIRCLE + phiAngle;
        } else if (dX > 0 && dY < 0) {
            // We are in the fourth quarter of the circle.
            return FULL_CIRCLE - phiAngle;
        }

        return phiAngle;
    }

    /**
     * For improving the accuracy of the azimuth arc
     * @param azimuth
     * @return
     */

    private List<Double> calculateAzimuthAccuracy(double azimuth) {
        double minAngle = azimuth - AZIMUTH_ACCURACY;
        double maxAngle = azimuth + AZIMUTH_ACCURACY;
        List<Double> minMax = new ArrayList<>();

        if (minAngle < 0)
            minAngle += FULL_CIRCLE;

        if (maxAngle >= FULL_CIRCLE)
            maxAngle -= FULL_CIRCLE;

        minMax.clear();
        minMax.add(minAngle);
        minMax.add(maxAngle);

        return minMax;
    }

    /**
     * Is the stolperstein next to us? I'm using the integrated compass.
     * If the element is between the parameters I simply make the imageView
     * visible.
     * @param minAngle The min. angel
     * @param maxAngle The max. angel
     * @param azimuth The arc
     * @return true if I changed something and false if not.
     */
    private boolean isBetween(double minAngle, double maxAngle, double azimuth) {
        if (minAngle > maxAngle) {
            if (isBetween(0, maxAngle, azimuth) && isBetween(minAngle, FULL_CIRCLE, azimuth))
                return true;
        } else {
            if (azimuth > minAngle && azimuth < maxAngle)
                return true;
        }
        return false;
    }

    /**
     * A small helper function. Here I'm setting up the onClickListener for the imageView.
     */
    private void stolperSteinOnClickListener(){
        mFirebaseAnalytics.setUserProperty("clicked_victim", getListOfVictimsNames());
        mFirebaseAnalytics.setUserProperty("users_position",
                Double.toString(getLatitude()) + " " + Double.toString(getLongitude()));
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreenActivity.this, DetailsActivity.class);
                intent.putParcelableArrayListExtra(CURRENT_OBJECT, mListStolperSteine);
                startActivity(intent);
            }
        });
    }

    /**
     * If the arc of the azimuth has changed this function will be used.
     * @param azimuthFrom starting arc
     * @param azimuthChangedTo ending arc
     */
    @Override
    public void onAzimuthChanged(float azimuthFrom, float azimuthChangedTo) {
        mAzimuthReal = azimuthChangedTo;
        mAzimuthTheoretical = calculateTheoreticalAzimuth();

        double minAngle = calculateAzimuthAccuracy(mAzimuthTheoretical).get(0);
        double maxAngle = calculateAzimuthAccuracy(mAzimuthTheoretical).get(1);

        if (isBetween(minAngle, maxAngle, mAzimuthReal)) {
            mImageView.setVisibility(View.VISIBLE);
            stolperSteinOnClickListener();
        } else {
           mImageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStop() {
        myCurrentAzimuth.stop();
        super.onStop();
    }

    /**
     * With this function the user can remove all of the stored data.
     */
    public void deleteEveryFavElement ()
    {
        int rows = getContentResolver().delete(StolperSteineContract.InventoryEntry.CONTENT_URI, null, null);
        if ( rows == 0){
            Snackbar.make
                    (findViewById(android.R.id.content),
                            getString(R.string.snackbar_empty_local_database_failed)
                    ,Snackbar.LENGTH_LONG).show();
        } Snackbar.make
            (findViewById(android.R.id.content),
                    getString(R.string.snackbar_empty_local_database_success)
                    ,Snackbar.LENGTH_LONG).show();
    }


    /**
     * The conformation dialog for removing all of the data
     */
    public void conformationDialog(){

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(R.string.alertdailog_message))
        .setCancelable(false)
        .setPositiveButton(getString(R.string.alertdailog_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteEveryFavElement();
            }
        })
        .setNegativeButton(R.string.alertdailog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = dialog.create();
        alert.setTitle(getString(R.string.alertdailog_title));
        alert.show();
    }
}
