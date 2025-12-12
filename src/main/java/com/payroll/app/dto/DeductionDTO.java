package com.payroll.app.dto;
import lombok.Data;

@Data
public class DeductionDTO {
    private Long salaryId;
    private Double lop;
    private Double pf;
    private Double loan;
}
