package com.example.android.stolpersteinear.utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bjoern on 08.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Is building the proper URL string for the FireBase data storage.
 * Note: I am using a anonymous access to the file. So everyone who knows the URL has
 * access to the file.
 */
public class URLBuilder {

    public final static URL BASE_URL;

    private final static String
            URL = "https://firebasestorage.googleapis.com/v0/b/stolpersteinar.appspot.com/o/stolpersteine-cologne.json?alt=media&token=69642de6-58f7-41ab-aa7d-2b3114e34cb5";

    static {
        URL url = null;
        try {
            url = new URL( URL );
        } catch (MalformedURLException ignored) {
            ignored.printStackTrace();
        }
        BASE_URL = url;
    }
}
