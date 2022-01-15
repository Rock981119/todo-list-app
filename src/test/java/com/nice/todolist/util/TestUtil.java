package com.nice.todolist.util;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nice.todolist.dto.TaskDto;
import com.nice.todolist.dto.UserDto;
import com.nice.todolist.entities.Task;
import com.nice.todolist.entities.User;

public class TestUtil {
    
	public static final Long ID=1L;
	public static final String USERNAME = "userName";
	public static final String USERNAME_UPDATED = "userNameUpdated";
	public static final String FIRSTNAME = "firstName";
	public static final String FIRSTNAME_UPDATED = "firstNameUpdated";
	public static final String EMAIL = "email@nice.com";
	public static final String EMAIL_UPDATED = "emailupdated@nice.com";
    
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    
    private static final String CHARACTER = "a";

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
    
	public static UserDto getTestUserDto() {
		return new UserDtoBuilder()
		          .userName(USERNAME)
		          .email(EMAIL)
		          .firstName(FIRSTNAME)
		          .build();
	}
	
	public static User getTestUser() {
		return new UserBuilder()
		          .id(1L)
		          .userName(USERNAME)
		          .email(EMAIL)
		          .firstName(FIRSTNAME)
		          .build();
	}
	
	/**
	 * @return User - having updated details
	 */
	public static UserDto getUpdatedTestUserDto() {
		return new UserDtoBuilder()
                .id(ID)
                .userName(USERNAME_UPDATED)
                .firstName(FIRSTNAME_UPDATED)
                .email(EMAIL_UPDATED)
                .build();
	}
	
	/**
	 * @return User - having updated details
	 */
	public static User getUpdatedTestUser() {
		return new UserBuilder()
                .id(ID)
                .userName(USERNAME_UPDATED)
                .firstName(FIRSTNAME_UPDATED)
                .email(EMAIL_UPDATED)
                .build();
	}
	
	public static TaskDto getTestTaskDto() {
		return new TaskDtoBuilder()
		          .name("taskNameDto")
		          .description("taskDescriptionDto")
		          .build();
	}
	
	public static Task getTestTask() {
		return new TaskBuilder()
		          .id(1L)
		          .name("taskName")
		          .description("taskDescription")
		          .build();
	}
	
	public static Task getUpdatedTestTask() {
		return new TaskBuilder()
                .id(ID)
                .name("name_Updated")
                .description("description_Updated")
                .build();
	}
	
	public static TaskDto getUpdatedTestTaskDto() {
		return new TaskDtoBuilder()
                .id(ID)
                .name("name_Updated")
                .description("description_Updated")
                .build();
	}

}
