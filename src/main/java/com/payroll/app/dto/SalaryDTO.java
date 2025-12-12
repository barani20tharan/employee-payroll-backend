package com.payroll.app.dto;

import lombok.Data;

@Data
public class SalaryDTO {
	
    private Long id;
	private String empname;
    private String dept;
    private String email;
    private String mobilenumber;
    private Double basicPay;
    private Double hra;
    private Double da;
    private Double otherAllowances;
}
