package com.nice.todolist.dto;

import static com.nice.todolist.util.Constants.REGEX_USER_NAME;

import java.util.Date;

import javax.validation.constraints.Pattern;

import lombok.Data;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nice.todolist.util.Constants;
import com.nice.todolist.util.JsonDateSerializer;

@Data
public class UserDto {

	private Long id;
	
	@NotEmpty
	@Pattern(regexp = REGEX_USER_NAME)
	@Length(min=Constants.MIN_LENGTH_USERNAME,max = Constants.MAX_LENGTH_USERNAME)
	private String userName;
	
	@Length(max = Constants.MAX_LENGTH_NAMEPART)
	private String firstName;
	
	@Length(max = Constants.MAX_LENGTH_NAMEPART)
	private String middleName;
	
	@Length(max = Constants.MAX_LENGTH_NAMEPART)
	private String lastName;
	
	@Length(max = Constants.MAX_LENGTH_EMAIL)
	@Email
	private String email;
	
	@Length(max = Constants.MAX_LENGTH_ROLEID)
	private String roleId;
	
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date createdDate;
	
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date modifiedDate;
}
