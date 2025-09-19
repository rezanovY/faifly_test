package com.faifly.service;

import com.faifly.config.AbstractIntegrationTest;
import com.faifly.model.doctor.Doctor;
import com.faifly.model.patient.Patient;
import com.faifly.model.visit.VisitInputDto;
import com.faifly.repository.DoctorRepository;
import com.faifly.repository.PatientRepository;
import com.faifly.repository.VisitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class VisitServiceTest extends AbstractIntegrationTest {

    @Autowired
    private VisitService visitService;

    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;


    @Test
    void shouldThrowExceptionWhenVisitOverlaps() {
        Patient p1 = patientRepository.save(new Patient(null,"John", "Doe"));
        Patient p2 = patientRepository.save(new Patient(null,"Johnnnn", "Doennnn"));
        Doctor d = doctorRepository.save(new Doctor(null, "Alice", "Smith", "Europe/Berlin"));

        String start = "2025-09-17T10:00:00Z";
        String end   = "2025-09-17T11:00:00Z";

        VisitInputDto dto1 = new VisitInputDto(start, end, p1.getId(), d.getId());
        VisitInputDto dto2 = new VisitInputDto(start, end, p2.getId(), d.getId());

        visitService.createVisit(dto1);

        assertThatThrownBy(() -> visitService.createVisit(dto2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}