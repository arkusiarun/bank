package com.zink.bank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import java.sql.Date;

@Data
@Entity
@Table(name =  "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

	@Id
	@Column(name = "id")
	public Integer id;

	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "last_logged")
	private Date lastLoggedInDate;

	@Column(name = "active")
	private boolean active;

}