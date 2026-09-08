package com.practice.loganalyzer.dto;

import lombok.Data;

@Data 
public class IncidentRequest {

    private Long id;
    private String serviceName;
    private String environment;
    private String logs;
}
