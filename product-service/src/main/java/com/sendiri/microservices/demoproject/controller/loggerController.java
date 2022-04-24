package com.sendiri.microservices.demoproject.controller;

import com.sendiri.microservices.demoproject.model.LoggerEntity;
import com.sendiri.microservices.demoproject.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/v1/")
@RestController
public class loggerController {

    @Autowired
    LoggerRepository loggerRepository;

    @GetMapping("logger")
    public List<LoggerEntity> getLog(){
        return loggerRepository.findAll();
    }

}
