package com.demo.integrationplatform.dynamicrouterload.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JsSchemaRepository extends JpaRepository<JsSchemaEntity, Long> {
    List<JsSchemaEntity> findBySchemaTitle(String title);
}
