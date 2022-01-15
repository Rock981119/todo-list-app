package com.nice.todolist.util;

public enum TaskStatus {
	NOT_STARTED("NS"),
	IN_PROGRESS("IP"),
	COMPLETE("CO");
	
	private String statusCode;
	
	private TaskStatus(String statusCode){
		this.statusCode=statusCode;
	}
	
	public String getValue(){
		return this.statusCode;
	}
	
	public static TaskStatus fromString(final String statusValue){
		switch(statusValue){
		  case "NS": return NOT_STARTED;
		  case "IP": return IN_PROGRESS;
		  case "CO": return COMPLETE;
		  default: throw new IllegalArgumentException("Invalid Status Code. Valid status codes are: \n 1.\"NS\" for 'Not Started',"
		  		                                                                       +"\n 2.\"IP\" for 'In Progress',"
		  		                                                                       +"\n 3.\"CO\" for Complete");
		}
	}
}
