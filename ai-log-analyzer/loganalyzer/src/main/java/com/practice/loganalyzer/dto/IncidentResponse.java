package com.practice.loganalyzer.dto;

import java.util.List;

import lombok.Data;


@Data 
public class IncidentResponse {
    private Long incidentId;
    private String severity;
    private String probableCause;
    private List<String> resolutionSteps;

    public IncidentResponse()
    {
        
    }

    public IncidentResponse(Long incidentId, String severity, List<String> resolutionSteps) {
        this.incidentId = incidentId;
        this.severity = severity;
        this.resolutionSteps = resolutionSteps;
    }

    
}
