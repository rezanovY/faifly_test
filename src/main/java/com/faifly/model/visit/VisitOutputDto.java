package com.faifly.model.visit;

import com.faifly.model.doctor.DoctorOutputDto;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class VisitOutputDto {
    private String start;
    private String end;
    private DoctorOutputDto doctor;
}
