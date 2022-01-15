package com.nice.todolist.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nice.todolist.dto.UserDto;
import com.nice.todolist.entities.User;
import com.nice.todolist.services.UserService;

@RestController
@RequestMapping(value="/api/users"
               ,produces=MediaType.APPLICATION_JSON_VALUE)
public class UserController extends BaseController {
    
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService){
		this.userService = userService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createNewUser(@RequestBody @Valid UserDto userDto){
		User addedUser = userService.createUser(userDto);
		UserDto resUserDto = new UserDto();
		BeanUtils.copyProperties(addedUser, resUserDto);
		return resUserDto;
	}

	@GetMapping
	public List<UserDto> getAllUsers(){
		List<UserDto> allUsers = new ArrayList<UserDto>();
		for(User user:userService.getAllUsers()){
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			System.out.println("userDto:"+userDto);
			allUsers.add(userDto);
		}
		return allUsers;
	}

	@GetMapping(value = "/{id}")
    public UserDto getByIdOrName(@PathVariable("id") String idOrUserName) {
		User foundUser = userService.findUserByIdOrName(idOrUserName);
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(foundUser, userDto);
        return userDto;
    }
	
	@PatchMapping(value="/{id}")
    public UserDto updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserDto userRequest) {
		User updatedUser = userService.updateUser(id, userRequest);
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(updatedUser, userDto);
        return userDto;
    }
	
	@DeleteMapping(value="/{id}")
    public UserDto deleteUser(@PathVariable("id") Long id) {        
		User deleted = userService.deleteUserById(id);
		return convertFromUserEntityToDto(deleted);
    }
	
	private UserDto convertFromUserEntityToDto(User user) {
		UserDto userDto = new UserDto();
		
		userDto.setId(user.getId());
		userDto.setUserName(user.getUserName());
		userDto.setFirstName(user.getFirstName());
		userDto.setMiddleName(user.getMiddleName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setCreatedDate(user.getCreatedDate());
		userDto.setModifiedDate(user.getModifiedDate());
		
		return userDto;
	}
}
