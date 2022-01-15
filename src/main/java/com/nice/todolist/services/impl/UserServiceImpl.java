package com.nice.todolist.services.impl;

import static com.nice.todolist.util.Constants.REGEX_NUMERIC;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nice.todolist.dto.UserDto;
import com.nice.todolist.entities.User;
import com.nice.todolist.exception.TodoException;
import com.nice.todolist.exception.TodoNotFoundException;
import com.nice.todolist.repositories.TaskAssignmentRepository;
import com.nice.todolist.repositories.UserRepository;
import com.nice.todolist.services.UserService;
import com.nice.todolist.util.PropertyUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	@Autowired
    private TaskAssignmentRepository taskAssignmentRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	@Transactional
	public User createUser(UserDto userDto) {
		User userEntity = new User();
		BeanUtils.copyProperties(userDto, userEntity);
		userEntity.setCreatedDate(new Date());
        return userRepository.save(userEntity);
	}
	
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
        return userRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public User findUserByIdOrName(String idOrName) throws TodoException{
		User found = null;
		
		if(idOrName.matches(REGEX_NUMERIC)){
			found = userRepository.findOne(Long.parseLong(idOrName));
			log.debug("Got the user entry by Id: {}", found);
		}else{
			found = userRepository.findByUserName(idOrName);
			log.debug("Got the user entry by userName: {}", found);
		}
		
		if(Objects.isNull(found)){
			throw new TodoNotFoundException("No data exists for the given userId:"+idOrName);
		}
		return found;
	}
	
	@Transactional(rollbackFor = {TodoNotFoundException.class})
	public User updateUser(Long userId, UserDto updatedUserDto) {
		User user = userRepository.findOne(userId);
		log.debug("Found the user entry: {}", user);
		
		if(Objects.isNull(user)){
			throw new TodoNotFoundException("No data exists with given userId:"+userId);
		}
		
		BeanUtils.copyProperties(updatedUserDto, user,PropertyUtil.getNullPropertiesString(updatedUserDto));
		user.setModifiedDate(new Date());
        return userRepository.save(user);
	}
	
	@Transactional(rollbackFor = {TodoNotFoundException.class})
	public User deleteUserById(Long userId) {
		User deleted = userRepository.findOne(userId);
		log.debug("Found the user entry: {}", deleted);
		
		if(Objects.isNull(deleted)){
		   throw new TodoNotFoundException("No record found with given id:"+userId+".Please verify and retry again." );
		}
		
		try{/*
			if(Objects.nonNull(deleted.getAssignedTasks()) && !deleted.getAssignedTasks().isEmpty()){
				taskAssignmentRepository.deleteByUserId(userId);
			}*/
			
			userRepository.delete(deleted.getId());
		}catch(DataIntegrityViolationException dive){
			throw new TodoException("The user appears to be having some active assigned tasks. Please either mark as complete or delete the assigned tasks "
					+ "before attempting to deleting the user.");
		}catch(Exception exp){
			throw new TodoException("Could not able to remove the user. Reason:"+exp.getMessage());
		}
		return deleted;
	}
}
