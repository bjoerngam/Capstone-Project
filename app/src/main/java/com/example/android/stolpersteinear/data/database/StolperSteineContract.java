package com.example.android.stolpersteinear.data.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bjoern on 08.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The contract for the local database
 */

public class StolperSteineContract {

    // With the default constructor set as private it's not possible
    // to create an object of the class
    private StolperSteineContract() {}

    // Here we are setting up the default content_authority uri
    static final String CONTENT_AUTHORITY = "com.example.android.stolpersteinear";

    //Here we are build the complete uri int two steps
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_PRODUCT = "StolperSteineAR";

    public static final class InventoryEntry implements BaseColumns {

        // the complete content_uri for getting access to the database
        public static final Uri CONTENT_URI
                = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);

        public static final String TABLE_NAME = "stolpersteine";                    // the name of the table inside the database

        // Here is the definition of our database layout;
        public static final String _ID = BaseColumns._ID;                          // the unique id of the entry

        public static final String Description = "description";                     // the text of the stolperstein

        public static final String Image = "image";                                 // the name of the image

        public static final String Street = "street";                               // the address of the stolperstein

        public static final String Website = "website";                             // the historical text of the stolperstein

        public static final String Name = "name";                                   // the name of the person at the stolperstein

        public static final String Date_of_death = "date_of_death";                 // the death date of the victim

        public static final String Date_of_birth ="date_of_birth";                   // the birth date of the victim

        public static final String Latitude = "latitude";                            // the latitude GPS coordinate

        public static final String Longitude = "longitude";                          // the longitude GPS coordinate

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of ware.
         */
        public static final String CONTENT_LIST_TYPE
                = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT ;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE
                = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT ;
    }
}
