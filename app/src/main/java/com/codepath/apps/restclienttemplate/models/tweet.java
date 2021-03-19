package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import java.util.ArrayList;
import java.util.List;
@Parcel

public class tweet {
    public String body;
    public String createdAt;
    public user User;
    public long id;

    //Empty constructor for parcel
    public tweet(){}

    public static tweet fromJson(JSONObject jsobject) throws JSONException {
        tweet tw = new tweet();
        tw.body = jsobject.getString("full_text");
        tw.createdAt = jsobject.getString("created_at");
        tw.User = user.fromJson(jsobject.getJSONObject("user"));
        tw.id = jsobject.getLong("id");
        return tw;
    }

    public static List<tweet> fromJsonArray(JSONArray arr) throws JSONException {
        List<tweet> tw = new ArrayList<>();
        for(int i = 0;i<arr.length();i++){
            tw.add(fromJson(arr.getJSONObject(i)));
        }
        return tw;
    }

    public String getFormattedTimestamp(tweet tw){
        return TimeFormatter.getTimeDifference(tw.createdAt);
    }
}
