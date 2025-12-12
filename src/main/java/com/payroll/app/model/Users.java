package com.payroll.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class Users {
     @Id
     @GeneratedValue(strategy =GenerationType.IDENTITY)
	private long id;
     
     
	private String username;
	
	@Column(unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
}
