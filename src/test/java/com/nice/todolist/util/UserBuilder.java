package com.nice.todolist.util;

import java.util.Date;

import com.nice.todolist.entities.User;

public class UserBuilder {

	private User model;
	
	public UserBuilder(){
		model = new User();
	}
	
	public UserBuilder id(Long id) {
		model.setId(id);
	    return this;
	}

	public UserBuilder userName(String userName) {
	     model.setUserName(userName);
	     return this;
	}
	
	public UserBuilder firstName(String firstName) {
	     model.setFirstName(firstName);
	     return this;
	}
	
	public UserBuilder lastName(String lastName) {
	     model.setLastName(lastName);
	     return this;
	}

	public UserBuilder email(String email) {
	     model.setEmail(email);
	     return this;
	}
	
	public UserBuilder roleId(String roleId) {
	     model.setRoleId(roleId);
	     return this;
	}
	
	public UserBuilder createdDate(Date date) {
	     model.setCreatedDate(date);
	     return this;
	}
	
	public UserBuilder modifiedDate(Date date) {
	     model.setModifiedDate(date);
	     return this;
	}
	
	public User build() {
	       return model;
	}
}
