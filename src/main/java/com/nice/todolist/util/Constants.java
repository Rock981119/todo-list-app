/**
 * 
 */
package com.nice.todolist.util;

/**
 * @author nshek
 *
 */
public class Constants {
	
	public static final String REGEX_NUMERIC = "^-?\\d+$";
	public static final String REGEX_USER_NAME = "^[A-Za-z_]\\w{7,29}$";
	
	//User 
	public static final int MIN_LENGTH_USERNAME = 8;
	public static final int MAX_LENGTH_USERNAME = 30;
	public static final int MAX_LENGTH_NAMEPART = 50;
	public static final int MAX_LENGTH_EMAIL = 120;
	public static final int MAX_LENGTH_ROLEID = 5;
	
	//Task
	public static final int MIN_LENGTH_TASKNAME = 10;
	public static final int MAX_LENGTH_TASKNAME = 255;
	public static final int MAX_LENGTH_DESCRIPTION=500;
}
