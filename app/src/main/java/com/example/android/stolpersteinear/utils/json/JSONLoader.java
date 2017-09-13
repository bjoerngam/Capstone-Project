package com.example.android.stolpersteinear.utils.json;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.stolpersteinear.data.StolperSteine;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Bjoern on 08.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Loads the JSON data out of the firebase enviornment.
 */
public class JSONLoader extends AsyncTaskLoader<ArrayList<StolperSteine>> {

    private double longitude;
    private double latitude;

    public JSONLoader(Context context, double longitude, double latitude) {
        super(context);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Gets the data
     * @return the JSON data
     */
    @Override
    public ArrayList<StolperSteine> loadInBackground() {
        StolperSteineGSONReader stolperSteineGSONReader;

        // Perform the network request, parse the response, and extract a list of stolpersteine.
        try {
            stolperSteineGSONReader = new StolperSteineGSONReader();
            return stolperSteineGSONReader.getStolperSteine(longitude, latitude);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
