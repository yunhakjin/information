package com.springboot.information.service.serviceImpl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;
import com.springboot.information.entity.Text;
import com.springboot.information.entity.Twitter;
import com.springboot.information.entity.tweet_list;
import com.springboot.information.service.TextService;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

/**
 * Created by yww on 2019/3/29.
 */
@Component
public class TextServiceImpl implements TextService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoDbFactory mongodbfactory;


    GridFSBucket gridFSBucket;


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

    @Override
    public Map getTextAndImageJson(Map params)  {
        String date=(String)params.get("date");
        String target=(String)params.get("target");
        Map<String,Object> resultMap=new LinkedHashMap<String,Object>();
        List<Map> event = new ArrayList<>();
        //获取所有事件，事件的length，遍历所有事件，包装event
        List<Text> texts=mongoTemplate.findAll(Text.class);
        for (int i =0;i<texts.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("id",texts.get(i).getTextID());
            List<Object> box = new ArrayList<>();
            if(date.equals((texts.get(i).getTime_infer().get("most_possible_time")).toString().substring(0,10))){
                map.put("event_id",texts.get(i).getTextID());
                map.put("event_level",texts.get(i).getSummary().get("level"));
                //box
                List<Object> GeoList = (List) texts.get(i).getGeo_infer();
                if(GeoList.size()!=0){
                    for (int j = 0; j < GeoList.size();j++){
                        Map GeoNunber = (Map) GeoList.get(j);
                        //System.out.println("GeoNunber"+GeoNunber);
                        Map Bbox = (Map) GeoNunber.get("bbox");
                        //System.out.println("bbox"+Bbox);
                        List<Double> up = (List) Bbox.get("northeast");
                        List<Double> down = (List) Bbox.get("southwest");
                        //System.out.println(up);
                        //System.out.println(down);
                        Double up_lon2 = 0.0;
                        Double up_lat2 = 0.0;
                        Double down_lon1 = 0.0;
                        Double down_lat1 = 0.0;

                        for(int m = 0; m<up.size();m++){
                            if(m==0){
                                up_lat2 = (Double) up.get(m);
                            }else{
                                up_lon2= (Double) up.get(m);
                            }
                        }
                        for(int n = 0; n<down.size();n++){
                            if(n==0){
                                down_lat1 = (Double) down.get(n);
                            }else{
                                down_lon1 = (Double) down.get(n);
                            }
                        }
                        List<Double> P1 = new ArrayList<>();
                        List<Double> P2 = new ArrayList<>();
                        List<Double> P3 = new ArrayList<>();
                        List<Double> P4 = new ArrayList<>();
                        P1.add(down_lon1);
                        P1.add(up_lat2);
                        P2.add(up_lon2);
                        P2.add(up_lat2);
                        P3.add(up_lon2);
                        P3.add(down_lat1);
                        P4.add(down_lon1);
                        P4.add(down_lat1);

                        box.add(P1);
                        box.add(P2);
                        box.add(P3);
                        box.add(P4);
                        box.add(P1);

                        map.put("box",box);
                        break;
                    }

                }else{
                    map.put("box",null);
                }

                //imgs
                List<Map> imgs = new ArrayList<>();
                Map img = new HashMap();
                img.put("time",date);
                img.put("location",target);
                List<String> gridfsID = readFilesFromGridFs();
                img.put("img_name",gridfsID);
                imgs.add(img);
                map.put("imgs",imgs);

                //msgs
                List<Map> tweetAll = (List) texts.get(i).getTweet_list();
                List<Map> tweet_list = new ArrayList<>();
                if(tweetAll.size()>= 5){
                    for(int t=0;t<5;t++){
                        Map tweet = tweetAll.get(t);
                        Map tweetOut = new HashMap();
                        tweetOut.put("time",tweet.get("created_at"));
                        tweetOut.put("text",tweet.get("text"));
                        tweetOut.put("place",tweet.get("place"));
                        tweet_list.add(tweetOut);
                    }
                }else{
                    for(int t=0;t<tweetAll.size();t++){
                        Map tweet = tweetAll.get(t);
                        Map tweetOut = new HashMap();
                        tweetOut.put("time",tweet.get("created_at"));
                        tweetOut.put("text",tweet.get("text"));
                        tweetOut.put("place",tweet.get("place"));
                        tweet_list.add(tweetOut);
                    }
                }
                map.put("msgs",tweet_list);

                //content
                Map<String,Object> mapContent = new HashMap<>();
                mapContent.put("time",date);
                mapContent.put("location",target);
                List<String> keywordList = (List)texts.get(i).getSummary().get("keywords");
                mapContent.put("keyword",keywordList);
                map.put("content",mapContent);

                event.add(map);
            }
        }

        resultMap.put("events_count",event.size());
        resultMap.put("event",event);
        return resultMap;
    }

    @Override
    public Map getAllEvents() {
        Map<String,Object> resultMap=new LinkedHashMap<String,Object>();
        List<Map> events = new ArrayList<>();
        //获取所有事件，事件的length，遍历所有事件，包装event
        List<Text> texts=mongoTemplate.findAll(Text.class);
        for(int i =0;i<texts.size();i++){
            Map event = new HashMap();
            event.put("event_id",texts.get(i).getTextID());
            event.put("event_level",texts.get(i).getSummary().get("level"));
            event.put("most_possible_time",texts.get(i).getTime_infer().get("most_possible_time"));
            List<String> keywordList = (List)texts.get(i).getSummary().get("keywords");
            event.put("keywords",keywordList);
            events.add(event);
        }
        resultMap.put("event",events);
        return resultMap;
    }

    @Override
    public Map getEventsByID(Map params) {
        String id=(String)params.get("id");
        Map<String,Object> resultMap=new LinkedHashMap<String,Object>();
        Text text = mongoTemplate.findById(id,Text.class,"text");
        //返回一个Bbox的list；
        List<Object> GeoList = (List) text.getGeo_infer();
        List<Map> bboxs = new ArrayList<>();

        if(text.getGeo_infer().size()!=0){
            for (int j = 0; j < GeoList.size();j++){
                List<Object> box = new ArrayList<>();
                Map bboxList=new HashMap();//bbox

                Map GeoNunber = (Map) GeoList.get(j);
                Map Bbox = (Map) GeoNunber.get("bbox");

                List<Double> up = (List) Bbox.get("northeast");
                List<Double> down = (List) Bbox.get("southwest");

                bboxList.put("address",GeoNunber.get("address"));
                bboxList.put("country",GeoNunber.get("country"));
                bboxList.put("freq",GeoNunber.get("freq"));
                Double up_lon2 = 0.0;
                Double up_lat2 = 0.0;
                Double down_lon1 = 0.0;
                Double down_lat1 = 0.0;
                for(int m = 0; m<up.size();m++){
                    if(m==0){
                        up_lat2 = (Double) up.get(m);
                    }else{
                        up_lon2= (Double) up.get(m);
                    }
                }
                for(int n = 0; n<down.size();n++){
                    if(n==0){
                        down_lat1 = (Double) down.get(n);
                    }else{
                        down_lon1 = (Double) down.get(n);
                    }
                }
                List<Double> P1 = new ArrayList<>();
                List<Double> P2 = new ArrayList<>();
                List<Double> P3 = new ArrayList<>();
                List<Double> P4 = new ArrayList<>();
                P1.add(down_lon1);
                P1.add(up_lat2);
                P2.add(up_lon2);
                P2.add(up_lat2);
                P3.add(up_lon2);
                P3.add(down_lat1);
                P4.add(down_lon1);
                P4.add(down_lat1);

                box.add(P1);
                box.add(P2);
                box.add(P3);
                box.add(P4);
                box.add(P1);
                bboxList.put("bbox",box);

                bboxs.add(bboxList);
            }

        }else {
                bboxs.add(null);
        }

        //一个tweet的list--排序规则

        List<Map> tweets = new ArrayList<>();

        List<Map> tweetList = (List)text.getTweet_list();
        if(tweetList.size()!=0){
            Aggregation agg = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("_id").is(id)),
                    Aggregation.unwind("tweet_list"),
                    Aggregation.sort(Sort.Direction.ASC,"tweet_list.timestamp_ms"),
                    Aggregation.project("tweet_list.timestamp_ms","tweet_list.text","tweet_list.created_at")

            );
            AggregationResults<JSONObject> results = mongoTemplate.aggregate(agg, "text", JSONObject.class);
            System.out.println("results"+results.getRawResults()); //获取到的结果是document
            List<JSONObject> mappedResults = results.getMappedResults();
            System.out.println("mappedResults:"+mappedResults);

            if(text.getTweet_list().size()<10){
                //list 所有的tweet按照顺序

            }else{
                //list前十条，按照时间的先后顺序。
            }

            //对获得的推特列表存入文本
            for (int i = 0;i<tweetList.size();i++){

//                creatFile(id);
//
//
//                try{
//                    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(""),true));
//                    writer.write("\n"+"");
//                    writer.close();
//                }catch(Exception e){
//                    e.printStackTrace();
//                }

            }
        }else {
            System.out.println("此事件的geoList的size为0！");
        }
        resultMap.put("bboxs",bboxs);
        return resultMap;
    }

    private void creatFile(String id) {
        //创建一个文件，名字就是id；
        String filePath = "/home/hzhao/IdeaProjects/tweetList";
        File dir = new File(filePath); // 一、检查放置文件的文件夹路径是否存在，不存在则创建
        if (!dir.exists()) {
            dir.mkdirs();// mkdirs创建多级目录
        }
        File checkFile = new File(filePath + "/"+id+".txt");
        FileWriter writer = null; try { // 二、检查目标文件是否存在，不存在则创建
            if (!checkFile.exists()) {
                checkFile.createNewFile();// 创建目标文件
            } // 三、向目标文件中写入内容 //
            writer = new FileWriter(checkFile, true);
            writer.append("your content");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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





    @Test
    public List<String> readFilesFromGridFs() {
        GridFsResource[] imagefiles = gridFsTemplate.getResources("*");
        List<String> list = new ArrayList<>();
        for (GridFsResource imagefile : imagefiles) {
            System.out.println(imagefile.getId().toString().substring(19,43));
            //list.add(imagefile.getId().toString().substring(19,43));
            list.add(imagefile.getFilename());
         }
        return list;
    }


}
