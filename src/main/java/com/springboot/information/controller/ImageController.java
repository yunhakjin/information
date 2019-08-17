package com.springboot.information.controller;

import com.springboot.information.entity.Image;
import com.springboot.information.entity.ImageOutPut;
import com.springboot.information.entity.Twitter;
import com.springboot.information.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by yww on 2019/3/30.
 */
@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ImageService imageService;

    @RequestMapping("/getImage")
    public List<Image> getImage(){
        //return imageService.getImage();
        return null;
    }

    @RequestMapping("/getImageOutput")
    public List<ImageOutPut> getImageOutput(){
        //return imageService.getImageOutput();
        return null;
    }

    @GetMapping("/hello")
    public String helloHtml() {

        return "index";
    }
}
