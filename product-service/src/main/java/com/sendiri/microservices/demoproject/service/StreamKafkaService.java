package com.sendiri.microservices.demoproject.service;

import com.sendiri.microservices.demoproject.model.LoggerEntity;
import com.sendiri.microservices.demoproject.repository.LoggerRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@EnableKafka
@Configuration
public class StreamKafkaService {
    Logger logger = LoggerFactory.getLogger(StreamKafkaService.class);

    @Autowired
    LoggerRepository loggerRepository;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String boostrapSv;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                boostrapSv);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "my-group");
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @KafkaListener(topics = "add-product", groupId = "my-group")
    public void ListenAdd(String message) {
        logger.info("Received Message : " + message);
        LoggerEntity le = new LoggerEntity();
        le.setAction("menambah produk");
        le.setLog_json(message);

        loggerRepository.save(le);
    }

    @KafkaListener(topics = "update-product", groupId = "my-group")
    public void ListenUpdate(String message) {
        logger.info("Received Message : " + message);
        LoggerEntity le = new LoggerEntity();
        le.setAction("mengupdate produk");
        le.setLog_json(message);

        loggerRepository.save(le);
    }

    @KafkaListener(topics = "delete-product", groupId = "my-group")
    public void ListenDelete(String message) {
        logger.info("Received Message : " + message);
        LoggerEntity le = new LoggerEntity();
        le.setAction("menghapus produk");
        le.setLog_json(message);

        loggerRepository.save(le);
    }

}
