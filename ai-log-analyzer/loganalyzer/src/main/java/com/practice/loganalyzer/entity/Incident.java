package com.practice.loganalyzer.entity;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Data 
@Table(name = "to_incident")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "service_name" ,length = 50)
    private String serviceName;
    private String environment;
    private String logs;
    @CreationTimestamp
    @Column (name = "created_on",nullable = false, updatable = false)
    private Date createdDate;
    private String status;
    private String cause;
    private String severity;
    private List<String> resolutionSteps;
}
