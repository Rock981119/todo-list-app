/**
 * 
 */
package com.nice.todolist.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nice.todolist.dto.TaskDto;
import com.nice.todolist.entities.Task;
import com.nice.todolist.entities.User;
import com.nice.todolist.exception.BadRequestException;
import com.nice.todolist.exception.TodoException;
import com.nice.todolist.exception.TodoNotFoundException;
import com.nice.todolist.model.TaskAssignmentRequest;
import com.nice.todolist.model.TaskAssignmentResponse;
import com.nice.todolist.repositories.TaskAssignmentRepository;
import com.nice.todolist.repositories.TaskRepository;
import com.nice.todolist.repositories.UserRepository;
import com.nice.todolist.services.TaskAssignmentService;
import com.nice.todolist.services.TaskService;
import com.nice.todolist.util.PropertyUtil;
import com.nice.todolist.util.TaskStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * @author nshek
 *
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService,TaskAssignmentService {

    private TaskRepository taskRepository;
    
    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;
    
    @Autowired
    private UserRepository userRepository;
	
	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository){
		this.taskRepository = taskRepository;
	}
	
	@Transactional
	@Override
	public Task createTask(TaskDto added) {		
		Task taskEntity = new Task();
		if(Objects.isNull(added) || Objects.isNull(added.getName())||added.getName().isEmpty()){
			throw new BadRequestException("taskName may not be empty while adding it.");
		}
		BeanUtils.copyProperties(added, taskEntity);
		taskEntity.setStatus(TaskStatus.NOT_STARTED.getValue());
		taskEntity.setCreatedDate(new Date());
        return taskRepository.save(taskEntity);
	}

	@Transactional(rollbackFor = {TodoNotFoundException.class})
	@Override
	public Task deleteById(Long id) throws TodoNotFoundException {
		Task deleted = taskRepository.fetchByTaskId(id);
		log.debug("Found the task entry: {}", deleted);
		
		if(Objects.isNull(deleted)){
		   throw new TodoNotFoundException("No record found with given id:"+id+".Please verify and retry again." );
		}
		
		try{
			/*if(Objects.nonNull(deleted.getUser())){
				taskAssignmentRepository.deleteByTaskId(deleted.getId());
			}*/
			taskRepository.delete(deleted.getId());
		}catch(Exception exp){
			throw new TodoException("Could not able to remove the user. Reason:"+exp.getMessage());
		}
		return deleted;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	@Override
	public Task findById(Long id) throws TodoNotFoundException {
		Task found = null;
		
		found = taskRepository.fetchByTaskId(id);
		log.debug("Got the user entry by Id: {}", found);
				
		if(Objects.isNull(found)){
			throw new TodoNotFoundException("No data exists for the given userId:"+id);
		}
		return found;
	}

	@Transactional(rollbackFor = {TodoNotFoundException.class})
	@Override
	public Task updateTask(TaskDto updatedDto) {
		String status = updatedDto.getStatus();		
		if(!StringUtils.isEmpty(status)){
			TaskStatus statusEnum = TaskStatus.fromString(status);
			updatedDto.setStatus(statusEnum.getValue());
			log.debug("Status will be changed to: {}", statusEnum.getValue());
		}
		Task found = taskRepository.fetchByTaskId(updatedDto.getId());
		log.debug("Found the task entry: {}", found);
		
		if(Objects.isNull(found)){
			throw new TodoNotFoundException("No data exists with given userId:"+updatedDto.getId());
		}
		
		BeanUtils.copyProperties(updatedDto, found,PropertyUtil.getNullPropertiesString(updatedDto));
        return taskRepository.save(found);
	}

	@Override
	public List<Task> findTasksByStatus(String statusCode) {
		log.debug("status value received from client:{}",statusCode);
		String trimmedStatus = statusCode.replace("\"", "");
		TaskStatus.fromString(trimmedStatus);
		log.debug("retrieving tasks with:{} status",trimmedStatus);
		
		List<Task> tasks = taskRepository.findByStatus(trimmedStatus);
		if(Objects.isNull(tasks) || tasks.isEmpty()){
			throw new TodoNotFoundException("No tasks found with given status:"+trimmedStatus);
		}
		return tasks;
	}

	@Override
	public TaskAssignmentResponse assignTask(TaskAssignmentRequest assignmentRequest) {
		Task task = taskRepository.fetchByTaskId(assignmentRequest.getTaskId());
		log.debug("task entry for assignment:{}", task);
		
		User user = userRepository.findOne(assignmentRequest.getUserId());
		log.debug("user entry for assignment:{}", user);
		
		if(Objects.isNull(task)){
			throw new TodoNotFoundException("No task found with given id:"+assignmentRequest.getTaskId()+".Task must exists for an assigning to user.");
		}
		
		if(Objects.isNull(user)){
			throw new TodoNotFoundException("No user found with given id:"+assignmentRequest.getUserId()+".User must exists for working on the task.");
		}
		task.setUser(user);
		
		log.debug("adding the task assignment:{}",assignmentRequest);
		taskRepository.save(task);
		
		TaskAssignmentResponse assignmentResponse = new TaskAssignmentResponse();
		assignmentResponse.setTaskId(task.getId());
		assignmentResponse.setTaskName(task.getName());
		assignmentResponse.setUserId(user.getId());
		assignmentResponse.setUserName(user.getUserName());
		assignmentResponse.setFirstName(user.getFirstName());
		assignmentResponse.setLastName(user.getLastName());
		
		return assignmentResponse;
	}

	@Override
	public List<TaskAssignmentResponse> getAllTasksWithAssignedUsers() {
		List<Task> tasks = getAllTasks();
		if(Objects.isNull(tasks) || tasks.isEmpty()){
			throw new TodoNotFoundException("There are no task exists in the system.");
		}
		return prepareTaskAssignment(tasks);
	}

	@Override
	public List<TaskAssignmentResponse> getCompletedTasksWithAssignedUsers() {
		List<Task> tasks = taskRepository.findByStatus(TaskStatus.COMPLETE.getValue());
		if(Objects.isNull(tasks) || tasks.isEmpty()){
			throw new TodoNotFoundException("There are no completed tasks.");
		}
		return prepareTaskAssignment(tasks);
	}

	private List<TaskAssignmentResponse> prepareTaskAssignment(List<Task> tasks) {
		List<TaskAssignmentResponse> taskAssignmentList = new ArrayList<TaskAssignmentResponse>();
		for(Task task:tasks){
			if(Objects.nonNull(task.getUser())){
				TaskAssignmentResponse assignmentResponse = new TaskAssignmentResponse();
				assignmentResponse.setTaskId(task.getId());
				assignmentResponse.setTaskName(task.getName());
				assignmentResponse.setUserId(task.getUser().getId());
				assignmentResponse.setUserName(task.getUser().getUserName());
				assignmentResponse.setFirstName(task.getUser().getFirstName());
				assignmentResponse.setLastName(task.getUser().getLastName());
				taskAssignmentList.add(assignmentResponse);
			}
		}
		return taskAssignmentList;
	}

	@Override
	public List<TaskAssignmentResponse> getNotCompletedTasksWithAssignedUsers() {
		List<Task> tasks = taskRepository.findByStatusNotLike(TaskStatus.COMPLETE.getValue());
		if(Objects.isNull(tasks) || tasks.isEmpty()){
			throw new TodoNotFoundException("There are no completed tasks.");
		}
		return prepareTaskAssignment(tasks);
	}

	@Override
	public List<TaskAssignmentResponse> getInProgressTasksWithAssignedUsers() {
		List<Task> tasks = taskRepository.findByStatus(TaskStatus.IN_PROGRESS.getValue());
		if(Objects.isNull(tasks) || tasks.isEmpty()){
			throw new TodoNotFoundException("There are no inprogress tasks.");
		}
		return prepareTaskAssignment(tasks);
	}

	@Override
	public List<TaskAssignmentResponse> getNotStartedTasksWithAssignedUsers() {
		List<Task> tasks = taskRepository.findByStatus(TaskStatus.NOT_STARTED.getValue());
		if(Objects.isNull(tasks) || tasks.isEmpty()){
			throw new TodoNotFoundException("None of the tasks found with not-started status.");
		}
		return prepareTaskAssignment(tasks);
	}

}
