package com.payroll.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payroll.app.dto.DeductionDTO;
import com.payroll.app.dto.SalaryDTO;
import com.payroll.app.model.EmployeeDeduction;
import com.payroll.app.model.EmployeeSalary;
import com.payroll.app.repository.DeductionRepository;
import com.payroll.app.repository.SalaryRepository;
import com.payroll.app.service.PayrollService;
import com.payroll.app.util.ResponseUtil;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;
    
    @Autowired
    private SalaryRepository salaryRepository;
    
    @Autowired
    private DeductionRepository deductionRepository;

    // ---------------------------
    // SAVE Salary
    // ---------------------------
    @PostMapping("/salary")
    public Map<String, Object> saveSalary(@RequestBody SalaryDTO dto) {
        return payrollService.saveSalary(dto);
    }

    // ---------------------------
    // SAVE Deduction with salaryId
    // ---------------------------
    @PostMapping("/deduction/{salaryId}")
    public Map<String, Object> saveDeduction(@PathVariable Long salaryId,
                                             @RequestBody DeductionDTO dto) {
        return payrollService.saveDeduction(salaryId, dto);
    }

    // ---------------------------
    // SAVE Deduction (normal)
    // ---------------------------
    @PostMapping("/deduction")
    public Map<String, Object> saveDeduction(@RequestBody DeductionDTO dto) {
        return payrollService.saveDeduction(dto);
    }

    @GetMapping("/deductions")
    public Map<String, Object> getAllDeductions() {

        List<EmployeeDeduction> deductionList = deductionRepository.findAll();

        return ResponseUtil.generateResponse(
            HttpStatus.OK,
            "All deductions loaded successfully",
            new Date(),
            deductionList
        );
    }

    // ---------------------------
    // Calculate Net Pay
    // ---------------------------
    @GetMapping("/netpay/{salaryId}")
    public Map<String, Object> getNetPay(@PathVariable Long salaryId) {
        return payrollService.calculateNetPay(salaryId);
    }

    // ---------------------------
    // DELETE Employee Payroll
    // ---------------------------
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        return payrollService.deleteUser(id);
    }


    // ----------------------------------------------------------
    // 🔥 NEW API: UPDATE SALARY DETAILS
    // ----------------------------------------------------------
    @PutMapping("/salary/update/{id}")
    public Map<String, Object> updateSalary(@PathVariable Long id,
                                            @RequestBody SalaryDTO dto) {
        return payrollService.updateSalary(id, dto);
    }


    // ----------------------------------------------------------
    // 🔥 NEW API: UPDATE DEDUCTION DETAILS
    // ----------------------------------------------------------
    @PutMapping("/deduction/update/{id}")
    public Map<String, Object> updateDeduction(@PathVariable Long id,
                                               @RequestBody DeductionDTO dto) {
        return payrollService.updateDeduction(id, dto);
    }
    @GetMapping("/user/data/{email}")
    public Map<String, Object> loadUserData(@PathVariable String email) {

        List<EmployeeSalary> salaryList = salaryRepository.findByEmail(email);

        List<EmployeeDeduction> deductionList = new ArrayList<>();

        for (EmployeeSalary sal : salaryList) {
            EmployeeDeduction ded = deductionRepository.findBySalary_Id(sal.getId());
            if (ded != null) deductionList.add(ded);
        }

        Map<String, Object> data = Map.of(
            "salaries", salaryList,
            "deductions", deductionList
        );

        return ResponseUtil.generateResponse(
            HttpStatus.OK,
            "User data loaded successfully",
            new Date(),
            data
        );
    }
}