package com.springboot.information.dao;

import com.springboot.information.entity.ImageOutPut;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by yww on 2019/4/1.
 */
@Component
@Repository
public interface ImageDao extends MongoRepository<ImageOutPut,Integer> {
}
