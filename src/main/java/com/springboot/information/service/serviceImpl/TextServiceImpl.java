package com.springboot.information.service.serviceImpl;

import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.util.JSON;
import com.springboot.information.entity.Text;
import com.springboot.information.entity.Twitter;
import com.springboot.information.service.TextService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yww on 2019/3/29.
 */
@Component
public class TextServiceImpl implements TextService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;



    @Override
    public void saveUser(Text text) {

    }

    @Override
    public List<Text> findAll() {
        return mongoTemplate.findAll(Text.class);
    }

    @Override
    public void deleteTextById(Long id) {

    }

    @Override
    public List<Twitter> getTwitter() {
        String path="/home/hzhao/IdeaProjects/inputText";
        File file = new File(path);
        File[] files=file.listFiles();
        List<Twitter> wjList = new ArrayList<Twitter>();//新建一个文件集合
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {//判断是否为文件
                //Document document = Document.parse(txt2String(files[i]));
                mongoTemplate.insert(txt2String(files[i]),"twitter");

            }
        }
        return wjList;
    }

    @Override
    public List<Text> getText() {
        String path="/home/hzhao/IdeaProjects/outputText";
        File file = new File(path);
        File[] files=file.listFiles();
        List<Text> wjList = new ArrayList<Text>();//新建一个文件集合
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {//判断是否为文件
                //直接通过json存入MongoDB
                Document document = Document.parse(txt2String(files[i]));
                mongoTemplate.insert(document,"text");
            }
        }
        return wjList;
    }

    @Override
    public List<Map> getTimeAndLocation() {
        List<Text> texts=mongoTemplate.findAll(Text.class);
        List<Map> maps = new ArrayList<Map>();
        for (int i =0;i<texts.size();i++){
            System.out.println(texts);
            Map map = new HashMap<>();
            map.put("time",texts.get(i).getTime_infer());
            map.put("location",texts.get(i).getGeo_infer());
            map.put("tweet",texts.get(i).getTweet_list());//参考的tweet
            maps.add(map);
        }
        return maps;
    }

    @Override
    public List<Twitter> getTwitterListFromGridFS() {

        return null;
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







}
