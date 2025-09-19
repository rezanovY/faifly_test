package com.faifly.model.visit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitInputDto {
    @NotBlank
    private String start;

    @NotBlank
    private String end;

    @NotNull
    private Integer patientId;

    @NotNull
    private Integer doctorId;
}
