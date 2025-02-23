package com.kaiburr.task1.demo.controller;

import com.kaiburr.task1.demo.model.Task;
import com.kaiburr.task1.demo.model.TaskExecution;
import com.kaiburr.task1.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable String id) {
        return taskRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable String id, @RequestBody Task updatedTask) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setName(updatedTask.getName());
            task.setOwner(updatedTask.getOwner());
            task.setCommand(updatedTask.getCommand());
            return taskRepository.save(task);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable String id) {
        taskRepository.deleteById(id);
        return "Task Deleted: " + id;
    }

    @GetMapping("/search")
    public List<Task> findTasksByName(@RequestParam String name) {
        return taskRepository.findByNameContainingIgnoreCase(name);
    }

    @PutMapping("/{id}/execute")
    public Task executeTask(@PathVariable String id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            Date startTime = new Date();
            String output = "";

            try {
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("bash", "-c", task.getCommand());
                processBuilder.redirectErrorStream(true);
                
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder outputBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
                process.waitFor();
                output = outputBuilder.toString();
            } catch (Exception e) {
                output = "Error executing command: " + e.getMessage();
            }

            Date endTime = new Date();
            TaskExecution execution = new TaskExecution(startTime, endTime, output);
            task.addTaskExecution(execution);
            return taskRepository.save(task);
        }
        return null;
    }
}
