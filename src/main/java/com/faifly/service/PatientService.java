package com.faifly.service;

import com.faifly.model.PatientVisitProjection;
import com.faifly.model.doctor.DoctorOutputDto;
import com.faifly.model.patient.PatientOutputDto;
import com.faifly.model.patient.PatientsListOutputDto;
import com.faifly.model.visit.VisitOutputDto;
import com.faifly.repository.PatientCustomRepository;
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
    private final PatientCustomRepository patientCustomRepository;

    public PatientsListOutputDto getPatients(int page, int size, String search, List<Integer> doctorIds) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PatientVisitProjection> pageResult =
                patientCustomRepository.findPatientsWithVisitsAndDoctors(search, doctorIds, pageable);

        Map<Integer, List<PatientVisitProjection>> grouped = pageResult.getContent().stream()
                .collect(Collectors.groupingBy(PatientVisitProjection::patientId));

        List<PatientOutputDto> data = grouped.values().stream()
                .map(list -> {
                    PatientVisitProjection first = list.getFirst();

                    List<VisitOutputDto> visits = list.stream()
                            .map(r -> {
                                ZoneId zone = ZoneId.of(r.doctorTimezone());

                                String start = r.startDateTime()
                                        .atZone(ZoneId.of("UTC"))
                                        .withZoneSameInstant(zone)
                                        .toString();

                                String end = r.endDateTime()
                                        .atZone(ZoneId.of("UTC"))
                                        .withZoneSameInstant(zone)
                                        .toString();

                                DoctorOutputDto doctorDto = new DoctorOutputDto(
                                        r.doctorFirstName(),
                                        r.doctorLastName(),
                                        r.doctorTotalPatients()
                                );

                                return new VisitOutputDto(start, end, doctorDto);
                            })
                            .toList();

                    return new PatientOutputDto(first.firstName(), first.lastName(), visits);
                })
                .toList();

        return new PatientsListOutputDto(data, (int) pageResult.getTotalElements());
    }
}















