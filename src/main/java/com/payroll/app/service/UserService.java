package com.payroll.app.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.payroll.app.dto.SigninDTO;
import com.payroll.app.dto.UserDTO;

@Service
public interface UserService {

	 public Map<String,Object>UserRegister(@RequestBody UserDTO userdto);
	 public Map<String,Object>UserSign(@RequestBody SigninDTO signdto);
	  
}
