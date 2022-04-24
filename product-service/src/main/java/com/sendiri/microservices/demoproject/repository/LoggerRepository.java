package com.sendiri.microservices.demoproject.repository;

import com.sendiri.microservices.demoproject.model.LoggerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggerRepository extends JpaRepository<LoggerEntity, Long> {
}
