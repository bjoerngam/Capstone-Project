package com.example.android.stolpersteinear.utils.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.stolpersteinear.R;
import com.example.android.stolpersteinear.data.database.StolperSteineContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.stolpersteinear.R.id.textviewStoredVictimAddress;

/**
 * Created by bjoern on 08.09.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The adapter for the favorites.
 */
public class FavoritesAdapter extends CursorAdapter {

    /**
     * UI images size for picasso
     **/
    private final static int IMAGE_WIDTH = 100;
    private final static int IMAGE_HEIGHT = 100;

    /**
     * GUI binding via butter knife
     **/
    @BindView(R.id.imageStoredVictim)
    ImageView mVictimImageView;
    @BindView(R.id.textviewStoredVictimName)
    TextView mVictimNameTextView;
    @BindView(R.id.textviewStoredVictimBornDate)
    TextView mVictimBornDateTextView;
    @BindView(R.id.textviewStoredVictimDeathDate)
    TextView mVictimDeathDateTextView;
    @BindView(textviewStoredVictimAddress)
    TextView mVictimAddressTextView;
    @BindView(R.id.textviewStoredVictimDescription)
    TextView mVictimDescriptionTextView;
    @BindView(R.id.wwwStoredImageView)
    ImageView mWWWImageView;

    private Context context;
    /**
     * The basic constructor
     * @param context The context is the FavoritesActivity.
     * @param c The cursor created by the FavoritesActivity.
     */
    public FavoritesAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.context = context;
    }

    /**
     * Inflating the layout
     * @param context The context is the FavoritesActivity.
     * @param cursor The cursor created by the FavoritesActivity.
     * @param viewGroup The ViewGroup.
     * @return the inflated layout.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.favoriteslist,
                viewGroup, false);
    }

    /**
     * Getting the values out of the local database and assigning them to the view
     * @param view Is the view out of newView (R.layout.favoriteslist).
     * @param context The context is the FavoritesActivity.
     * @param cursor The cursor created by the FavoritesActivity.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        ButterKnife.bind(this, view);

        mVictimNameTextView
                .setText(cursor.getString(cursor.getColumnIndex(StolperSteineContract.InventoryEntry.Name)));
        mVictimAddressTextView
                .setText(cursor.getString(cursor.getColumnIndex(StolperSteineContract.InventoryEntry.Street)));
        mVictimBornDateTextView
                .setText(cursor.getString(cursor.getColumnIndex(StolperSteineContract.InventoryEntry.Date_of_birth)));
        mVictimDeathDateTextView
                .setText(cursor.getString(cursor.getColumnIndex(StolperSteineContract.InventoryEntry.Date_of_death)));
        mVictimDescriptionTextView
                .setText(cursor.getString(cursor.getColumnIndex(StolperSteineContract.InventoryEntry.Description)));

        //*** Insert the image also via the URL **/
        Picasso.with(view.getContext())
                .load(cursor.getString(cursor.getColumnIndex(StolperSteineContract.InventoryEntry.Image)))
                .resize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .placeholder(R.drawable.stolpersteine_small)
                .into(mVictimImageView);

        //*** Adding the correct URL ***/
        mWWWImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(cursor.getString(cursor.getColumnIndex(StolperSteineContract.InventoryEntry.Website))));
                context.startActivity(intent);
            }
        });
    }
}
