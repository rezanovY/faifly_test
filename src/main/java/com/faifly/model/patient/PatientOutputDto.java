package com.faifly.model.patient;

import com.faifly.model.visit.VisitOutputDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PatientOutputDto {
    private String firstName;
    private String lastName;
    private List<VisitOutputDto> lastVisits;
}
