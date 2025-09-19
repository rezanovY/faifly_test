package com.faifly.repository;

import com.faifly.model.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query(value = """
        SELECT v.doctor_id, COUNT(DISTINCT v.patient_id)
        FROM visit v
        WHERE v.doctor_id IN :doctorIds
        GROUP BY v.doctor_id
        """, nativeQuery = true)
    List<Object[]> findDoctorTotalPatients(@Param("doctorIds") List<Integer> doctorIds);
}