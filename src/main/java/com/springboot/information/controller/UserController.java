package com.springboot.information.controller;

import com.springboot.information.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping("/list")
    public List<User> getAll(){
        return mongoTemplate.findAll(User.class);
    }
}
