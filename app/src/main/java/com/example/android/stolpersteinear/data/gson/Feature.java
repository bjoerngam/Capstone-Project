package com.example.android.stolpersteinear.data.gson;

/**
 * Created by bjoern on 23.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description:
 * The class was created with: http://www.jsonschema2pojo.org/
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feature {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("properties")
        @Expose
        private Properties properties;
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;
        @SerializedName("id")
        @Expose
        private String id;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Feature withType(String type) {
            this.type = type;
            return this;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public Feature withProperties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public Feature withGeometry(Geometry geometry) {
            this.geometry = geometry;
            return this;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Feature withId(String id) {
            this.id = id;
            return this;
        }
}