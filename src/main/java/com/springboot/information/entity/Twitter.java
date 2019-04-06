package com.springboot.information.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by yww on 2019/3/30.
 */
@Document(collection="twitter")
public class Twitter implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private String textID;
    private String textPath;
    private String context;

    @Override
    public String toString() {
        return "Twitter{" +
                "textID=" + textID +
                ", textPath='" + textPath + '\'' +
                ", context='" + context + '\'' +
                '}';
    }

    public String getTextID() {
        return textID;
    }

    public void setTextID(String textID) {
        this.textID = textID;
    }

    public String getTextPath() {
        return textPath;
    }

    public void setTextPath(String textPath) {
        this.textPath = textPath;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
