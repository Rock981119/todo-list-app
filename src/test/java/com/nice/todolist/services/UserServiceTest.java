/**
 * 
 */
package com.nice.todolist.services;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.nice.todolist.dto.UserDto;
import com.nice.todolist.entities.User;
import com.nice.todolist.exception.TodoException;
import com.nice.todolist.exception.TodoNotFoundException;
import com.nice.todolist.repositories.UserRepository;
import com.nice.todolist.services.impl.UserServiceImpl;
import com.nice.todolist.util.TestUtil;

/**
 * @author nshek
 *
 */
public class UserServiceTest {
	
	private static final Long ID = 1L;    
    private static final Date CURRENT_DATE = new Date();
    
    private static final SimpleDateFormat DATEFORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    
    @Mock private UserRepository mockUserRepository;
    private UserService userService;
    
	@Before
    public void setUp() {
		initMocks(this);
		userService = new UserServiceImpl(mockUserRepository);
	}
	
	@Test
	public void add_NewUser_ShouldBeSavedSuccessfully() {
		UserDto userDto = TestUtil.getTestUserDto();
		
		userService.createUser(userDto);
		
		ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
		verify(mockUserRepository, times(1)).save(userArgument.capture());
		
		verifyNoMoreInteractions(mockUserRepository);
		
		User model = userArgument.getValue();

        assertNull(model.getId());
        assertThat(model.getUserName(), is(userDto.getUserName()));
        assertThat(model.getFirstName(), is(userDto.getFirstName()));
        assertThat(model.getEmail(), is(userDto.getEmail()));
        assertThat(DATEFORMATTER.format(model.getCreatedDate()),is(DATEFORMATTER.format(CURRENT_DATE)));
        assertNull(model.getModifiedDate());
	}
	
	@Test
    public void findAll_ShouldReturnListOfUsers() {
        List<User> models = new ArrayList<>();
        when(mockUserRepository.findAll()).thenReturn(models);

        List<User> actual = userService.getAllUsers();

        verify(mockUserRepository, times(1)).findAll();
        
        verifyNoMoreInteractions(mockUserRepository);

        assertThat(actual, is(models));
    }
	
	@Test
    public void findById_UserEntryFound_ShouldReturnFoundUserEntry()  {
        User model = TestUtil.getTestUser();model.setId(ID);

        when(mockUserRepository.findOne(ID)).thenReturn(model);

        User actual = userService.findUserByIdOrName(String.valueOf(ID));

        verify(mockUserRepository, times(1)).findOne(ID);
        verifyNoMoreInteractions(mockUserRepository);

        assertThat(actual, is(model));
    }

	@Test(expected = TodoNotFoundException.class)
    public void findById_UserEntryNotFound_ShouldThrowException() {
        when(mockUserRepository.findOne(ID)).thenReturn(null);

        userService.findUserByIdOrName(String.valueOf(ID));

        verify(mockUserRepository, times(1)).findOne(ID);        
        verifyNoMoreInteractions(mockUserRepository);
    }
	
	@Test
    public void findByUserName_UserEntryFound_ShouldReturnFoundUserEntry()  {
        User model = TestUtil.getTestUser();

        when(mockUserRepository.findByUserName(TestUtil.USERNAME)).thenReturn(model);

        User actual = userService.findUserByIdOrName(TestUtil.USERNAME);

        verify(mockUserRepository, times(1)).findByUserName(TestUtil.USERNAME);
        verifyNoMoreInteractions(mockUserRepository);

        assertThat(actual, is(model));
    }

	@Test(expected = TodoNotFoundException.class)
    public void findByUserName_UserEntryNotFound_ShouldThrowException() {
        when(mockUserRepository.findByUserName(TestUtil.USERNAME)).thenReturn(null);

        userService.findUserByIdOrName(TestUtil.USERNAME);

        verify(mockUserRepository, times(1)).findByUserName(TestUtil.USERNAME);        
        verifyNoMoreInteractions(mockUserRepository);
    }
	
	@Test
    public void deleteById_UserEntryFound_ShouldDeleteUserEntryAndReturnIt() {
        User model = TestUtil.getTestUser();model.setId(ID);

        when(mockUserRepository.findOne(ID)).thenReturn(model);

        User actual = userService.deleteUserById(ID);

        verify(mockUserRepository, times(1)).findOne(ID);
        verify(mockUserRepository, times(1)).delete(model.getId());
        
        verifyNoMoreInteractions(mockUserRepository);

        assertThat(actual, is(model));
    }

	@Test(expected = TodoException.class)
    public void deleteById_UserEntryFound_ShouldDeleteFailAndThrowException() {
        User model = TestUtil.getTestUser();model.setId(ID);
         
        when(mockUserRepository.findOne(ID)).thenReturn(model);
        when(mockUserRepository.save(model)).thenReturn(model);
        doThrow(new TodoException()).when(mockUserRepository).delete(model.getId());
        
        userService.deleteUserById(ID);

        verify(mockUserRepository, times(1)).findOne(ID);
        verify(mockUserRepository, times(1)).save(model);
        verify(mockUserRepository, times(1)).delete(model.getId());
        
        verifyNoMoreInteractions(mockUserRepository);
    }
	
    @Test(expected = TodoNotFoundException.class)
    public void deleteById_UserEntryNotFound_ShouldThrowException() {
        when(mockUserRepository.findOne(ID)).thenReturn(null);

        userService.deleteUserById(ID);

        verify(mockUserRepository, times(1)).findOne(ID);
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    public void update_UserEntryFound_ShouldUpdateSuccessfully() throws TodoNotFoundException {
        UserDto dto = TestUtil.getUpdatedTestUserDto();
        
        User model = TestUtil.getTestUser();model.setId(ID);

        when(mockUserRepository.findOne(dto.getId())).thenReturn(model);
        ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
		

        userService.updateUser(ID, dto);

        verify(mockUserRepository, times(1)).findOne(dto.getId());
        verify(mockUserRepository, times(1)).save(userArgument.capture());
        
        verifyNoMoreInteractions(mockUserRepository);

        User actual = userArgument.getValue();
        
        assertThat(actual.getId(), is(dto.getId()));
        assertThat(actual.getUserName(), is(dto.getUserName()));
        assertThat(actual.getFirstName(), is(dto.getFirstName()));
        assertThat(DATEFORMATTER.format(model.getModifiedDate()),is(DATEFORMATTER.format(CURRENT_DATE)));
    }

    @Test(expected = TodoNotFoundException.class)
    public void update_UserEntryNotFound_ShouldThrowException() throws TodoNotFoundException {
    	UserDto dto = TestUtil.getUpdatedTestUserDto();

        when(mockUserRepository.findOne(dto.getId())).thenReturn(null);

        userService.updateUser(ID,dto);

        verify(mockUserRepository, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(mockUserRepository);
    }
}
