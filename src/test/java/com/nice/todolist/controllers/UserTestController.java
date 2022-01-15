package com.nice.todolist.controllers;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.nice.todolist.dto.UserDto;
import com.nice.todolist.entities.User;
import com.nice.todolist.exception.TodoNotFoundException;
import com.nice.todolist.services.UserService;
import com.nice.todolist.util.Constants;
import com.nice.todolist.util.TestUtil;
import com.nice.todolist.util.UserBuilder;
import com.nice.todolist.util.UserDtoBuilder;

/**
 * @author Shekar Nyala
 *
 */
public class UserTestController extends AbstractTestController {
	
	//private MockMvc mockMvc;

	//@Autowired @Qualifier(value="userService")
	@Mock
    private UserService userServiceMock;
    
    /*@Autowired
    private WebApplicationContext webApplicationContext;*/
    
    @InjectMocks
    private UserController userController;

    @Before
    public void setUping() {
    	//Mockito.reset(userServiceMock);
    	// Initialize Mockito annotated components
    	initMocks(this);
    	setUp(userController);
        //mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void add_EmptyUserEntry_ShouldReturnValidationErrorForUserName() throws Exception {
        UserDto dto = new UserDto();

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isBadRequest());/*
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", containsString("userName:The userName should not be empty.")));*/
         
