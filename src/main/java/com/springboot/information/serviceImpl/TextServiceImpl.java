package com.springboot.information.serviceImpl;

import com.mongodb.client.gridfs.GridFSBucket;
//import com.mongodb.util.JSON;
import com.springboot.information.entity.Text;
import com.springboot.information.entity.Twitter;
import com.springboot.information.entity.tweet_list;
import com.springboot.information.service.TextService;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
//import org.codehaus.jettison.json.JSONArray;
//import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

/**
 * Created by yww on 2019/3/29.
 */
//@Component
@Service
public class TextServiceImpl implements TextService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoDbFactory mongodbfactory;

    @Autowired
    ApplicationContext applicationContext;

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
            event.put("desc",texts.get(i).getSummary().get("desc"));
            event.put("location",texts.get(i).getSummary().get("airport"));
            events.add(event);
        }
        resultMap.put("event",events);
        return resultMap;
    }

    @Override
    public Map getEventsByID(Map params) {
        String id=(String)params.get("id");
        System.out.println(id);
        Map<String,Object> resultMap=new LinkedHashMap<String,Object>();
        Text text = mongoTemplate.findById(id,Text.class,"text");
        //返回一个Bbox的list；
        List<Object> GeoList = (List) text.getGeo_infer();
        List<Map> bboxs = new ArrayList<>();
        List<Object> airport = new ArrayList<>();

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
                    Aggregation.project("tweet_list.timestamp_ms","tweet_list.text","tweet_list.created_at","tweet_list.user")

            );
            AggregationResults<JSONObject> results = mongoTemplate.aggregate(agg, "text", JSONObject.class);
            //System.out.println("results"+results.getRawResults()); //获取到的结果是document
            //String res = results.getRawResults();
            String json = com.mongodb.util.JSON.serialize(results.getRawResults());
            System.out.println("JSON serialized Document: " + json);
            JSONObject jso= JSON.parseObject(json);
            JSONArray resultss=jso.getJSONArray("results");
            System.out.println(resultss);
            for(int j = 0;j<resultss.size();j++){
                Map tweet_one = (Map) resultss.get(j);
                System.out.println(tweet_one);
                Map map=new HashMap();
                map.put("_id",id);
                map.put("timestamp_ms",tweet_one.get("timestamp_ms"));
                map.put("text",tweet_one.get("text"));
                map.put("created_at",tweet_one.get("created_at"));

                Map user = (Map) tweet_one.get("user");
                System.out.println(tweet_one.get("user"));
                map.put("user_id",user.get("id"));
                if(tweet_one.get("place")!=null){
                    map.put("place",tweet_one.get("place"));

                }else{
                    map.put("place","null");
                }
                if(user.get("time_zone")!= null){
                    map.put("location",user.get("time_zone"));
                }else{
                    map.put("location","null");
                }



                tweets.add(map);
                if(j>=9)
                    break;
            }
            //存储到本地文件 json格式 JSONArray
            creatFile(id,resultss);
            resultMap.put("urlFlag","http://localhost:8080/information/text/download/"+id+".txt");



        }else {
            System.out.println("此事件的geoList的size为0！");
        }

        //airport
        Map airportName = new HashMap();
        airportName.put("airport",text.getSummary().get("airport"));
        airport.add(airportName);



        //getPic();
        List<Map> picName = new ArrayList<Map>();
        List<Map> picList= (List)text.getSummary().get("picname");
        if(picList!=null){
            System.out.println(picList.size());
            for(int t = 0;t<picList.size();t++){
                System.out.println(picList.get(t));
                picName.add(picList.get(t));
            }

        }
        airport.add(picName);

        //getairportaddr
        Map airportaddr = new HashMap();
        List<Map> addrlonandlat= (List)text.getSummary().get("airportaddr");
        List<Map> addrlonandlatres = new ArrayList<Map>();
        //判空
        if(addrlonandlat!=null||addrlonandlat.size()!=0){
            addrlonandlatres.add(addrlonandlat.get(1));
            addrlonandlatres.add(addrlonandlat.get(0));
        }
        airportaddr.put("airportaddr",addrlonandlatres);
        airport.add(airportaddr);


        resultMap.put("tweets_list",tweets);
        resultMap.put("bboxs",bboxs);
        resultMap.put("airport",airport);

        return resultMap;
    }

    @Override
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        //String id=(String)params.get("id");
        File file = new File("/home/hzhao/IdeaProjects/information/src/main/resources/public/", id + ".txt");
        try(InputStream inputStream =  new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();){
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename="+id+".txt");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void runmodels() {
        //String url="http://59.78.194.14:10010/test";
        String url="http://127.0.0.1:10010/test";
        String result = "";
        try{
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection conn =  realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //获取URLConnection对象对应的输出流
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            //flush输出流的缓冲
            out.flush();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
            //creatFiletowenben(""+new Date().getTime(),result);

        } catch (Exception e) {
            System.out.println("发送POST请求出现异常" + e);
            e.printStackTrace();
        }finally {
            System.out.println(result);
        }
        System.out.println(result);
        System.out.println(""+new Date().getTime());

    }

    @Override
    public void runPicmodels() {
       try {
            String shpath = "/home/hzhao/project_bj";
           String[] params = new String[] { "/home/hzhao/anaconda3/envs/zh_py35/bin/python", "/home/hzhao/project_bj/detection_pub/__main__.pyc"};
           Process ps=Runtime.getRuntime().exec(params);
           ps.waitFor();

           BufferedReader bufrIn = new BufferedReader(new InputStreamReader(ps.getInputStream(), "UTF-8"));
           BufferedReader bufrError = new BufferedReader(new InputStreamReader(ps.getErrorStream(), "UTF-8"));

            // 读取输出
           StringBuilder result = new StringBuilder();
           String line = null;
           while ((line = bufrIn.readLine()) != null || (line = bufrError.readLine()) != null) {
               result.append(line).append('\n');
           }

           System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runEventmodels() {
        String shpath = "/home/hzhao/project_bj";

        String url="http://59.78.194.115:10010/test";
        String result = "";
        try{
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection conn =  realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            //flush输出流的缓冲
            out.flush();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
            //System.out.println(""+new Date().getTime());
            creatFiletowenben(""+new Date().getTime(),result);
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常" + e);
            e.printStackTrace();
        }finally {
            System.out.println(result);
        }
        System.out.println(result+"tag::");
        System.out.println(""+new Date().getTime());

    }

    @Override
    public Map tweetAnalysis(Map<String, Object> params) {
        // count
        Map res = new HashMap();
        String id=(String)params.get("id");
        Text text = mongoTemplate.findById(id,Text.class,"text");
        List<Object> tw = text.getTweet_list();

        if(tw.size()!= 0){
            Aggregation agg = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("_id").is(id)),
                    Aggregation.unwind("tweet_list"),
                    Aggregation.sort(Sort.Direction.ASC,"tweet_list.timestamp_ms"),
                    Aggregation.project("tweet_list.timestamp_ms","tweet_list.text","tweet_list.created_at","tweet_list.user")

            );
            AggregationResults<JSONObject> results = mongoTemplate.aggregate(agg, "text", JSONObject.class);
            //System.out.println("results"+results.getRawResults()); //获取到的结果是document
            //String res = results.getRawResults();
            String json = com.mongodb.util.JSON.serialize(results.getRawResults());
            System.out.println("JSON serialized Document: " + json);
            JSONObject jso= JSON.parseObject(json);
            JSONArray resultss=jso.getJSONArray("results");
            System.out.println(resultss);
            res.put("tweets_num",tw.size());
        }else{
            res.put("tweets_num",0);
        }
        return res;
    }

    private void creatFiletowenben(String id, String resultss) {
        //创建一个文件，名字就是id；
        String filePath = "/home/hzhao/QB/output/";
        File dir = new File(filePath); // 一、检查放置文件的文件夹路径是否存在，不存在则创建
        if (!dir.exists()) {
            dir.mkdirs();// mkdirs创建多级目录
        }
        File checkFile = new File(filePath + id+".json");
        FileWriter writer = null; try { // 二、检查目标文件是否存在，不存在则创建
            if (!checkFile.exists()) {
                checkFile.createNewFile();// 创建目标文件
            } // 三、向目标文件中写入内容 //
            writer = new FileWriter(checkFile, false);
            writer.write("{\"key\":"+resultss+"}");
            System.out.println("test:"+resultss);
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

    private void creatFile(String id, JSONArray resultss) {
        //创建一个文件，名字就是id；
        String filePath = "./src/main/resources/public/";
        File dir = new File(filePath); // 一、检查放置文件的文件夹路径是否存在，不存在则创建
        if (!dir.exists()) {
            dir.mkdirs();// mkdirs创建多级目录
        }
        File checkFile = new File(filePath + id+".txt");
        FileWriter writer = null; try { // 二、检查目标文件是否存在，不存在则创建
            if (!checkFile.exists()) {
                checkFile.createNewFile();// 创建目标文件
            } // 三、向目标文件中写入内容 //
            writer = new FileWriter(checkFile, false);
            writer.write("{\"key\":"+resultss+"}");
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
