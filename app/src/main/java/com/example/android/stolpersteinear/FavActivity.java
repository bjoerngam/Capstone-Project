package com.example.android.stolpersteinear;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.android.stolpersteinear.data.database.StolperSteineContract;
import com.example.android.stolpersteinear.utils.Adapter.FavoritesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjoern on 08.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The activty for displaying the local store victims.
 */

public class FavActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID_STORED_DATA = 1010;

    final String[] projection = new String[]{
            StolperSteineContract.InventoryEntry._ID,
            StolperSteineContract.InventoryEntry.Description,
            StolperSteineContract.InventoryEntry.Image,
            StolperSteineContract.InventoryEntry.Street,
            StolperSteineContract.InventoryEntry.Website,
            StolperSteineContract.InventoryEntry.Name,
            StolperSteineContract.InventoryEntry.Date_of_death,
            StolperSteineContract.InventoryEntry.Date_of_birth,
            StolperSteineContract.InventoryEntry.Latitude,
            StolperSteineContract.InventoryEntry.Longitude
    };

    /**
     * GUI binding via butter knife
     **/
    @BindView(R.id.listViewStored)
    ListView listViewStored;
    @BindView(R.id.toolbar_local_stored_data)
    Toolbar mToolbarDetails;

    private FavoritesAdapter favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        ButterKnife.bind(this);
        mToolbarDetails.setElevation(4);
        mToolbarDetails.setTitle(getString(R.string.fav_title_toolbar));
        mToolbarDetails.setSubtitle(getString(R.string.fav_subtitle));
        setSupportActionBar(mToolbarDetails);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true); }

        favoritesAdapter = new FavoritesAdapter(getApplicationContext(), null);
        listViewStored.setAdapter(favoritesAdapter);
        getSupportLoaderManager().initLoader(LOADER_ID_STORED_DATA, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.CursorLoader(this,
                StolperSteineContract.InventoryEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favoritesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favoritesAdapter.swapCursor(null);
    }
}
