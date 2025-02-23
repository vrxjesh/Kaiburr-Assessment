package com.kaiburr.task1.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "tasks") // Marks this as a MongoDB document
public class Task {
    @Id
    private String id;
    private String name;
    private String owner;
    private String command;
    private List<TaskExecution> taskExecutions;

    // Default Constructor
    public Task() {}

    // Parameterized Constructor
    public Task(String id, String name, String owner, String command, List<TaskExecution> taskExecutions) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.command = command;
        this.taskExecutions = taskExecutions;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }

    public List<TaskExecution> getTaskExecutions() { return taskExecutions; }
    public void setTaskExecutions(List<TaskExecution> taskExecutions) { this.taskExecutions = taskExecutions; }
}
