package com.learn.controller;

import com.learn.dto.ResponseWrapper;
import com.learn.dto.TaskDTO;
import com.learn.enums.Status;
import com.learn.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getTasks(){
        return ResponseEntity.ok(new ResponseWrapper("Tasks are successfully retrieved", taskService.listAllTasks(), HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable ("id") Long id){
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully retrieved", taskService.findById(id), HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO){
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Task is successfully created",  HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable ("id") Long id){
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully deleted", HttpStatus.OK));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully updated", HttpStatus.OK));
    }

    @GetMapping("/employee/pending-tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTasks(){
        return ResponseEntity.ok(new ResponseWrapper("Pending Tasks are successfully retrieved", taskService.listAllTasksByStatusIsNot(Status.COMPLETE),HttpStatus.OK));
    }

    @PutMapping("/employee/update")
    public ResponseEntity<ResponseWrapper> employeeUpdateTasks(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task is successfully updated", HttpStatus.OK));
    }

    @GetMapping("/employee/archive")
    public ResponseEntity<ResponseWrapper> employeeArchivedTasks(){
        return ResponseEntity.ok(new ResponseWrapper("Archived Tasks are successfully retrieved", taskService.listAllTasksByStatus(Status.COMPLETE),HttpStatus.OK));
    }
}
