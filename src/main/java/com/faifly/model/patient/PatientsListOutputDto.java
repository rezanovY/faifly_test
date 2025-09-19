package com.faifly.model.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PatientsListOutputDto {
    private List<PatientOutputDto> patientsList;
    private Integer count;
}
