package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="tblcarnation")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long registerId;
	
	@NotBlank(message="Username cannot be blank")
	@Column(name = "userName")
	private String userName;
	
	@NotBlank(message="Email cannot be blank")
	@Email(message="Required Email Format") // Fixed spelling: Required
	private String email;
	
	@NotBlank(message="Password cannot be blank")
	private String password;

	// GETTERS AND SETTERS
	
	public Long getRegisterId() {
		return registerId;
	}

	// FIXED: Parameter must be Long to match the field
	public void setRegisterId(Long registerId) {
		this.registerId = registerId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}