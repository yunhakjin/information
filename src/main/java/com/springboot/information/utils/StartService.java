package com.springboot.information.utils;

import com.springboot.information.controller.TextController;
import com.springboot.information.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class StartService implements ApplicationRunner {

    @Autowired
    TextService textService;

    @Autowired
    TextController textController;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        textController.runmodels();
    }

}