        verifyZeroInteractions(userServiceMock);
    }
    
    @Test
    public void add_UserEntryWithUserNameTooLongValue_ShouldReturnValidationErrors() throws Exception {
        UserDto dto = new UserDto();
        dto.setUserName(TestUtil.createStringWithLength(Constants.MAX_LENGTH_USERNAME+1));
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isBadRequest());/*
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                		"userName:The maximum length of the userName is 30 characters."
                		,"userName:must match \"^[A-Za-z_]\\w{7,29}$\""
                )));*/
         
        verifyZeroInteractions(userServiceMock);
    }
    
    @Test
    public void add_UserEntryWithEmailTooLongValue_ShouldReturnValidationErrors() throws Exception {
        UserDto dto = TestUtil.getTestUserDto();
        dto.setEmail(TestUtil.createStringWithLength(Constants.MAX_LENGTH_EMAIL+1));
        
        mockMvc.perform(post("/api/users")
					           .contentType(MediaType.APPLICATION_JSON)
					           .accept(MediaType.APPLICATION_JSON)
					           .content(TestUtil.convertObjectToJsonBytes(dto))
			  ).andExpect(status().isBadRequest());
         
        verifyZeroInteractions(userServiceMock);
    }
    
    @Test
    public void add_NewUserEntry_ShouldAddUserEntryAndReturnAddedEntry() throws Exception {
        UserDto dto = getTestUserDto();
        User  added = getTestUser();
        
        when(userServiceMock.createUser(any(UserDto.class))).thenReturn(added);
        
        mockMvc.perform(post("/api/users")
		                .contentType(TestUtil.APPLICATION_JSON_UTF8)
		                .accept(TestUtil.APPLICATION_JSON_UTF8)
		                .content(TestUtil.convertObjectToJsonBytes(dto))
              ).andExpect(status().isCreated())
               .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.userName", is("nshekarb4u")))
               .andExpect(jsonPath("$.email", is("nshekarb4u@nice.com")));
         
        ArgumentCaptor<UserDto> dtoCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(userServiceMock, times(1)).createUser(dtoCaptor.capture());
        verifyNoMoreInteractions(userServiceMock);
        
        UserDto dtoArgument = dtoCaptor.getValue();
        assertNull(dtoArgument.getId());
        assertThat(dtoArgument.getUserName(), is("nshekarb4u"));
        assertThat(dtoArgument.getEmail(), is("nshekarb4u@nice.com"));
    }
    
    @Test
    public void deleteById_UserEntryFound_ShouldDeleteUserEntryAndReturnIt() throws Exception {
        User deleted = TestUtil.getTestUser();

        when(userServiceMock.deleteUserById(1L)).thenReturn(deleted);

        mockMvc.perform(delete("/api/users/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is(TestUtil.USERNAME)))
                .andExpect(jsonPath("$.firstName", is(TestUtil.FIRSTNAME)))
                .andExpect(jsonPath("$.email", is(TestUtil.EMAIL)));

        verify(userServiceMock, times(1)).deleteUserById(1L);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void deleteById_UserIsNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(userServiceMock.deleteUserById(3L)).thenThrow(new TodoNotFoundException(""));

        mockMvc.perform(delete("/api/users/{id}", 3L)
        		.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        	   )
                .andExpect(status().isNotFound());

        verify(userServiceMock, times(1)).deleteUserById(3L);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void findAll_UsersFound_ShouldReturnFoundUserEntries() throws Exception {
        User first  = TestUtil.getTestUser();first.setUserName("userName1");first.setId(1L);
        User second = TestUtil.getTestUser();second.setUserName("userName2");second.setId(2L);
         
        when(userServiceMock.getAllUsers()).thenReturn(Arrays.asList(first, second));
        
        mockMvc.perform(get("/api/users")
        		  .contentType(TestUtil.APPLICATION_JSON_UTF8)
                  .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userName", is("userName1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].userName", is("userName2")));
        
        verify(userServiceMock, times(1)).getAllUsers();
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void findById_UserEntryFound_ShouldReturnFoundUserEntry() throws Exception {
        User found = TestUtil.getTestUser();
        String userId = String.valueOf(1L);
        
        when(userServiceMock.findUserByIdOrName(userId)).thenReturn(found);
        
        mockMvc.perform(get("/api/users/{id}", 1L)
        		.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is(TestUtil.USERNAME)))
                .andExpect(jsonPath("$.email", is(TestUtil.EMAIL)))
                .andExpect(jsonPath("$.firstName", is(TestUtil.FIRSTNAME)));
        
        verify(userServiceMock, times(1)).findUserByIdOrName(userId);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void findById_UserEntryNotFound_ShouldReturnHttpStatusCode404() throws Exception {
    	String userId = String.valueOf(1L);
        when(userServiceMock.findUserByIdOrName(userId)).thenThrow(new TodoNotFoundException(""));

        mockMvc.perform(get("/api/users/{id}", 1L)
        		.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isNotFound());

        verify(userServiceMock, times(1)).findUserByIdOrName(userId);
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void update_EmptyUserEntry_ShouldReturnValidationErrorForUserName() throws Exception {
        UserDto dto = new UserDto();

        mockMvc.perform(patch("/api/users/{id}", 1L)
		                .contentType(TestUtil.APPLICATION_JSON_UTF8)
		                .content(TestUtil.convertObjectToJsonBytes(dto))
        	  ).andExpect(status().isBadRequest());

        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void update_UserNameIsTooLong_ShouldReturnValidationErrorsForUserName() throws Exception {
        UserDto dto = new UserDto();
        dto.setUserName(TestUtil.createStringWithLength(Constants.MAX_LENGTH_USERNAME + 1));

        mockMvc.perform(patch("/api/users/{id}", 1L)
		                .contentType(TestUtil.APPLICATION_JSON_UTF8)
		                .content(TestUtil.convertObjectToJsonBytes(dto))
        	  ).andExpect(status().isBadRequest());/*
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                		"userName:The maximum length of the userName is 30 characters."
                	   ,"userName:must match \"^[A-Za-z_]\\w{7,29}$\""
                )));*/

        verifyZeroInteractions(userServiceMock);
    }

    @Test
    public void update_UserEntryNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        UserDto dto = TestUtil.getUpdatedTestUserDto();dto.setId(3L);

        when(userServiceMock.updateUser(anyLong(), any(UserDto.class))).thenThrow(new TodoNotFoundException(""));

		MvcResult mvcResult = mockMvc.perform(patch("/api/users/{id}", 3L)
								                .contentType(TestUtil.APPLICATION_JSON_UTF8)
								                .content(TestUtil.convertObjectToJsonBytes(dto))
		        	  				).andReturn();
		
        ArgumentCaptor<UserDto> dtoCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(userServiceMock, times(1)).updateUser(anyLong(),dtoCaptor.capture());
        verifyNoMoreInteractions(userServiceMock);

        UserDto dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getId(), is(3L));
        assertThat(dtoArgument.getUserName(), is(TestUtil.USERNAME_UPDATED));
        assertThat(dtoArgument.getEmail(), is(TestUtil.EMAIL_UPDATED));
    }

    @Test
    public void update_UserEntryFound_ShouldUpdateUserEntryAndReturnIt() throws Exception {
        UserDto dto = TestUtil.getUpdatedTestUserDto();

        User updated = TestUtil.getUpdatedTestUser();

        when(userServiceMock.updateUser(anyLong(), any(UserDto.class))).thenReturn(updated);

        mockMvc.perform(patch("/api/users/{id}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is(TestUtil.USERNAME_UPDATED)))
                .andExpect(jsonPath("$.email", is(TestUtil.EMAIL_UPDATED)));

        ArgumentCaptor<UserDto> dtoCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(userServiceMock, times(1)).updateUser(anyLong(),dtoCaptor.capture());
        verifyNoMoreInteractions(userServiceMock);

        UserDto dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getId(), is(1L));
        assertThat(dtoArgument.getUserName(), is(TestUtil.USERNAME_UPDATED));
        assertThat(dtoArgument.getEmail(), is(TestUtil.EMAIL_UPDATED));
    }


	private UserDto getTestUserDto() {
		return new UserDtoBuilder()
		          .userName("nshekarb4u")
		          .email("nshekarb4u@nice.com")
		          .build();
	}
	
	private User getTestUser() {
		return new UserBuilder()
		          .id(1L)
		          .userName("nshekarb4u")
		          .email("nshekarb4u@nice.com")
		          .build();
	}
}
