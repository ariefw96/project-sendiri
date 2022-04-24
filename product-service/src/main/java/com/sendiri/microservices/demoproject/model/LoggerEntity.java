package com.sendiri.microservices.demoproject.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="logger")
public class LoggerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name ="action")
    private String action;
    @Column(name ="log_json")
    private String log_json;
}
