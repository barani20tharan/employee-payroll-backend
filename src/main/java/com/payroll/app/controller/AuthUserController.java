package com.payroll.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payroll.app.dto.DeductionDTO;
import com.payroll.app.dto.SalaryDTO;
import com.payroll.app.dto.SigninDTO;
import com.payroll.app.dto.UserDTO;
import com.payroll.app.model.EmployeeDeduction;
import com.payroll.app.model.EmployeeSalary;
import com.payroll.app.repository.DeductionRepository;
import com.payroll.app.repository.SalaryRepository;
import com.payroll.app.service.PayrollService;
import com.payroll.app.service.UserService;
@CrossOrigin(origins = "http://localhost:5173")


@RestController
public class AuthUserController {

@Autowired 
private UserService userservice;


	
	@PostMapping ("/Register")
	 public Map<String,Object>UserRegister(@RequestBody UserDTO userdto){
		return userservice.UserRegister(userdto);
	}
	
	@PostMapping("/Signin")
	public Map<String,Object>UserSign(@RequestBody SigninDTO signin){
		return userservice.UserSign(signin);
	}
	 


}
