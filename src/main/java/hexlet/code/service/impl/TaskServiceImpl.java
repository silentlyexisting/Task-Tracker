package hexlet.code.service.impl;

import hexlet.code.dto.TaskDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.TaskService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final UserService userService;

    @Override
    public Task createNewTask(final TaskDto taskDto) {
        final Task newTask = new Task(
                taskDto.getName(),
                taskDto.getDescription(),
                getTaskStatusForTaskCreation(taskDto),
                userService.getCurrentUser(),
                getExecutorForTaskCreation(taskDto)
        );

        if (taskDto.getLabelIds() != null) {
            newTask.setLabels(getLabelsForTaskCreation(taskDto));
        }

        return taskRepository.save(newTask);
    }

    @Override
    public Task updateExistingTask(final long id, final TaskDto taskDto) {
        final Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Task"));

        taskToUpdate.setName(taskDto.getName());
        taskToUpdate.setDescription(taskDto.getDescription());
        taskToUpdate.setTaskStatus(getTaskStatusForTaskCreation(taskDto));
        taskToUpdate.setAuthor(userService.getCurrentUser());
        taskToUpdate.setExecutor(getExecutorForTaskCreation(taskDto));

        if (taskDto.getLabelIds() != null) {
            taskToUpdate.setLabels(getLabelsForTaskCreation(taskDto));
        }

        return taskRepository.save(taskToUpdate);
    }

    private List<Label> getLabelsForTaskCreation(final TaskDto taskDto) {
        return taskDto.getLabelIds().stream().map(id -> labelRepository.findById(id).get())
                .collect(Collectors.toList());
    }


    private TaskStatus getTaskStatusForTaskCreation(final TaskDto taskDto) {
        return taskStatusRepository.findById(taskDto.getTaskStatusId()).get();
    }

    private User getExecutorForTaskCreation(final TaskDto taskDto) {
        return userRepository.findById(taskDto.getExecutorId()).get();
    }
}
