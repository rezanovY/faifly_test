package com.faifly.service;

import com.faifly.model.doctor.DoctorOutputDto;
import com.faifly.model.doctor.Doctor;
import com.faifly.model.patient.Patient;
import com.faifly.model.patient.PatientOutputDto;
import com.faifly.model.patient.PatientsListOutputDto;
import com.faifly.model.visit.Visit;
import com.faifly.model.visit.VisitOutputDto;
import com.faifly.repository.DoctorRepository;
import com.faifly.repository.PatientRepository;
import com.faifly.repository.VisitRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;

    public PatientsListOutputDto getPatients(int page, int size, String search, List<Integer> doctorIds) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Patient> patients = patientRepository.findPatientsWithFilters(search, doctorIds, pageable);

        List<Integer> patientIds = patients.getContent().stream()
                .map(Patient::getId)
                .toList();

        List<Visit> lastVisits = visitRepository.findLastVisitsPerPatientPerDoctor(patientIds);

        List<Object[]> totalsRaw = doctorRepository.findDoctorTotalPatients(
                lastVisits.stream().map(Visit::getDoctorId).distinct().toList()
        );
        Map<Integer, Integer> doctorTotals = totalsRaw.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).intValue(),
                        r -> ((Number) r[1]).intValue()
                ));

        List<PatientOutputDto> data = patients.getContent().stream()
                .map(p -> {
                    List<VisitOutputDto> visits = lastVisits.stream()
                            .filter(v -> v.getPatientId().equals(p.getId()))
                            .map(v -> {
                                Doctor doctor = doctorRepository.findById(v.getDoctorId()).orElseThrow();
                                ZoneId zone = ZoneId.of(doctor.getTimezone());

                                String start = v.getStartDateTime().atZone(ZoneId.of("UTC"))
                                        .withZoneSameInstant(zone).toString();
                                String end = v.getEndDateTime().atZone(ZoneId.of("UTC"))
                                        .withZoneSameInstant(zone).toString();

                                return new VisitOutputDto(
                                        start,
                                        end,
                                        toDto(doctor, doctorTotals)
                                );
                            })
                            .toList();

                    return new PatientOutputDto(p.getFirstName(), p.getLastName(), visits);
                })
                .toList();

        return new PatientsListOutputDto(data, (int) patients.getTotalElements());
    }

    private DoctorOutputDto toDto(Doctor doctor,  Map<Integer, Integer> doctorTotals) {
        return new DoctorOutputDto(
                doctor.getFirstName(),
                doctor.getLastName(),
                doctorTotals.getOrDefault(doctor.getId(), 0));
    }
}















