package com.faifly.service;

import com.faifly.model.doctor.Doctor;
import com.faifly.model.patient.Patient;
import com.faifly.model.visit.Visit;
import com.faifly.model.visit.VisitInputDto;
import com.faifly.repository.DoctorRepository;
import com.faifly.repository.PatientRepository;
import com.faifly.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public Integer createVisit(VisitInputDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        ZoneId doctorZone = ZoneId.of(doctor.getTimezone());
        ZonedDateTime startZdt = ZonedDateTime.parse(dto.getStart()).withZoneSameInstant(doctorZone);
        ZonedDateTime endZdt = ZonedDateTime.parse(dto.getEnd()).withZoneSameInstant(doctorZone);

        Instant start = startZdt.toInstant();
        Instant end = endZdt.toInstant();

        boolean exists = visitRepository.existsOverlap(doctor.getId(), start, end);
        if (exists) {
            throw new IllegalArgumentException("Doctor already has a visit at this time");
        }

        Visit visit = new Visit();
        visit.setDoctorId(doctor.getId());
        visit.setPatientId(patient.getId());
        visit.setStartDateTime(start);
        visit.setEndDateTime(end);

        Visit saved = visitRepository.save(visit);

        return saved.getId();
    }
}
