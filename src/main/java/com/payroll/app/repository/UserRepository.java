package com.payroll.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.payroll.app.dto.UserDTO;

import com.payroll.app.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	 Users findByEmailAndPassword(String email, String password);
}


