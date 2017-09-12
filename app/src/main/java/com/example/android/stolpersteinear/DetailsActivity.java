package com.example.android.stolpersteinear;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.android.stolpersteinear.data.StolperSteine;
import com.example.android.stolpersteinear.utils.Adapter.DetailsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjoern on 08.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The activty for displaying the details of the stolpersteine.
 */

public class DetailsActivity extends AppCompatActivity {

    /**
     * GUI binding via butter knife
     **/
    @BindView(R.id.detailsList)
    ListView mDetailsList;

    @BindView(R.id.toolbar_details)
    Toolbar mToolbarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        /* Setting up a nice toolbar **/
        mToolbarDetails.setElevation(4);
        mToolbarDetails.setTitle(getString(R.string.toolbar_details));
        mToolbarDetails.setSubtitle(getString(R.string.toolbar_subtitle_details));
        setSupportActionBar(mToolbarDetails);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        /* toolbar end **/

        ArrayList<StolperSteine> mStolperSteine
                = getIntent().getParcelableArrayListExtra(MainScreenActivity.CURRENT_OBJECT);

        DetailsAdapter adapter;
        adapter = new DetailsAdapter(getApplicationContext(), mStolperSteine);
        mDetailsList.setAdapter(adapter);
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
}
