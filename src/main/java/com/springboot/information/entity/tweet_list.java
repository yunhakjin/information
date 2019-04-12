package com.springboot.information.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

public class tweet_list implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private String timestamp_ms;
    private String id;
    private String in_reply_to_status_id;
    private String favorite_count;
    private String truncated;
    private String place;
    private String orgn;
    private String is_quote_status;
    private String created_at;
    private String text;
    private List<Object> entities;
    private List<Object> user;
    private String in_reply_to_user_id;
    private String retweet_count;
    private String filter_level;
    private String source;

    @Override
    public String toString() {
        return "tweet_list{" +
                "timestamp_ms='" + timestamp_ms + '\'' +
                ", id='" + id + '\'' +
                ", in_reply_to_status_id='" + in_reply_to_status_id + '\'' +
                ", favorite_count='" + favorite_count + '\'' +
                ", truncated='" + truncated + '\'' +
                ", place='" + place + '\'' +
                ", orgn='" + orgn + '\'' +
                ", is_quote_status='" + is_quote_status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", text='" + text + '\'' +
                ", entities=" + entities +
                ", user=" + user +
                ", in_reply_to_user_id='" + in_reply_to_user_id + '\'' +
                ", retweet_count='" + retweet_count + '\'' +
                ", filter_level='" + filter_level + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    public String getTimestamp_ms() {
        return timestamp_ms;
    }

    public void setTimestamp_ms(String timestamp_ms) {
        this.timestamp_ms = timestamp_ms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
