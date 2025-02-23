package com.kaiburr.task1.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.kaiburr.task1.demo.model.Task;
import com.kaiburr.task1.demo.model.TaskExecution;
import com.kaiburr.task1.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // GET /tasks - Retrieve all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // GET /tasks/{id} - Retrieve a task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /tasks - Create a new task
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    // PUT /tasks/{id} - Update a task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task updatedTask) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedTask.setId(id);
        return ResponseEntity.ok(taskRepository.save(updatedTask));
    }

    // DELETE /tasks/{id} - Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok("Task Deleted: " + id);
    }

    // GET /tasks/search?name=xyz - Search tasks by name
    @GetMapping("/search")
    public List<Task> searchTasksByName(@RequestParam String name) {
        return taskRepository.findByNameContainingIgnoreCase(name);
    }

    // PUT /tasks/{id}/execute - Execute a task command and store execution details
    @PutMapping("/{id}/execute")
    public ResponseEntity<String> executeTask(@PathVariable String id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = taskOptional.get();
        TaskExecution execution = new TaskExecution(new Date(), new Date(), "Executed: " + task.getCommand());
        task.getTaskExecutions().add(execution);

        taskRepository.save(task);
        return ResponseEntity.ok("Task Executed: " + task.getCommand());
    }
}
