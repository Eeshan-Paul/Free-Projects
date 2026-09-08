package com.practice.loganalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.loganalyzer.dto.GenericResponse;
import com.practice.loganalyzer.dto.IncidentRequest;
import com.practice.loganalyzer.dto.IncidentResponse;
import com.practice.loganalyzer.service.IncidentAnalyserService;

@RestController 
@RequestMapping("/log-analyze")
public class IncidentController {

    private final IncidentAnalyserService incidentAnalyserService;

    public IncidentController(IncidentAnalyserService incidentAnalyserService)
    {
        this.incidentAnalyserService = incidentAnalyserService;
    }

    @PostMapping("/saveIncident")
    public ResponseEntity<?> saveIncident(@RequestBody IncidentRequest incidentRequest)
    {
        GenericResponse<Object> genericResponse = new GenericResponse<>();
        IncidentResponse incidentResponse = incidentAnalyserService.saveAndGetIncident(incidentRequest);

        if (incidentResponse!=null)
        {
            genericResponse.setSuccess(true);
            genericResponse.setMessage("Data saved and retrived sucessfully");
            genericResponse.setData(incidentResponse);
        }
        else 
        {
            genericResponse.setSuccess(false);
            genericResponse.setMessage("Failed");
        }
        return ResponseEntity.ok(genericResponse);
    }

}
