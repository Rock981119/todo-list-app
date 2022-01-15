/**
 * 
 */
package com.nice.todolist.services;

import java.util.List;

import com.nice.todolist.dto.UserDto;
import com.nice.todolist.entities.User;

/**
 * @author nshek
 *
 */
public interface UserService {

	public User createUser(UserDto userDto);
	public List<User> getAllUsers();
	public User findUserByIdOrName(String idOrName);
	public User updateUser(Long userId, UserDto updatedUserDto);
	public User deleteUserById(Long userId);
}
