package com.faifly.repository;

import com.faifly.model.PatientVisitProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientCustomRepository {

    Page<PatientVisitProjection> findPatientsWithVisitsAndDoctors(String search, List<Integer> doctorIds, Pageable pageable);

}
