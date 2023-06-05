package com.learn.service;

import com.learn.dto.ProjectDTO;
import com.learn.dto.TaskDTO;
import com.learn.dto.UserDTO;
import com.learn.enums.Status;

import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id);
    List<TaskDTO> listAllTasks();

    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);

    int totalNonCompletedTask(String projectCode);
    int totalCompletedTask(String projectCode);

    void deleteByProject(ProjectDTO projectDTO);

    void completeByProject(ProjectDTO projectDTO);

    List<TaskDTO> listAllTasksByStatusIsNot(Status status);
    List<TaskDTO> listAllTasksByStatus(Status status);

    List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee);

}
