package com.devup.qcm.sdk;

import android.os.Parcel;
import android.os.Parcelable;

import com.devup.qcm.sdk.interfaces.JSONable;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable, JSONable {
    String id, identifier, password, pseudo;
    public String metaData;

    public User() {
    }

    public User(String user_id, String email, String password) {
        this.id = user_id;
        this.identifier = email;
        this.password = password;
    }

    public User setIdentifier(String email) {
        this.identifier = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setUserId(String user_id) {
        this.id = user_id;
        return this;
    }

    public String getEmail() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public User setPseudo(String pseudo) {
        this.pseudo = pseudo;
        return this;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toString());

    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return User.fromJson(source.readString());
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public final static User fromJson(String json) {
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        return user;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return new JSONObject(toString());
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
