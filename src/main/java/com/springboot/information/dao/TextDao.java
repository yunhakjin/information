package com.springboot.information.dao;

import com.springboot.information.entity.Text;
import com.springboot.information.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by yww on 2019/3/29.
 */
@Component
@Repository
public interface TextDao extends MongoRepository<Text,Integer> {





}
