package com.demo.integrationplatform.dynamicrouterload.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessRepository extends JpaRepository<ProcessEntity, Long> {
    List<ProcessEntity> findAllByStatus(String status);
}
