/**
 * 
 */
package com.nice.todolist.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author snyala
 *
 */
@Entity
@Table(name="TODOLIST",schema="TODODB")
@Getter
@Setter
public class TodoList {
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME")
	private String name; 
	
	@Column(name="PURPOSE")
	private String purpose;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "TODOLIST_TASK",schema="TODODB",
	           joinColumns={@JoinColumn(name="todolist_id", referencedColumnName="id")},
	           inverseJoinColumns={@JoinColumn(name="task_id",referencedColumnName="id")})
	private Set<Task> tasks;
}
