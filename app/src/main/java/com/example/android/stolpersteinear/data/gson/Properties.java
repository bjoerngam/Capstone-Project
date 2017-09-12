package com.example.android.stolpersteinear.data.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bjoern on 09.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description:
 * The class was created with: http://www.jsonschema2pojo.org/
 */
public class Properties {

    @SerializedName("@id")
    @Expose
    private String id;
    @SerializedName("historic")
    @Expose
    private String historic;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("memorial:addr")
    @Expose
    private String memorialAddr;
    @SerializedName("memorial:text")
    @Expose
    private String memorialText;
    @SerializedName("memorial:type")
    @Expose
    private String memorialType;
    @SerializedName("memorial:website")
    @Expose
    private String memorialWebsite;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("person:date_of_birth")
    @Expose
    private String personDateOfBirth;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("@timestamp")
    @Expose
    private String timestamp;
    @SerializedName("@version")
    @Expose
    private Integer version;
    @SerializedName("@changeset")
    @Expose
    private Integer changeset;
    @SerializedName("@user")
    @Expose
    private String user;
    @SerializedName("@uid")
    @Expose
    private Integer uid;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("memorial:ref")
    @Expose
    private String memorialRef;
    @SerializedName("person:date_of_death")
    @Expose
    private String personDateOfDeath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Properties withId(String id) {
        this.id = id;
        return this;
    }

    public String getHistoric() {
        return historic;
    }

    public void setHistoric(String historic) {
        this.historic = historic;
    }

    public Properties withHistoric(String historic) {
        this.historic = historic;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Properties withImage(String image) {
        this.image = image;
        return this;
    }

    public String getMemorialAddr() {
        return memorialAddr;
    }

    public void setMemorialAddr(String memorialAddr) {
        this.memorialAddr = memorialAddr;
    }

    public Properties withMemorialAddr(String memorialAddr) {
        this.memorialAddr = memorialAddr;
        return this;
    }

    public String getMemorialText() {
        return memorialText;
    }

    public void setMemorialText(String memorialText) {
        this.memorialText = memorialText;
    }

    public Properties withMemorialText(String memorialText) {
        this.memorialText = memorialText;
        return this;
    }

    public String getMemorialType() {
        return memorialType;
    }

    public void setMemorialType(String memorialType) {
        this.memorialType = memorialType;
    }

    public Properties withMemorialType(String memorialType) {
        this.memorialType = memorialType;
        return this;
    }

    public String getMemorialWebsite() {
        return memorialWebsite;
    }

    public void setMemorialWebsite(String memorialWebsite) {
        this.memorialWebsite = memorialWebsite;
    }

    public Properties withMemorialWebsite(String memorialWebsite) {
        this.memorialWebsite = memorialWebsite;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties withName(String name) {
        this.name = name;
        return this;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Properties withNetwork(String network) {
        this.network = network;
        return this;
    }

    public String getPersonDateOfBirth() {
        return personDateOfBirth;
    }

    public void setPersonDateOfBirth(String personDateOfBirth) {
        this.personDateOfBirth = personDateOfBirth;
    }

    public Properties withPersonDateOfBirth(String personDateOfBirth) {
        this.personDateOfBirth = personDateOfBirth;
        return this;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Properties withPos(String pos) {
        this.pos = pos;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Properties withWebsite(String website) {
        this.website = website;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Properties withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Properties withVersion(Integer version) {
        this.version = version;
        return this;
    }

    public Integer getChangeset() {
        return changeset;
    }

    public void setChangeset(Integer changeset) {
        this.changeset = changeset;
    }

    public Properties withChangeset(Integer changeset) {
        this.changeset = changeset;
        return this;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Properties withUser(String user) {
        this.user = user;
        return this;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Properties withUid(Integer uid) {
        this.uid = uid;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Properties withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMemorialRef() {
        return memorialRef;
    }

    public void setMemorialRef(String memorialRef) {
        this.memorialRef = memorialRef;
    }

    public Properties withMemorialRef(String memorialRef) {
        this.memorialRef = memorialRef;
        return this;
    }

    public String getPersonDateOfDeath() {
        return personDateOfDeath;
    }

    public void setPersonDateOfDeath(String personDateOfDeath) {
        this.personDateOfDeath = personDateOfDeath;
    }

    public Properties withPersonDateOfDeath(String personDateOfDeath) {
        this.personDateOfDeath = personDateOfDeath;
        return this;
    }
}
