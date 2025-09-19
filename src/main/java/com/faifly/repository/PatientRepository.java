package com.faifly.repository;

import com.faifly.model.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Query(value = """
        SELECT DISTINCT p.*
        FROM patient p
        LEFT JOIN visit v ON v.patient_id = p.id
        WHERE (:search IS NULL OR LOWER(p.first_name) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(p.last_name) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:doctorIds IS NULL OR v.doctor_id IN :doctorIds)
        """,
            countQuery = """
        SELECT COUNT(DISTINCT p.id)
        FROM patient p
        LEFT JOIN visit v ON v.patient_id = p.id
        WHERE (:search IS NULL OR LOWER(p.first_name) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(p.last_name) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:doctorIds IS NULL OR v.doctor_id IN :doctorIds)
        """,
            nativeQuery = true)
    Page<Patient> findPatientsWithFilters(
            @Param("search") String search,
            @Param("doctorIds") List<Integer> doctorIds,
            Pageable pageable
    );
}
