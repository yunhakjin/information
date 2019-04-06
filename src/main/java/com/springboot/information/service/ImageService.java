package com.springboot.information.service;

import com.springboot.information.entity.Image;
import com.springboot.information.entity.ImageOutPut;

import java.util.List;

/**
 * Created by yww on 2019/4/1.
 */
public interface ImageService {
    List<Image> getImage();

    List<ImageOutPut> getImageOutput();
}
