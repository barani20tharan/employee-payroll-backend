package com.payroll.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class EmployeeSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "salary", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private EmployeeDeduction deduction;


    private String empname;
    private String dept;
    private String email;
    private String mobilenumber;
    private Double basicPay;
    private Double hra;
    private Double da;
    private Double otherAllowances;
    private Double grossPay;

    public void calculateGrossPay() {
        double basic = (basicPay == null) ? 0 : basicPay;
        double hraAmount = (hra == null) ? 0 : hra;
        double daAmount = (da == null) ? 0 : da;
        double other = (otherAllowances == null) ? 0 : otherAllowances;

        this.grossPay = basic + hraAmount + daAmount + other;
    }
}
