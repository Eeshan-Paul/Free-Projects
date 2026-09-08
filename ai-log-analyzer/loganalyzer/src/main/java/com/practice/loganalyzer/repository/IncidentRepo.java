package com.practice.loganalyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.loganalyzer.entity.Incident;

public interface IncidentRepo extends JpaRepository<Incident,Long>{

}
