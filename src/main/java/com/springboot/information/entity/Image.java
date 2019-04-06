package com.springboot.information.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by yww on 2019/4/1.
 */
@Document(collection="image")
public class Image implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private String imageID;
    private String imageName;
    private String imagePath;

    @Override
    public String toString() {
        return "Image{" +
                "imageID='" + imageID + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
