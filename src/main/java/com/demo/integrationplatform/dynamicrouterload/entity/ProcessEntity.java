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
@Table(name = "process")
public class ProcessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="process_name")
    private String processName;
    @Column(name="process_flow")
    private String processFlow;
    @Column(name="status")
    private String status;
    @Column(name="create_time")
    private String createTime;
}
