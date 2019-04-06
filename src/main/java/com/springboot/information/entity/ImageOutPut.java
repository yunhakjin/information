package com.springboot.information.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by yww on 2019/3/30.
 */
@Document(collection="imageoutput")
public class ImageOutPut  implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private String imageoutputID;
    private String category_id;
    private Object score;
    private Object bbox;

    @Override
    public String toString() {
        return "ImageOutPut{" +
                "imageoutputID='" + imageoutputID + '\'' +
                ", category_id='" + category_id + '\'' +
                ", score=" + score +
                ", bbox=" + bbox +
                '}';
    }

    public String getImageoutputID() {
        return imageoutputID;
    }

    public void setImageoutputID(String imageoutputID) {
        this.imageoutputID = imageoutputID;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public Object getBbox() {
        return bbox;
    }

    public void setBbox(Object bbox) {
        this.bbox = bbox;
    }

    public Object getScore() {
        return score;
    }

    public void setScore(Object score) {
        this.score = score;
    }
}
