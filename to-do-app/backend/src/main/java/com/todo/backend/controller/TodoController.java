package com.todo.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.backend.dto.GenericResponse;
import com.todo.backend.entity.Task;
import com.todo.backend.service.TaskService;


@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TaskService taskService;

    public TodoController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/getAllTasks")
    public ResponseEntity<GenericResponse<List<Task>>> getExistingTasks() {
        List<Task> tasks = taskService.getAllTask();

        GenericResponse<List<Task>> response = new GenericResponse<>();
        if (tasks.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("No tasks found!");
        } else {
            response.setSuccess(true);
            response.setMessage("Tasks fetched successfully!");
            response.setData(tasks);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getTask")
    public ResponseEntity<GenericResponse<Task>> getTaskById(@RequestParam Long id) {
        Task task = taskService.getTaskById(id);
        GenericResponse<Task> response = new GenericResponse<>();

        if (task == null) {
            response.setSuccess(false);
            response.setMessage("No task found");
        } else {
            response.setSuccess(true);
            response.setMessage("Task fetched successfully");
            response.setData(task);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/saveTask")
    public ResponseEntity<GenericResponse<Task>> saveTask(@RequestBody Task task) {
        taskService.saveTask(task);

        GenericResponse<Task> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setMessage("Task saved successfully");
        response.setData(task);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/updateTaskById/{id}")
    public ResponseEntity<GenericResponse<Task>> updateTaskById(@PathVariable Long id,
            @RequestBody Map<String, Object> fieldsToUpdate) {
        taskService.updateTask(id, fieldsToUpdate);

        Task updatedTask = taskService.getTaskById(id);
        GenericResponse<Task> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setMessage("Task updated successfully");
        response.setData(updatedTask);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);

        GenericResponse<Void> response = new GenericResponse<>();
        response.setSuccess(true);
        response.setMessage("Task deleted successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getTaskByTitleOrDesc")
    public ResponseEntity<GenericResponse<List<Task>>> getTaskByTitleOrDesc(@RequestParam String query) {
        List<Task> tasks = taskService.getTaskByTitleOrDesc(query);

        GenericResponse<List<Task>> response = new GenericResponse<>();
        if (tasks == null || tasks.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("No matching tasks found");
        } else {
            response.setSuccess(true);
            response.setMessage("Matching tasks fetched successfully");
            response.setData(tasks);
        }

        return ResponseEntity.ok(response);
    }
}
