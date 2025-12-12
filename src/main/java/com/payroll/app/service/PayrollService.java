package com.payroll.app.service;

import java.util.Map;
import com.payroll.app.dto.DeductionDTO;
import com.payroll.app.dto.SalaryDTO;

public interface PayrollService {

    Map<String, Object> saveSalary(SalaryDTO salaryDTO);

    public Map<String, Object> saveDeduction(Long salaryId, DeductionDTO dto);
    
    public Map<String, Object> saveDeduction(DeductionDTO dto);



    Map<String, Object> calculateNetPay(Long salaryId);
    
    public Map<String, Object> deleteUser(Long id);
    
    Map<String, Object> updateSalary(Long id, SalaryDTO dto);

    Map<String, Object> updateDeduction(Long id, DeductionDTO dto);

    Map<String, Object> loadUserData(String email);

}
