package com.devup.qcm.sdk;

import org.json.JSONException;
import org.json.JSONObject;

import com.devup.qcm.sdk.interfaces.JSONable;
import com.google.gson.Gson;

import android.os.Parcel;
import android.os.Parcelable;

public class Subject implements Parcelable, JSONable {

    private String id;
    private String id_parent;
    private String title;
    private String url_picture;
    private String description;

    public Subject() {

    }

    public Subject(String idSubject, String idParent, String title,
                   String urlImage, String description) {
        super();
        this.id = idSubject;
        this.id_parent = idParent;
        this.title = title;
        this.url_picture = urlImage;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public Subject setId(String idSubject) {
        this.id = idSubject;
        return this;
    }

    public String getParentId() {
        return id_parent;
    }

    public Subject setParentId(String idParent) {
        this.id_parent = idParent;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Subject setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPictureURL() {
        return url_picture;
    }

    public Subject setPictureURL(String urlImage) {
        this.url_picture = urlImage;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Subject setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toString());
    }

    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel source) {
            try {
                return Subject.fromJson(source.readString());
            } catch (Exception e) {
                return new Subject();
            }
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public final static Subject fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Subject.class);
    }

    public JSONObject toJson() throws JSONException {
        return new JSONObject(toString());
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Subject fromJson(JSONObject jsonSubject) {
        if (jsonSubject == null) {
            return null;
        }
        return fromJson(jsonSubject.toString());
    }
}
