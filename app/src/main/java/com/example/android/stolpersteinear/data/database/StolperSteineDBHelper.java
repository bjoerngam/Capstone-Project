package com.example.android.stolpersteinear.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bjoern on 24.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: For creating / updating the database. The basic function.
 */

public class StolperSteineDBHelper extends SQLiteOpenHelper {

    //Name of the database file
    private static final String DATABASE_NAME = "stolpersteine.db";

    //Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    //Constructor
    StolperSteineDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_TABLE = "CREATE TABLE " + StolperSteineContract.InventoryEntry.TABLE_NAME + " ("
                + StolperSteineContract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StolperSteineContract.InventoryEntry.Name + " TEXT, "
                + StolperSteineContract.InventoryEntry.Description + " TEXT, "
                + StolperSteineContract.InventoryEntry.Street + " TEXT, "
                + StolperSteineContract.InventoryEntry.Image + " TEXT, "
                + StolperSteineContract.InventoryEntry.Website + " TEXT, "
                + StolperSteineContract.InventoryEntry.Date_of_birth + " TEXT, "
                + StolperSteineContract.InventoryEntry.Date_of_death + " TEXT, "
                + StolperSteineContract.InventoryEntry.Latitude + " REAL, "
                + StolperSteineContract.InventoryEntry.Longitude + " REAL)";

        // Execute the SQL statement and creating the database
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Currently nothing to do there.
    }
}
