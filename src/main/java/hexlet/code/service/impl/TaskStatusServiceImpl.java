package hexlet.code.service.impl;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus createNewTaskStatus(TaskStatusDto taskStatusDto) {
        return taskStatusRepository.save(new TaskStatus(
                taskStatusDto.getName()
        ));
    }


    @Override
    public TaskStatus updateExistingTaskStatus(long id, TaskStatusDto taskStatusDto) {
        TaskStatus taskStatusToUpdate = taskStatusRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Task Status"));

        taskStatusToUpdate.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatusToUpdate);
    }
}
