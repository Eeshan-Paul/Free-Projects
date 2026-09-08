package com.todo.backend.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.todo.backend.entity.Task;
import com.todo.backend.repositories.TaskRepo;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> getAllTask() {
        return taskRepo.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepo.findById(id).orElse(null);
    }

    public List<Task> getTaskByTitleOrDesc(String query) {
        return taskRepo.findByTitleOrDescriptionContaining(query);
    }

    public void saveTask(Task task) {
        taskRepo.save(task);
    }

    public void updateTask(Long id, Map<String, Object> fieldsToUpdate) {
        taskRepo.findById(id).ifPresent(existingTask -> {
            fieldsToUpdate.forEach((key, value) -> {
                Field field = org.springframework.util.ReflectionUtils.findField(Task.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    org.springframework.util.ReflectionUtils.setField(field, existingTask, value);
                }
            });
            taskRepo.save(existingTask);
        });
    }

    public void deleteTask(Long id) {
        taskRepo.deleteById(id);
    }
}
