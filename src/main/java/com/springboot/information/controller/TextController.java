package com.springboot.information.controller;

/**
 * Created by yww on 2019/3/29.
 */
import com.springboot.information.entity.Text;
import com.springboot.information.entity.Twitter;
import com.springboot.information.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/text")
public class TextController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TextService textService;

//    @RequestMapping("/list")
//    public List<Text> getAll(){
//        return mongoTemplate.findAll(Text.class);
//    }

    @RequestMapping("/listText")
    public List<Text> getAll(){
        return textService.findAll();
    }

    @RequestMapping("/getTwitter")
    public List<Twitter> getTwitter(){
        System.out.println(textService.getTwitter());
        return textService.getTwitter();
    }
    @RequestMapping("/getText")
    public List<Text> getText(){
        return textService.getText();
    }

    @RequestMapping("/getTimeAndLocation")
    public List<Map> getTimeAndLocation(){
        return textService.getTimeAndLocation();
    }

    @RequestMapping("/getTwitterListFromGridFS")
    public List<Twitter> getTwitterListFromGridFS(){
        return textService.getTwitterListFromGridFS();
    }

}
