package com.springboot.information.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user")
public class User {
    private int userid;
    private String name;
    private int age;

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
