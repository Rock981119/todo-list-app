package com.nice.todolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nice.todolist.entities.TodoList;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
	
}
