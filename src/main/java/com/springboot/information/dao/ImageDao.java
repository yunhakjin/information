package com.springboot.information.dao;

import com.springboot.information.entity.ImageOutPut;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by yww on 2019/4/1.
 */
public interface ImageDao extends MongoRepository<ImageOutPut,Integer> {
}
