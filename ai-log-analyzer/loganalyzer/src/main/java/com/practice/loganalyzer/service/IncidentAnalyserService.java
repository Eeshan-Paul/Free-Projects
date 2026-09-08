package com.practice.loganalyzer.service;

import com.practice.loganalyzer.dto.IncidentRequest;
import com.practice.loganalyzer.dto.IncidentResponse;

public interface IncidentAnalyserService {

    public IncidentResponse saveAndGetIncident(IncidentRequest incident);
    
}
