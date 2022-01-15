package com.nice.todolist.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.nice.todolist.repositories.TaskAssignmentKey;

import lombok.Data;

@Data
@Entity
@Table(name="TASK_ASSIGNMENT",schema="TODODB")
public class TaskAssignment {
	
	@EmbeddedId
    private TaskAssignmentKey assignmentKey;

}
