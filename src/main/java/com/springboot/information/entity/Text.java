package com.springboot.information.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yww on 2019/3/29.
 */
@Document(collection="text")
public class Text implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private String textID;
    private Object summary;
    private ArrayList<Object> geo_infer;
    private Object time_infer;
    private ArrayList<Object> tweet_list;

    public String getTextID() {
        return textID;
    }

    public void setTextID(String textID) {
        this.textID = textID;
    }

    public Object getSummary() {
        return summary;
    }

    public void setSummary(Object summary) {
        this.summary = summary;
    }

    public ArrayList<Object> getGeo_infer() {
        return geo_infer;
    }

    public void setGeo_infer(ArrayList<Object> geo_infer) {
        this.geo_infer = geo_infer;
    }

    public ArrayList<Object> getTweet_list() {
        return tweet_list;
    }

    public void setTweet_list(ArrayList<Object> tweet_list) {
        this.tweet_list = tweet_list;
    }

    public Object getTime_infer() {
        return time_infer;
    }

    public void setTime_infer(Object time_infer) {
        this.time_infer = time_infer;
    }

    @Override
    public String toString() {
        return "Text{" +
                "textID=" + textID +
                ", summary=" + summary +
                ", geo_infer=" + geo_infer +
                ", time_infer=" + time_infer +
                ", tweet_list=" + tweet_list +
                '}';
    }
}
