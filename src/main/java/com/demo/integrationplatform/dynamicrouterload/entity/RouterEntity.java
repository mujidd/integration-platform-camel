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
@Table(name = "router")
public class RouterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="router_name")
    private String routerName;
    @Column(name="router_flow")
    private String routerFlow;
    @Column(name="timestamp")
    private String timestamp;
}
