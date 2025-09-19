package com.faifly.controller;

import com.faifly.config.AbstractIntegrationTest;
import com.faifly.model.doctor.Doctor;
import com.faifly.model.patient.Patient;
import com.faifly.model.visit.VisitInputDto;
import com.faifly.repository.DoctorRepository;
import com.faifly.repository.PatientRepository;
import com.faifly.service.PatientService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReceptionControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void shouldReturnCreatedOnValidVisit() throws Exception {
        Patient p = patientRepository.save(new Patient(null, "John", "Doe"));
        Doctor d = doctorRepository.save(new Doctor(null, "Alice", "Smith", "Europe/Berlin"));

        String start = "2025-09-17T10:00:00Z";
        String end   = "2025-09-17T11:00:00Z";

        VisitInputDto dto = new VisitInputDto(start, end, p.getId(), d.getId());

        mockMvc.perform(post("/api/patients/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}