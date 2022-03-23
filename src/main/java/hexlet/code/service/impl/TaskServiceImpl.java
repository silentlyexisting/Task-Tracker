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
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    @Override
    public Task createTask(TaskDto taskDto) {
        Task task = new Task(
                taskDto.getName(),
                taskDto.getDescription(),
                getTaskStatus(taskDto),
                getAuthorAsCurrentUser(),
                getExecutor(taskDto)
        );

        if (taskDto.getLabels() != null) {
            task.setLabels(getLabels(taskDto));
        }

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(long id, TaskDto taskDto) {
        Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Task"));

        taskToUpdate.setName(taskDto.getName());
        taskToUpdate.setDescription(taskDto.getDescription());
        taskToUpdate.setTaskStatus(getTaskStatus(taskDto));
        taskToUpdate.setAuthor(getAuthorAsCurrentUser());
        taskToUpdate.setExecutor(getExecutor(taskDto));

        return taskRepository.save(taskToUpdate);
    }

    private List<Label> getLabels(TaskDto taskDto) {
        return taskDto.getLabels().stream().map(id -> labelRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Label")))
                .collect(Collectors.toList());
    }


    private TaskStatus getTaskStatus(TaskDto taskDto) {
        return taskStatusRepository.findById(taskDto.getTaskStatusId())
                .orElseThrow(() -> new CustomNotFoundException("Task Status"));
    }

    private User getAuthorAsCurrentUser() {
        final var username = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(String.valueOf(username))
                .orElseThrow(() -> new CustomNotFoundException("User (Author)"));
    }

    private User getExecutor(TaskDto taskDto) {
        return userRepository.findById(taskDto.getExecutorId())
                .orElseThrow(() -> new CustomNotFoundException("User (Executor)"));
    }


}
