package com.sendiri.microservices.demoproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class defaultcontroller {


    @GetMapping("")
    public String helloWorld(){
        return "Hello World!";
    }
}
