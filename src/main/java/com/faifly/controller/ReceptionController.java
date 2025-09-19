package com.faifly.controller;

import com.faifly.model.patient.PatientsListOutputDto;
import com.faifly.model.visit.VisitInputDto;
import com.faifly.service.PatientService;
import com.faifly.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/patients")
public class ReceptionController {
    private final PatientService patientService;
    private final VisitService visitService;


    @PostMapping("/visit")
    public ResponseEntity<Integer> createVisit(@Valid @RequestBody VisitInputDto request) {
        Integer visitId = visitService.createVisit(request);
        URI location = URI.create("/api/visit/" + visitId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/list")
    public ResponseEntity<PatientsListOutputDto> getPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<Integer> doctorIds
    ) {
        doctorIds = doctorIds != null ? doctorIds : Collections.emptyList();
        PatientsListOutputDto result = patientService.getPatients(page, size, search, doctorIds);
        return ResponseEntity.ok(result);
    }
}
