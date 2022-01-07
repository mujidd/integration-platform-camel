package com.demo.integrationplatform.dynamicrouterload.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouterRepository extends JpaRepository<RouterEntity, Long> {
    List<RouterEntity> findAllByStatus(String status);
}
