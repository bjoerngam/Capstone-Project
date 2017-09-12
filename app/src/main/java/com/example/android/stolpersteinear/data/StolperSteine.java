package com.example.android.stolpersteinear.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by bjoern on 23.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The basic class for the stolpersteine
 */
public class StolperSteine implements Parcelable {

    public static final Creator<StolperSteine> CREATOR = new Creator<StolperSteine>() {
        @Override
        public StolperSteine createFromParcel(Parcel in) {
            return new StolperSteine(in);
        }

        @Override
        public StolperSteine[] newArray(int size) {
            return new StolperSteine[size];
        }
    };
    private static final String BASE_URL
            = "https://commons.wikimedia.org/wiki/";    // Sometimes the use URL is not complete So here is the Prefix.
    private String name;                                //the name of the victim
    private String website;                             //website with more victim information
    private String street;                              //the street where you can find the stolperstein
    private String image;                               //image of the victim or the stolperstein
    private String description;                         //description of the victim
    private String dateOfBirth;                         //date of birth
    private String dateOfDeath;                         //date of death
    private Double latitude;                            // latitude of the current stolperstein
    private Double longitude;                           // longitude of the current stolperstein

    /**
     * The basic constructor. The constructor is used inside the GSONBuilder.
     * @param name The name of the victim (first- and last name).
     * @param website The website with more information about the website (mostly not set).
     * @param street The street / address of the victim and the stolperstein.
     * @param image The image of the stolperstein. Currently I'm using only the placeholder.
     *              Because of the WIKIPEDIA parsing problem. The used HTTP address is not
     *              the URL to the correct image.
     * @param description Here we setting up the description of the victims fate.
     * @param dateOfBirth When was the victim born.
     * @param dateOfDeath When died the victim.
     * @param latitude Latitude of the OpenStreetMap point.
     * @param longitude Longitude of the OpenStreetMap point.
     */
    public StolperSteine(String name, String website, String street, String image, String description,
                         String dateOfBirth, String dateOfDeath, Double latitude, Double longitude) {

        setVName(name);
        setWebsite(website);
        setStreet(street);
        setImage(image);
        setVDescription(description);
        setDateOfBirth(dateOfBirth);
        setDateOfDeath(dateOfDeath);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    /**
     * The constructor for the parcel mechanism
     *
     * @param in Parcel object
     */

    private StolperSteine(Parcel in) {
        name = in.readString();
        website = in.readString();
        street = in.readString();
        image = in.readString();
        description = in.readString();
        dateOfBirth = in.readString();
        dateOfDeath = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();

    }

    /**
     * The basic get methods
     **/
    public String getVName() {
        return checkStringNotNull(name); }

    /*** The basic set methods **/
    private void setVName(String name) { this.name = name; }

    public String getStreet() {
        return checkStringNotNull(street);
    }

    private void setStreet(String street) {
        this.street = street;
    }

    public String getDateOfBirth() {
        return checkStringNotNull(dateOfBirth);
    }

    private void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;}

    public String getWebsite() {
        return checkStringNotNull(website);
    }

    /**
     * The quality of the JSON data is not quite well. For the correct intent
     * I have to add the correct protocol and if the website string is empty
     * I will us the basic page
     *
     * @param website URL of the website
     */
    private void setWebsite(String website) {
        if (!TextUtils.isEmpty(website)) {
            if (!website.startsWith("htt")) {
                this.website = "http://" + website;
            } else {
                this.website = website;
            }
        } else this.website = "http://stolperstein.eu";
    }

    public String getImage() {
        return checkStringNotNull(image);
    }

    /**
     * We have to make sure if we using a proper image address.
     * Currently most of the images are located at wikipedia.
     * I have to find a solution for getting the correct image.
     * Properly there is a script for getting the correct URL.
     * @param image the URL of an Image
     */
    private void setImage(String image) {
        if (TextUtils.isEmpty(image)){
            this.image = "Currently not set";
            return;
        }
        if (!image.startsWith(BASE_URL)) {
            this.image = BASE_URL + image;
        } else {
            this.image = image;
        }
    }

    public String getVDescription() {
        return checkStringNotNull(description);
    }

    private void setVDescription(String description) {
        this.description = description;
    }

    public String getDateOfDeath() {
        return checkStringNotNull(dateOfDeath);
    }

    private void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public Double getLongitude() {
        return longitude;
    }

    private void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    private void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /** A small helper class for checking if the value is empty **/
    private String checkStringNotNull(String value) {
        if (TextUtils.isEmpty(value)) {
            return "Currently Unknown"; }
        return value;
    }

    //*** Everything around Parcelable of the StolperSteine objects ***/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getVName());
        parcel.writeString(getWebsite());
        parcel.writeString(getStreet());
        parcel.writeString(getImage());
        parcel.writeString(getVDescription());
        parcel.writeString(getDateOfBirth());
        parcel.writeString(getDateOfDeath());
        parcel.writeDouble(getLatitude());
        parcel.writeDouble(getLongitude());
    }
    //*** End of Parcelable ***/
}
