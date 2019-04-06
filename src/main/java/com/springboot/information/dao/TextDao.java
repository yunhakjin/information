package com.springboot.information.dao;

import com.springboot.information.entity.Text;
import com.springboot.information.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by yww on 2019/3/29.
 */
public interface TextDao extends MongoRepository<Text,Integer> {





}
