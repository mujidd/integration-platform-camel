package com.demo.integrationplatform.dynamicrouterload.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JsSpecRepository extends JpaRepository<JsSpecEntity, Long> {
    List<JsSpecEntity> findBySpecTitle(String title);
}
