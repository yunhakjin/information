package com.springboot.information.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by yww on 2019/3/29.
 */
@Document(collection="text")
public class Text implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private String _id;
    private Map summary;
    private ArrayList<Object> geo_infer;
    private Map time_infer;
    private ArrayList<Object> tweet_list;

    public String getTextID() {
        return _id;
    }

    public void setTextID(String _id) {
        this._id = _id;
    }

    public Map getSummary() {
        return summary;
    }

    public void setSummary(Map summary) {
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

    public Map getTime_infer() {
        return time_infer;
    }

    public void setTime_infer(Map time_infer) {
        this.time_infer = time_infer;
    }

    @Override
    public String toString() {
        return "Text{" +
                "_id=" + _id +
                ", summary=" + summary +
                ", geo_infer=" + geo_infer +
                ", time_infer=" + time_infer +
                ", tweet_list=" + tweet_list +
                '}';
    }
}
