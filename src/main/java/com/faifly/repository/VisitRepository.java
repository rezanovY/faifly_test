package com.faifly.repository;

import com.faifly.model.visit.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Query(value = """
        SELECT v.*
        FROM visit v
        JOIN (
            SELECT patient_id, doctor_id, MAX(start_date_time) AS max_start
            FROM visit
            WHERE patient_id IN :patientIds
            GROUP BY patient_id, doctor_id
        ) lv
        ON v.patient_id = lv.patient_id
           AND v.doctor_id = lv.doctor_id
           AND v.start_date_time = lv.max_start
        """, nativeQuery = true)
    List<Visit> findLastVisitsPerPatientPerDoctor(@Param("patientIds") List<Integer> patientIds);

    @Query("""
    SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END
    FROM Visit v
    WHERE (v.doctorId = :doctorId OR v.patientId = :patientId)
      AND (:start < v.endDateTime AND :end > v.startDateTime)
    """)
    boolean existsOverlap(@Param("doctorId") Integer doctorId,
                          @Param("patientId") Integer patientId,
                          @Param("start") Instant start,
                          @Param("end") Instant end);
}