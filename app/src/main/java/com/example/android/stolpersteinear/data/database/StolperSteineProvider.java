package com.example.android.stolpersteinear.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by bjoern on 24.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The SQLHelper class supports the basic SQL operations like query, insert, delete etc.
 */
public class StolperSteineProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = StolperSteineProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the stolpersteine table
     */
    private static final int ELEMENTS = 100;

    /**
     * URI matcher code for the content URI for a single stolperstein in the stolpersteine table
     */
    private static final int ELEMENT = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.stolpersteinear/stolpersteinear" will map to the
        // integer code {@link #products}. This URI is used to provide access to MULTIPLE rows
        // of the products table.
        sUriMatcher.addURI(StolperSteineContract.CONTENT_AUTHORITY,
                StolperSteineContract.PATH_PRODUCT, ELEMENTS);

        // The content URI of the form "content:/com.example.android.stolpersteinear/stolpersteinear/#" will map to the
        // integer code {@link #PRODUCT_ID}. This URI is used to provide access to ONE single row
        // of the products table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.stolpersteinear/stolpersteinear/3" matches, but
        // "content://com.example.android.stolpersteinear/stolpersteinear" (without a number at the end) doesn't match.
        sUriMatcher.addURI(StolperSteineContract.CONTENT_AUTHORITY,
                StolperSteineContract.PATH_PRODUCT + "/#", ELEMENT);
    }

    // The database helper object
    StolperSteineDBHelper mDbHelper;
    // The current context for getting ride off the NullPointerException
    Context mContext;

    /**
     * Creates the database
     *
     * @return true
     */
    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = new StolperSteineDBHelper(mContext);
        return true;
    }

    /**
     * The basic SQL query method
     * @param uri The url of the database: StolperSteineContract.InventoryEntry.CONTENT_URI
     * @param projection Here we will define the String[] in this we define which columns will be used.
     * @param selection We we will use the selection for getting all or only one element.
     * @param selectionArgs Not used in this project.
     * @param sortOrder Not used in this project.
     * @return the cursor / the position of the current dataset.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;
        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {

            case ELEMENTS:
                // If we want to get all of information we will call this query statement
                cursor = database.query(StolperSteineContract.InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case ELEMENT:
                // If we want to get a special row ot the database? So we will use this sql
                // statement
                selection = StolperSteineContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StolperSteineContract.InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(mContext.getContentResolver(), uri);
        // Return the cursor
        return cursor;
    }

    /**
     * Here we check if we are select every element or a single element.
     * @param uri the URL
     * @return The proper type list or element
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ELEMENTS:
                return StolperSteineContract.InventoryEntry.CONTENT_LIST_TYPE;
            case ELEMENT:
                return StolperSteineContract.InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    /**
     * Insert the values into the database
     * @param uri The url of the database: StolperSteineContract.InventoryEntry.CONTENT_URI
     * @param contentValues The values which should be insert.
     * @return starts the function insertEntries
     */

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ELEMENTS:
                return insertEntries(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert the values into the database
     * @param uri The url of the database: StolperSteineContract.InventoryEntry.CONTENT_URI
     * @param values The values which should be insert.
     * @return the new URI
     */

    private Uri insertEntries(Uri uri, ContentValues values) {

        // Get write able database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new product with the given values
        long id = database.insert(StolperSteineContract.InventoryEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            return null;
        }
        // Notify all listeners that the data has changed for the product content URI
        mContext.getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Remove the data out of the database.
     * @param uri The url of the database: StolperSteineContract.InventoryEntry.CONTENT_URI
     * @param selection We we will use the selection for getting all or only one element.
     * @param selectionArgs The argument for the selection.
     * @return the number of deleted rows.
     */

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get write able database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ELEMENTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete
                        (StolperSteineContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ELEMENT:
                // Delete a single row given by the ID in the URI
                selection = StolperSteineContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete
                        (StolperSteineContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * The update function for the SQL database. If more elements has to be
     * updated we will use directly updateEntry. If it's a single element we will
     * add the selectionArgs.
     * @param uri The url of the database: StolperSteineContract.InventoryEntry.CONTENT_URI
     * @param contentValues The values which should be updated.
     * @param selection We we will use the selection for getting all or only one element.
     * @param selectionArgs The argument for the selection.
     * @return starts the updateEntry function.
     */

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ELEMENTS:
                return updateEntry(uri, contentValues, selection, selectionArgs);
            case ELEMENT:
                // For the Entry_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = StolperSteineContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateEntry(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * The function for updating the database.
     *
     * @param uri           The url of the database: StolperSteineContract.InventoryEntry.CONTENT_URI
     * @param contentValues The values which should be updated.
     * @param selection     We we will use the selection for getting all or only one element.
     * @param selectionArgs The argument for the selection.
     * @return the number of updated rows.
     */

    public int updateEntry(Uri uri, ContentValues contentValues,
                             String selection, String[] selectionArgs) {

        // Get write able access for our SQL database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //Track the number of rows, that has changed
        int rowsUpdated = database.update(StolperSteineContract.InventoryEntry.TABLE_NAME,
                contentValues, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
