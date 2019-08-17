package com.springboot.information.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GetHTMLController {
    @GetMapping("/index")
    public String helloHtml() {
        return "index";
    }
}
