package com.demo.integrationplatform.dynamicrouterload.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "js_schema")
public class JsSchemaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="schema_content")
    private String schemaContent;
    @Column(name="schema_title")
    private String schemaTitle;
    @Column(name="schema_version")
    private String schemaVersion;
    @Column(name="hash_code")
    private String hashCode;
    @Column(name="create_time")
    private String createTime;
}
