package com.faifly.model.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorOutputDto {
    private String firstName;
    private String lastName;
    private Long totalPatients;
}
