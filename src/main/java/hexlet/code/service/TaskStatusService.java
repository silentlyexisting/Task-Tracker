package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;

public interface TaskStatusService {
    TaskStatus createNewTaskStatus(TaskStatusDto taskStatusDto);
    TaskStatus updateExistingTaskStatus(long id, TaskStatusDto taskStatusDto);
}
