package com.example.android.stolpersteinear.data.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bjoern on 22.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description:
 * The class was created with: http://www.jsonschema2pojo.org/
 */
public class Stolpersteinedata {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("generator")
    @Expose
    private String generator;
    @SerializedName("copyright")
    @Expose
    private String copyright;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Stolpersteinedata withType(String type) {
        this.type = type;
        return this;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public Stolpersteinedata withGenerator(String generator) {
        this.generator = generator;
        return this;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Stolpersteinedata withCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Stolpersteinedata withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public Stolpersteinedata withFeatures(List<Feature> features) {
        this.features = features;
        return this;
    }
}


