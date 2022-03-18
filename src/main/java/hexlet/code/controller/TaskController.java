package hexlet.code.controller;

import hexlet.code.dto.TaskDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";
    private static final String ID = "/{id}";

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    private static final String ONLY_TASK_OWNER_BY_ID = """
        @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;

    @PostMapping
    public Task createTask(@RequestBody @Valid TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping(ID)
    public Task getTaskStatusById(@PathVariable(name = "id") long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Task"));
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PutMapping(path = ID)
    public Task updateTask(@PathVariable(name = "id") long id,
                           @RequestBody @Valid TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping(path = ID)
    @PreAuthorize(ONLY_TASK_OWNER_BY_ID)
    public void deleteTask(@PathVariable(name = "id") long id) {
        taskRepository.deleteById(id);
    }
}
