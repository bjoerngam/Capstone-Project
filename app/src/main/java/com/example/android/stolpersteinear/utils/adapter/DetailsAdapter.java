package com.example.android.stolpersteinear.utils.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.android.stolpersteinear.R;
import com.example.android.stolpersteinear.data.StolperSteine;
import com.example.android.stolpersteinear.data.database.StolperSteineContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjoern on 06.09.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The adapter class for the victims detail activity
 */
public class DetailsAdapter extends ArrayAdapter<StolperSteine> {

    /**
     * UI images size for picasso
     **/
    private final static int IMAGE_WIDTH = 100;
    private final static int IMAGE_HEIGHT = 100;

    /**
     * Binding the GUI elements via butter knife
     */
    @BindView(R.id.imageVictim)
    ImageView mVictimImageView;
    @BindView(R.id.textviewVictimName)
    TextView mVictimNameTextView;
    @BindView(R.id.textviewVictimBornDate)
    TextView mVictimBornDateTextView;
    @BindView(R.id.textviewVictimDeathDate)
    TextView mVictimDeathDateTextView;
    @BindView(R.id.textviewVictimAddress)
    TextView mVictimAddressTextView;
    @BindView(R.id.textviewVictimDescription)
    TextView mVictimDescriptionTextView;
    @BindView(R.id.saveDataSet)
    RadioButton mSaveButton;
    @BindView(R.id.wwwImageView)
    ImageView mWWWImageView;

    private ArrayList<StolperSteine> mCurrentList;
    private Context mContext;
    private View mView;
    /**
     * Content URI for the existing victims entry (null if it's a new victims entry)
     */
    private Uri mCurrentVictimURI;
    /**
     * With this string I will look for the victim if he or she is already a entry in the local database
     **/
    private String mCurrentVictimName;
    /**
     * With this boolean we are checking if a victim is already saved.
     **/
    private boolean mVictimSaved;

    /**
     * The default constructor of the DetailsAdapter class.
     * @param mContext the context is the DetailsActivity class.
     * @param currentList the arraylist of the found object at the GPS position.
     */

    public DetailsAdapter(Context mContext, ArrayList<StolperSteine> currentList) {
        super(mContext, R.layout.detailslist, currentList);
        this.mContext = mContext;
        this.mCurrentList = currentList;
    }

    /**
     * Building the view and using the view holder pattern.
     * @param position The current position.
     * @param convertView The convertView.
     * @param parent The parent viewGroup.
     * @return The created view.
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.detailslist, parent, false);
            ButterKnife.bind(this, convertView);
            viewHolder.mVictimAddressTextView = mVictimAddressTextView;
            viewHolder.mVictimBornDateTextView = mVictimBornDateTextView;
            viewHolder.mVictimDeathDateTextView = mVictimDeathDateTextView;
            viewHolder.mVictimDescriptionTextView = mVictimDescriptionTextView;
            viewHolder.mVictimImageView = mVictimImageView;
            viewHolder.mVictimNameTextView = mVictimNameTextView;
            viewHolder.mRadioButton = mSaveButton;
            viewHolder.mWWWImageView = mWWWImageView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mVictimNameTextView.setText(mCurrentList.get(position).getVName());
        viewHolder.mVictimBornDateTextView.setText(mCurrentList.get(position).getDateOfBirth());
        viewHolder.mVictimDeathDateTextView.setText(mCurrentList.get(position).getDateOfDeath());
        viewHolder.mVictimDescriptionTextView.setText(mCurrentList.get(position).getVDescription());
        viewHolder.mVictimAddressTextView.setText(mCurrentList.get(position).getStreet());
        mCurrentVictimName = mCurrentList.get(position).getVName();

        // Insert the image
        Picasso.with(mContext).load(mCurrentList.get(position).getImage())
                .resize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .placeholder(R.drawable.stolpersteine_small)
                .into(viewHolder.mVictimImageView);

        // Check if the movie is already saved in our database.
        isAlreadySaved();
        mView = parent.getRootView();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mVictimSaved) {
                    saveVictim(position);
                } else {
                    Snackbar.make(mView,
                            mContext.getString(R.string.victim_is_already_saved)
                            , Snackbar.LENGTH_LONG).show();
                }
            }
        });
        //Adding an onClickListener to the small WWW icon and setting up an Intent for
        //opening the given URL or if the string is empty: http://stolpersteine.eu
        mWWWImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mCurrentList.get(position).getWebsite()));
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * Saves the current values in to the database
     * @param position the current cursor position
     */
    private void saveVictim(int position) {

        ContentValues values = new ContentValues();

        values.put(
                StolperSteineContract.InventoryEntry.Name,
                mCurrentList.get(position).getVName());

        values.put(StolperSteineContract.InventoryEntry.Date_of_birth,
                mCurrentList.get(position).getDateOfBirth());

        values.put(StolperSteineContract.InventoryEntry.Date_of_death,
                mCurrentList.get(position).getDateOfDeath());

        values.put(StolperSteineContract.InventoryEntry.Description,
                mCurrentList.get(position).getVDescription());

        values.put(StolperSteineContract.InventoryEntry.Street,
                mCurrentList.get(position).getStreet());

        values.put(StolperSteineContract.InventoryEntry.Website,
                mCurrentList.get(position).getWebsite());

        values.put(StolperSteineContract.InventoryEntry.Image,
                mCurrentList.get(position).getImage());

        if (mCurrentVictimURI == null) {

            Uri newUri
                    = mContext.getContentResolver().insert(StolperSteineContract.InventoryEntry.CONTENT_URI, values);

            if (newUri == null) {
                Snackbar.make(mView,
                        mContext.getString(R.string.save_victim_entry_failed)
                        , Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(mView,
                        mContext.getString(R.string.save_victim_entry_success)
                        , Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Check if the victim is already saved in our database.
     */
    private void isAlreadySaved() {
        Cursor mCursor;
        String selection = StolperSteineContract.InventoryEntry.Name + " = ?";
        String[] projection = {
                StolperSteineContract.InventoryEntry._ID,
                StolperSteineContract.InventoryEntry.Name};
        String[] selectionArgs = {mCurrentVictimName};

        mCursor = mContext.getContentResolver().query(StolperSteineContract.InventoryEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        if (mCursor != null && mCursor.getCount() != 0) {
            // If the return value of the cursor is zero the movie is already saved.
            mSaveButton.setChecked(true);
            mVictimSaved = true;
            mCursor.close();
        } else {
            // There is currently no entry at the local database.
            mSaveButton.setChecked(false);
            mVictimSaved = false;
            if (mCursor != null) {
                mCursor.close(); }
        }
    }

    /**
     * Just to show I also know the view holder pattern class.
     */
    private static class ViewHolder {
        ImageView mVictimImageView;
        TextView mVictimNameTextView;
        TextView mVictimBornDateTextView;
        TextView mVictimDeathDateTextView;
        TextView mVictimAddressTextView;
        TextView mVictimDescriptionTextView;
        RadioButton mRadioButton;
        ImageView mWWWImageView;
    }
}
