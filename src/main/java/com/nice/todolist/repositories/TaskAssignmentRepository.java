package com.nice.todolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nice.todolist.entities.TaskAssignment;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, TaskAssignmentKey> {
	
	@Modifying
	@Query("delete from TaskAssignment o where o.assignmentKey.taskId=?1")
	public void deleteByTaskId(Long taskId);
	
	@Modifying
	@Query("delete from TaskAssignment o where o.assignmentKey.userId=?1")
	public void deleteByUserId(Long userId);
}
