package com.payroll.app.service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.payroll.app.dto.DeductionDTO;
import com.payroll.app.dto.SalaryDTO;
import com.payroll.app.model.EmployeeDeduction;
import com.payroll.app.model.EmployeeSalary;
import com.payroll.app.repository.DeductionRepository;
import com.payroll.app.repository.SalaryRepository;
import com.payroll.app.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
  public  class PayrollServiceImp implements PayrollService {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private DeductionRepository deductionRepository;

    @Override
    public Map<String, Object> saveSalary(SalaryDTO salaryDTO) {
        try {
            EmployeeSalary salary = new EmployeeSalary();
            salary.setBasicPay(salaryDTO.getBasicPay());
            salary.setHra(salaryDTO.getHra());
            salary.setDa(salaryDTO.getDa());
            salary.setOtherAllowances(salaryDTO.getOtherAllowances());
            salary.setDept(salaryDTO.getDept());
            salary.setEmail(salaryDTO.getEmail());
            salary.setEmpname(salaryDTO.getEmpname());
            salary.setMobilenumber(salaryDTO.getMobilenumber());

            // First calculate gross pay
            salary.calculateGrossPay();

            // Save salary
            EmployeeSalary savedSalary = salaryRepository.save(salary);

            // Create default deduction (0 values)
            EmployeeDeduction deduction = new EmployeeDeduction();
            deduction.setSalary(savedSalary);
            deduction.setLop(0.0);
            deduction.setPf(0.0);
            deduction.setLoan(0.0);
            deduction.calculateTotalDeduction();

            deductionRepository.save(deduction);

            return ResponseUtil.generateResponse(
                HttpStatus.OK,
                "Salary Saved Successfully",
                new Date(),
                savedSalary
            );

        } catch (Exception e) {
            log.error("Error saving salary:", e);
            return ResponseUtil.generateResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Salary Save Failed",
                new Date()
            );
        }
    }

    @Override
    public Map<String, Object> saveDeduction(Long salaryId, DeductionDTO dto) {
        try {
            EmployeeSalary salary = salaryRepository.findById(salaryId)
                    .orElseThrow(() -> new RuntimeException("Salary not found"));

            EmployeeDeduction deduction = deductionRepository.findBySalary_Id(salaryId);
            if (deduction == null) {
                deduction = new EmployeeDeduction();
            }


            deduction.setSalary(salary);
            deduction.setLop(dto.getLop());
            deduction.setPf(dto.getPf());
            deduction.setLoan(dto.getLoan());
            deduction.calculateTotalDeduction();

            deductionRepository.save(deduction);

            return ResponseUtil.generateResponse(
                HttpStatus.OK,
                "Deduction Saved Successfully",
                new Date(),
                deduction
            );

        } catch (Exception e) {
            log.error("Error saving deduction:", e);
            return ResponseUtil.generateResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Deduction Save Failed",
                new Date()
            );
        }
    }

   
    @Override
    public Map<String, Object> calculateNetPay(Long salaryId) {
        try {
            EmployeeSalary salary = salaryRepository.findById(salaryId)
                    .orElseThrow(() -> new RuntimeException("Salary not found"));

            EmployeeDeduction deduction = deductionRepository.findBySalary_Id(salaryId);
            if (deduction == null) {
                throw new RuntimeException("Deduction not found");
            }

            double netPay = salary.getGrossPay() - deduction.getTotalDeduction();

            Map<String, Object> employeeDetails = new HashMap<>();
            employeeDetails.put("name", salary.getEmpname());
            employeeDetails.put("department", salary.getDept());
            employeeDetails.put("basicSalary", salary.getBasicPay());
            employeeDetails.put("email", salary.getEmail());
            employeeDetails.put("mobilenumber", salary.getMobilenumber());

            Map<String, Object> deductionDetails = new HashMap<>();
            deductionDetails.put("pf", deduction.getPf());
            deductionDetails.put("loan", deduction.getLoan());
            deductionDetails.put("lop", deduction.getLop());
            deductionDetails.put("totalDeduction", deduction.getTotalDeduction());

            Map<String, Object> data = new HashMap<>();
            data.put("employee", employeeDetails);
            data.put("deduction", deductionDetails);
            data.put("grossPay", salary.getGrossPay());
            data.put("netPay", netPay);

            return ResponseUtil.generateResponse(
                    HttpStatus.OK,
                    "Net Pay Calculated Successfully",
                    new Date(),
                    data
            );

        } catch (Exception e) {
        	
            return ResponseUtil.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    new Date()
            );
        }
    }

	@Override
	public Map<String, Object> deleteUser(Long id) {
		log.debug("Attempting to delete user with ID: {}", id);
		try {
			salaryRepository.deleteById(id);
			log.info("User with ID: {} deleted successfully", id);
			return ResponseUtil.generateResponse(HttpStatus.OK, "Delete Success", new Date());
		}catch (Exception e) {
			log.error("Error deleting user: ", e);
			return ResponseUtil.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Delete Failed", new Date());
		}
	}

	@Override
	public Map<String, Object> saveDeduction(DeductionDTO dto) {
		EmployeeSalary salary = salaryRepository.findById(dto.getSalaryId())
	              .orElseThrow(() -> new RuntimeException("Salary not found"));

		return null;
	}
	
	@Override
	public Map<String, Object> updateSalary(Long id, SalaryDTO dto) {
	    try {
	        EmployeeSalary salary = salaryRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Salary not found"));

	        salary.setBasicPay(dto.getBasicPay());
	        salary.setHra(dto.getHra());
	        salary.setDa(dto.getDa());
	        salary.setOtherAllowances(dto.getOtherAllowances());
	        salary.setDept(dto.getDept());
	        salary.setEmail(dto.getEmail());
	        salary.setEmpname(dto.getEmpname());
	        salary.setMobilenumber(dto.getMobilenumber());

	        // Recalculate gross pay
	        salary.calculateGrossPay();

	        EmployeeSalary updated = salaryRepository.save(salary);

	        return ResponseUtil.generateResponse(
	                HttpStatus.OK,
	                "Salary Updated Successfully",
	                new Date(),
	                updated
	        );

	    } catch (Exception e) {
	        log.error("Error updating salary:", e);
	        return ResponseUtil.generateResponse(
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                "Salary Update Failed",
	                new Date()
	        );
	    }
	}
	@Override
	public Map<String, Object> updateDeduction(Long id, DeductionDTO dto) {
	    try {
	        EmployeeDeduction deduction = deductionRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Deduction not found"));

	        deduction.setLop(dto.getLop());
	        deduction.setPf(dto.getPf());
	        deduction.setLoan(dto.getLoan());

	        // Recalculate total deduction
	        deduction.calculateTotalDeduction();

	        EmployeeDeduction updated = deductionRepository.save(deduction);

	        return ResponseUtil.generateResponse(
	                HttpStatus.OK,
	                "Deduction Updated Successfully",
	                new Date(),
	                updated
	        );

	    } catch (Exception e) {
	        log.error("Error updating deduction:", e);
	        return ResponseUtil.generateResponse(
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                "Deduction Update Failed",
	                new Date()
	        );
	    }
	}

	private final SalaryRepository salaryRepo;
    private final DeductionRepository deductionRepo;

    public PayrollServiceImp(SalaryRepository salaryRepo, DeductionRepository deductionRepo) {
        this.salaryRepo = salaryRepo;
        this.deductionRepo = deductionRepo;
    }

    @Override
    public Map<String, Object> loadUserData(String email) {
        Map<String, Object> data = new HashMap<>();

        // ✔ Load employee salary rows
        List<EmployeeSalary> salaries = salaryRepo.findByEmail(email);

        // ✔ Load deductions linked to that user's salaries
        List<EmployeeDeduction> deductions = deductionRepo.findBySalary_Email(email);

        data.put("salaries", salaries);
        data.put("deductions", deductions);

        return data;
    }
}

