package com.nice.todolist.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nice.todolist.util.JsonDateSerializer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="TASK",schema="TODODB")
public class Task {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="STATUS")
	private String status;
	
	@JsonSerialize(using=JsonDateSerializer.class)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@JsonIgnore
	@ManyToOne
    @JoinTable(name="TASK_ASSIGNMENT",schema="TODODB",
               joinColumns={@JoinColumn(name="task_id", referencedColumnName="id")},
               inverseJoinColumns={@JoinColumn(name="user_id", referencedColumnName="id")})
    private User user;
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "TODOLIST_TASK",schema="TODODB",
	           joinColumns={@JoinColumn(name="task_id", referencedColumnName="id")},
	           inverseJoinColumns={@JoinColumn(name="todolist_id",referencedColumnName="id")})
	private List<TodoList> todoLists;
}
