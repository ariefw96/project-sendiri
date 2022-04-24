package com.sendiri.microservices.demoproject.service;

import com.sendiri.microservices.demoproject.model.ProductEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class KafkaService {

    Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    KafkaTemplate<String, ProductEntity> kafkaTemplate;

    public void sendKafka(String topic, ProductEntity param) throws ExecutionException, InterruptedException {
        try{
            kafkaTemplate.send(topic, param);
            logger.info("send kafka");
        }catch (Exception ex){
            logger.error(ex.getLocalizedMessage());
        }
    }


}
