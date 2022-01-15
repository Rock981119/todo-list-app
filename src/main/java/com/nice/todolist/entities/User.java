package com.nice.todolist.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="USER",schema="TODODB")
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="MIDDLE_NAME")
	private String middleName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="EMAIL", nullable = true)
	private String email;
	
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@JsonIgnore
	@OneToMany
	@JoinTable(name="TASK_ASSIGNMENT",schema="TODODB",
	           joinColumns={@JoinColumn(name="user_id",referencedColumnName="id")},
	           inverseJoinColumns={@JoinColumn(name="task_id",referencedColumnName="id")})	
	private Set<Task> assignedTasks;
	
	public static Builder getBuilder(String userName) {
        return new Builder(userName);
    }
	
	public static class Builder {

        private User built;
        
        public Builder(String userName) {
            built = new User();
            built.userName = userName;
        }
        
        public User build() {
            return built;
        }
        
        public Builder firstName(String firstName) {
            built.firstName = firstName;
            return this;
        }
        
        public Builder middleName(String middleName) {
            built.middleName = middleName;
            return this;
        }
        
        public Builder lastName(String lastName) {
            built.lastName = lastName;
            return this;
        }
        
        public Builder email(String email) {
            built.email = email;
            return this;
        }
        
        public Builder createdDate(Date createdDate) {
            built.createdDate = createdDate;
            return this;
        }
        
        public Builder modifiedDate(Date modifiedDate) {
            built.modifiedDate = modifiedDate;
            return this;
        }
	}
}
