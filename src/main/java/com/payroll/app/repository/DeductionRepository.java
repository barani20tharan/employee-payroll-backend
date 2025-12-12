package com.payroll.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.payroll.app.model.EmployeeDeduction;

public interface DeductionRepository extends JpaRepository<EmployeeDeduction, Long> {

    EmployeeDeduction findBySalary_Id(Long salaryId); // Correct finder
    List<EmployeeDeduction> findBySalary_Email(String email);
    List<EmployeeDeduction> findAll();
}
