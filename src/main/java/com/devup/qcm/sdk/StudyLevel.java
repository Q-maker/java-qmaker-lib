package com.devup.qcm.sdk;

import org.json.JSONException;
import org.json.JSONObject;

import com.devup.qcm.sdk.interfaces.JSONable;
import com.google.gson.Gson;

import android.os.Parcel;
import android.os.Parcelable;

public class StudyLevel implements Parcelable, JSONable {
    String id, id_parent = "0", title, description, pictureURL;

    public StudyLevel(String id_level, String id_parentLevel) {
        this.id = id_level;
        this.id_parent = id_parentLevel;
    }

    public StudyLevel(String id_level) {
        this.id = id_level;
    }

    public StudyLevel setLevelId(String id_level) {
        this.id = id_level;
        return this;
    }

    public StudyLevel setParentId(String id_parentLevel) {
        this.id_parent = id_parentLevel;
        return this;
    }

    public StudyLevel setDescription(String levelDescription) {
        this.description = levelDescription;
        return this;
    }

    public StudyLevel setTitle(String levelTitle) {
        this.title = levelTitle;
        return this;
    }

    public void setPictureURL(String urlLevelPicture) {
        this.pictureURL = urlLevelPicture;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return id_parent;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public static final Parcelable.Creator<StudyLevel> CREATOR = new Parcelable.Creator<StudyLevel>() {
        @Override
        public StudyLevel createFromParcel(Parcel source) {
            try {
                return StudyLevel.fromJson(source.readString());
            } catch (Exception e) {
                return new StudyLevel("NONE");
            }
        }

        @Override
        public StudyLevel[] newArray(int size) {
            return new StudyLevel[size];
        }
    };

    public final static StudyLevel fromJson(JSONObject json) {
        if (json == null) {
            return null;
        }
        return fromJson(json.toString());
    }

    public final static StudyLevel fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, StudyLevel.class);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public JSONObject toJson() throws JSONException {
        return new JSONObject(toString());
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(toString());
    }

}
