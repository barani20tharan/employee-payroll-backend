package com.payroll.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.payroll.app.model.EmployeeSalary;

public interface SalaryRepository extends JpaRepository<EmployeeSalary, Long> {
	List<EmployeeSalary> findByEmail(String email);
}
