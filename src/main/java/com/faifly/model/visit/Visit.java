package com.faifly.model.visit;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Instant startDateTime;
    private Instant endDateTime;
    @Column(name = "patient_id", nullable = false)
    private Integer patientId;
    @Column(name = "doctor_id", nullable = false)
    private Integer doctorId;

}
