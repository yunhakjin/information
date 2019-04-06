package com.springboot.information.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by yww on 2019/3/29.
 */
public interface GridfsService {

    public GridFSInputFile save(MultipartFile file);

    public GridFSDBFile getById(ObjectId id);

    public void remove(String id);

}
