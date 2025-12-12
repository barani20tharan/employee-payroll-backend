package com.payroll.app.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.payroll.app.dto.SigninDTO;
import com.payroll.app.dto.UserDTO;
import com.payroll.app.model.EmployeeDeduction;
import com.payroll.app.model.EmployeeSalary;
import com.payroll.app.model.Users;
import com.payroll.app.repository.DeductionRepository;
import com.payroll.app.repository.SalaryRepository;
import com.payroll.app.repository.UserRepository;
import com.payroll.app.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userrepository;

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private DeductionRepository deductionRepository;

    @Override
    public Map<String, Object> UserRegister(UserDTO userdto) {
        try {
            Users user = new Users();
            user.setId(userdto.getId());
            user.setUsername(userdto.getUsername());
            user.setPassword(userdto.getPassword());
            user.setEmail(userdto.getEmail());
            userrepository.save(user);

            return ResponseUtil.generateResponse(HttpStatus.OK, "Register Successful", new Date());
        } catch (Exception e) {
            log.error("Error registering user:", e);
            return ResponseUtil.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Credentials", new Date());
        }
    }

    // ============================================
    // FIXED LOGIN API → Returns lists for frontend
    // ============================================

    @Override
    public Map<String, Object> UserSign(SigninDTO signdto) {
        try {
            Users user = userrepository.findByEmailAndPassword(
                    signdto.getEmail(), 
                    signdto.getPassword()
            );

            if (user == null) {
                return ResponseUtil.generateResponse(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid Credentials",
                    new Date()
                );
            }

            // GET ALL EMPLOYEES (salary records)
            List<EmployeeSalary> salaries =
                    salaryRepository.findByEmail(signdto.getEmail());

            // GET ALL DEDUCTIONS for this user
            List<EmployeeDeduction> deductions =
                    deductionRepository.findBySalary_Email(signdto.getEmail());

            // Compute netpay (use first entry if exists)
            double netpay = 0;
            if (!salaries.isEmpty() && !deductions.isEmpty()) {
                EmployeeSalary s = salaries.get(0);
                EmployeeDeduction d = deductions.get(0);
                netpay = s.getGrossPay() - d.getTotalDeduction();
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("user", user);
            payload.put("salaries", salaries);     // array
            payload.put("deductions", deductions); // array
            payload.put("netpay", netpay);

            return ResponseUtil.generateResponse(
                    HttpStatus.OK,
                    "Login Successful",
                    new Date(),
                    payload
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.generateResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Login failed",
                    new Date()
            );
        }
    }

}
