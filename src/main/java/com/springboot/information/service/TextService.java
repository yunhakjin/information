package com.springboot.information.service;

import com.springboot.information.entity.Text;
import com.springboot.information.entity.Twitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by yww on 2019/3/29.
 */
public interface TextService {

    public void saveUser(Text text);

    public List<Text> findAll();

    public void deleteTextById(Long id);

    List<Twitter> getTwitter();

    List<Text> getText();

    List<Map> getTimeAndLocation();

    List<Twitter> getTwitterListFromGridFS();

    Map getTextAndImageJson(Map params);

    Map getAllEvents();

    Map getEventsByID(Map params);

    void download(String id, HttpServletRequest request, HttpServletResponse response);

    void runmodels();

    void runPicmodels();

    void runEventmodels();
}
