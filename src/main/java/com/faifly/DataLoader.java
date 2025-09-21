package com.faifly;

import com.faifly.model.doctor.Doctor;
import com.faifly.model.patient.Patient;
import com.faifly.model.visit.Visit;
import com.faifly.repository.DoctorRepository;
import com.faifly.repository.PatientRepository;
import com.faifly.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    @Override
    public void run(String... args) throws Exception {
        if (visitRepository.count() > 0) {
            return;
        }
        List<Doctor> doctors = IntStream.rangeClosed(1, 50)
                .mapToObj(i -> Doctor.builder()
                        .firstName("Doc" + i)
                        .lastName("Surname" + i)
                        .timezone("Europe/Kyiv")
                        .build())
                .collect(Collectors.toList());
        doctorRepository.saveAll(doctors);

        List<Patient> patients = IntStream.rangeClosed(1, 1000)
                .mapToObj(i -> Patient.builder()
                        .firstName("Patient" + i)
                        .lastName("Lastname" + i)
                        .build())
                .collect(Collectors.toList());
        patientRepository.saveAll(patients);

        Random rnd = new Random();
        List<Visit> visits = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Patient p = patients.get(rnd.nextInt(patients.size()));
            Doctor d = doctors.get(rnd.nextInt(doctors.size()));

            LocalDateTime start = LocalDateTime.now().minusDays(rnd.nextInt(365)).withHour(rnd.nextInt(8) + 8).withMinute(0);
            LocalDateTime end = start.plusMinutes(30 + (rnd.nextInt(4) * 15));
            Instant startInstant = start.atZone(ZoneId.of(d.getTimezone())).toInstant();
            Instant endInstant = end.atZone(ZoneId.of(d.getTimezone())).toInstant();

            visits.add(Visit.builder()
                    .startDateTime(startInstant)
                    .endDateTime(endInstant)
                    .patientId(p.getId())
                    .doctorId(d.getId())
                    .build());
        }
        visitRepository.saveAll(visits);
    }
}
