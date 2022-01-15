package com.nice.todolist.util;

import java.util.Date;

import com.nice.todolist.dto.UserDto;

public class UserDtoBuilder {

	private UserDto model;
	
	public UserDtoBuilder(){
		model = new UserDto();
	}
	
	public UserDtoBuilder id(Long id) {
		model.setId(id);
	    return this;
	}

	public UserDtoBuilder userName(String userName) {
	     model.setUserName(userName);
	     return this;
	}
	
	public UserDtoBuilder firstName(String firstName) {
	     model.setFirstName(firstName);
	     return this;
	}
	
	public UserDtoBuilder lastName(String lastName) {
	     model.setLastName(lastName);
	     return this;
	}

	public UserDtoBuilder email(String email) {
	     model.setEmail(email);
	     return this;
	}
	
	public UserDtoBuilder roleId(String roleId) {
	     model.setRoleId(roleId);
	     return this;
	}
	
	public UserDtoBuilder createdDate(Date date) {
	     model.setCreatedDate(date);
	     return this;
	}
	
	public UserDtoBuilder modifiedDate(Date date) {
	     model.setModifiedDate(date);
	     return this;
	}
	
	public UserDto build() {
	       return model;
	}
}
