package com.faifly.model;

import java.time.Instant;

public record PatientVisitProjection(
        Integer patientId,
        String firstName,
        String lastName,
        Integer doctorId,
        String doctorFirstName,
        String doctorLastName,
        String doctorTimezone,
        Instant startDateTime,
        Instant endDateTime,
        Long doctorTotalPatients
) {}