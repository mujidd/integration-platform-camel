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
@Table(name = "js_spec")
public class JsSpectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="jolt_spec")
    private String joltSpec;
    @Column(name="ep_in_dto")
    private String epInDto;
    @Column(name="ep_out_dto")
    private String epOutDto;
    @Column(name="timestamp")
    private String timestamp;
}
