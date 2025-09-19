package com.faifly.repository;

import com.faifly.config.AbstractIntegrationTest;
import com.faifly.model.doctor.Doctor;
import com.faifly.model.patient.Patient;
import com.faifly.model.visit.Visit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class DoctorRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Test
    @DisplayName("DoctorRepository.findDoctorTotalPatients returns distinct patient counts")
    void testFindDoctorTotalPatients_ObjectArray() {
        // створюємо пацієнтів та лікаря (без ручних id)
        Patient p1 = patientRepository.save(new Patient(null, "John", "Doe"));
        Patient p2 = patientRepository.save(new Patient(null, "Johnnnn", "Doennnn"));
        Doctor d  = doctorRepository.save(new Doctor(null, "Alice", "Smith", "Europe/Berlin"));

        Instant s1 = Instant.parse("2025-09-17T10:00:00Z");
        Instant e1 = Instant.parse("2025-09-17T11:00:00Z");

        Visit v1 = new Visit(null, s1, e1, p1.getId(), d.getId());
        Visit v2 = new Visit(null, s1.plusSeconds(3600), e1.plusSeconds(3600), p1.getId(), d.getId());
        Visit v3 = new Visit(null, s1.plusSeconds(7200), e1.plusSeconds(7200), p2.getId(), d.getId());

        visitRepository.saveAll(List.of(v1, v2, v3));

        List<Object[]> raw = doctorRepository.findDoctorTotalPatients(List.of(d.getId()));

        Map<Integer, Integer> totals = raw.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).intValue(),
                        r -> ((Number) r[1]).intValue()
                ));

        assertThat(totals).hasSize(1);
        assertThat(totals).containsEntry(d.getId(), 2);
    }
}
