package com.springboot.information.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.springboot.information.entity.Image;
import com.springboot.information.entity.ImageOutPut;
import com.springboot.information.entity.Twitter;
import com.springboot.information.service.ImageService;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yww on 2019/4/1.
 */

@Component
public class ImageServiceImpl implements ImageService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;


    @Override
    public List<Image> getImage() {
        String path="/home/hzhao/IdeaProjects/outTestpic";
        File file = new File(path);
        File[] files=file.listFiles();
        List<Image> wjList = new ArrayList<Image>();//新建一个文件集合
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {//判断是否为文件
                Image image=new Image();
                image.setImageID(System.currentTimeMillis()+files[i].getName());
                System.out.println(System.currentTimeMillis()+files[i].getName());
                image.setImagePath(files[i].getPath());
                //twitter.setContext(txt2String(files[i]));
                wjList.add(image);
                try {
                    storeToGridFs(files[i],files[i].getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
        return wjList;

    }

    @Override
    public List<ImageOutPut> getImageOutput() {
        String path="/home/hzhao/IdeaProjects/outputPic";
        File file = new File(path);
        File[] files=file.listFiles();
        List<ImageOutPut> wjList = new ArrayList<ImageOutPut>();//新建一个文件集合
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {//判断是否为文件
                //直接通过json存入MongoDB
                System.out.println(files.length);
                Document document = Document.parse(txt2String(files[i]));
                mongoTemplate.insert(document,"imageoutput");



            }
        }
        return wjList;
    }


    @Test
    public void storeToGridFs(File file,String name) throws FileNotFoundException {
        //定义输入流
        FileInputStream inputStram = new FileInputStream(file);
        //向GridFS存储文件
        ObjectId objectId =  gridFsTemplate.store(inputStram, name, "");
        //得到文件ID
        String fileId = objectId.toString();
        System.out.println(file);
        System.out.println(fileId);
    }
    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }







}
