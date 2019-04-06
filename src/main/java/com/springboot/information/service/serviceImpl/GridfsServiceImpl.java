package com.springboot.information.service.serviceImpl;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.springboot.information.service.GridfsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Created by yww on 2019/3/29.
 */
@Component
public class GridfsServiceImpl implements GridfsService{
    @Autowired
    private MongoDbFactory mongodbfactory;

    @Override
    public GridFSInputFile save(MultipartFile file) {
//        GridFS gridFS = new GridFS(mongodbfactory.getDb());
//        try{
//
//            InputStream in = file.getInputStream();
//
//            String name = file.getOriginalFilename();
//
//            GridFSInputFile gridFSInputFile = gridFS.createFile(in);
//
//            gridFSInputFile.setFilename(name);
//
//            gridFSInputFile.setContentType(file.getContentType());
//
//            gridFSInputFile.save();
//            return gridFSInputFile;
//        }
//        catch (Exception e){}

        return null;
    }

    @Override
    public GridFSDBFile getById(ObjectId id) {
//        GridFS gridFS = new GridFS(mongodbfactory.getDb());
//        return gridFS.findOne(new BasicDBObject("_id", id));
        return null;
    }

    @Override
    public void remove(String id) {
//        GridFS gridFS = new GridFS(mongodbfactory.getDb());
//        gridFS.remove(new ObjectId(id));

    }
}
