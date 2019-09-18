package com.springboot.information.controller;

/**
 * Created by yww on 2019/3/29.
 */
import com.springboot.information.entity.Text;
import com.springboot.information.entity.Twitter;
import com.springboot.information.service.TextService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/text")
@Api("事件类相关api")
public class TextController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TextService textService;

//    @RequestMapping("/list")
//    public List<Text> getAll(){
//        return mongoTemplate.findAll(Text.class);
//    }

    @ApiOperation(value = "listALL",notes = "listALL")
    @ApiImplicitParam(name = "params",value="",dataType = "JSON")
    @RequestMapping(value = "/listText",method = RequestMethod.GET)
    public List<Text> getAll(){
        return textService.findAll();
    }


   /* @ApiOperation(value = "推特获取",notes = "推特获取")
    @ApiImplicitParam(name = "params",value="",dataType = "JSON")
    @RequestMapping(value = "/getTwitter",method = RequestMethod.GET)
    public List<Twitter> getTwitter(){
        System.out.println(textService.getTwitter());
        return textService.getTwitter();
    }


    @ApiOperation(value = "文本获取",notes = "文本获取")
    @ApiImplicitParam(name = "params",value="",dataType = "JSON")
    @RequestMapping(value = "/getText",method = RequestMethod.GET)
    public List<Text> getText(){
        return textService.getText();
    }*/


    @ApiOperation(value = "时间和地点",notes = "时间和地点")
    @ApiImplicitParam(name = "params",value="",dataType = "JSON")
    @RequestMapping(value = "/getTimeAndLocation",method = RequestMethod.GET)
    public List<Map> getTimeAndLocation(){
        return textService.getTimeAndLocation();
    }

    @ApiOperation(value = "从gridfs中获取",notes = "从gridfs中获取")
    @RequestMapping(value = "/getTwitterListFromGridFS",method = RequestMethod.GET)
    public List<Twitter> getTwitterListFromGridFS(){
        return textService.getTwitterListFromGridFS();
    }


    @ApiOperation(value = "事件获取",notes = "事件获取")
    @ApiImplicitParam(name = "params",value="事件和地点",dataType = "JSON")
    @RequestMapping(value = "/getTextAndImageJson",method = RequestMethod.POST)
    public Map getTextAndImageJson(@RequestBody Map<String,Object> params){
        return textService.getTextAndImageJson(params);
    }


    @ApiOperation(value = "所有事件获取",notes = "所有事件获取")
    @RequestMapping(value = "/getAllEvents",method = RequestMethod.GET)
    public Map getAllEvents(){
        return textService.getAllEvents();
    }


    @ApiOperation(value = "文本模型运行",notes = "运行模型")
    @RequestMapping(value = "/runtwomodels",method = RequestMethod.GET)
    public void runmodels(){
        /*textService.runEventmodels();
        textService.runmodels();*/
        System.out.println("run models success....");
    }


    @ApiOperation(value = "影像模型运行",notes = "运行模型")
    @RequestMapping(value = "/runmodels",method = RequestMethod.GET)
    public void runPicmodels(){
        ///textService.runPicmodels();
    }

    @ApiOperation(value = "推特模型运行",notes = "运行模型")
    @RequestMapping(value = "/runEventmodels",method = RequestMethod.GET)
    public void runEventmodels(){
        textService.runEventmodels();
    }

    @ApiOperation(value = "单个事件获取",notes = "单个事件获取")
    @ApiImplicitParam(name = "params",value="事件id",dataType = "JSON")
    @RequestMapping(value = "/getEventsByID",method = RequestMethod.POST)
    public Map getEventsByID(@RequestBody Map<String,Object> params){
        return textService.getEventsByID(params);
    }

    @ApiOperation(value = "根据ID获取参考推特",notes = "获取推特by id")
    @ApiImplicitParam(name = "params",value="事件id",dataType = "JSON")
    @RequestMapping(value = "/tweetAnalysis",method = RequestMethod.POST)
    public Map tweetAnalysis(@RequestBody Map<String,Object> params){
        return textService.tweetAnalysis(params);
    }


    @ApiOperation(value = "download",notes = "download")
    //@ApiImplicitParam(name = "params",value="事件id",dataType = "JSON")
    @GetMapping("/download/{id}")
    //@RequestMapping(value = "/download",method = RequestMethod.GET)
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {

        textService.download(id,request, response);

    }

}
