ToDo List Application
=====================

This is a starter project for Java developer candidate testing.

Overview
--------

This is a web application with REST endpoints for managing a todo list (a list of tasks assigned to users). The application is set up to use Maven, Spring Boot, Hibernate, and an embedded H2 database.

Your job is to finish the application according to the user stories defined below. Please test your solution as you think best. Please ask as many questions as you think is necessary.


User Stories
------------

The user of your application, the "todo list manager", is responsible for creating users, creating tasks, assigning tasks to users, and setting task statuses using a collection of REST endpoints. The todo list manager expects all REST endpoints to return data in JSON format.

1. As a todo list manager, I need a REST endpoint to create new users.
2. As a todo list manager, I need a REST endpoint to change an existing user's user name.
3. As a todo list manager, I need a REST endpoint to delete a user.
4. As a todo list manager, I need a REST endpoint to list all of the users.
5. As a todo list manager, I need a REST endpoint to create a new task. Tasks have a name, description, status, and assigned user. Task statuses should include "Not Started", "In Progress", and "Complete".
6. As a todo list manager, I need a REST endpoint to update a task.
7. As a todo list manager, I need a REST endpoint to list all tasks along with their assigned users.
8. As a todo list manager, I need a REST endpoint to list all completed tasks along with their assigned users.
9. As a todo list manager, I need a REST endpoint to list all tasks that are not completed along with their assigned users.
10. As a todo list manager, I need a REST endpoint to list all tasks that are in progress along with their assigned users.
11. As a todo list manager, I need a REST endpoint to list all tasks that are not started along with their assigned users.

Bonus Stories
-------------

The following user stories are considered to be bonus functionality and are not strictly required.

1. As a todo list manager, I need to be able to create multiple separate todo lists. A todo list should have multiple tasks, tasks can belong to multiple todo lists, and tasks are still assigned to a single user.
2. As a todo list manager, I need a REST endpoint to list all tasks for a particular todo list.
3. As a todo list manager, I need a REST endpoint to list all tasks grouped by todo list.
4. As a todo list manager, I need a REST endpoint to delete todo lists.


Getting Started
---------------

* To build the project, run `mvn package`.
* To run the application, use the provided `startup.sh` script or run the project's jar file directly. The server will start on localhost on port 8080.
* The embedded H2 database will be automatically configured based on your entities when the application starts up.
* Feel free to make any modifications to the existing code that you see fit so long as you continue to use Maven, Spring Boot, and Hibernate.
