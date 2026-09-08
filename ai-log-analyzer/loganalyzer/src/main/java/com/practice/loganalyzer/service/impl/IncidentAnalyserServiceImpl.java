package com.practice.loganalyzer.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.practice.loganalyzer.dto.IncidentRequest;
import com.practice.loganalyzer.dto.IncidentResponse;
import com.practice.loganalyzer.entity.Incident;
import com.practice.loganalyzer.enums.IncidentResolutionStatus;
import com.practice.loganalyzer.repository.IncidentRepo;
import com.practice.loganalyzer.service.IncidentAnalyserService;

@Service
public class IncidentAnalyserServiceImpl implements IncidentAnalyserService{

    private final IncidentRepo incidentRepo;

    public IncidentAnalyserServiceImpl(IncidentRepo incidentRepo) {
        this.incidentRepo = incidentRepo;
    }

    @Override
    public IncidentResponse saveAndGetIncident(IncidentRequest incidentrequest) {
        IncidentResponse incidentResponse = new IncidentResponse();

        Boolean isValid = getAndSetIncidentStatus(incidentrequest,incidentResponse);
        Incident incident = new Incident();
        if (isValid && incidentResponse.getIncidentId()==null)
        {
            incident.setCause(incidentResponse.getProbableCause());
            incident.setEnvironment(incidentrequest.getEnvironment());
            incident.setLogs(incidentrequest.getLogs());
            incident.setResolutionSteps(incidentResponse.getResolutionSteps());
            incident.setServiceName(incidentrequest.getServiceName());
            incident.setSeverity(incidentResponse.getSeverity());
            incident.setStatus(IncidentResolutionStatus.PENDING.name());
            incidentRepo.save(incident);
        }

        return incidentResponse;
    }

    private Boolean getAndSetIncidentStatus(IncidentRequest incidentrequest,IncidentResponse incidentResponse)
    {
        List<String> resolutionSteps = new ArrayList<>();
        if (incidentrequest.getId()!=null)
        {
            Incident incident = incidentRepo.getReferenceById(incidentrequest.getId());
            if (incident == null)
            {
                resolutionSteps.add("No incident is found.Please enter null in id if incident not created");
                incidentResponse.setResolutionSteps(resolutionSteps);
                return false;
            }
            incidentResponse.setProbableCause(incident.getCause());
            incidentResponse.setResolutionSteps(incident.getResolutionSteps());
            incidentResponse.setIncidentId(incident.getId());
            incidentResponse.setSeverity(incident.getSeverity());
        }
        else 
        {
            String logs = incidentrequest.getLogs();
            if (logs == null || logs.isBlank())
            {
                resolutionSteps.add("Logs are required to analyze an incident.");
                return false;
            }

            if (logs.contains("Connection refused"))
            {
                incidentResponse.setProbableCause("DataBase Connection");
                incidentResponse.setSeverity("High");
                resolutionSteps.add("Please check any vpn is needed to connect.");
                resolutionSteps.add("Reconnect to vpn if there is any.");
                resolutionSteps.add("See whether username and password is correct or wrong");
            }
            if (logs.contains("outofMemory"))
            {
                incidentResponse.setProbableCause("Memory Issue");
                incidentResponse.setSeverity("Medium");
                resolutionSteps.add("Please check the space that is allocated to your microservice");
                resolutionSteps.add("It is occuring due to the hash memory allocated .Please check.");
            }
            incidentResponse.setResolutionSteps(resolutionSteps);
        }
        return true;
    }

}
