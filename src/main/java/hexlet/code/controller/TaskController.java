package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public static final String TASK_CONTROLLER_PATH = "/tasks";
    private static final String ID = "/{id}";
    private static final String ONLY_TASK_OWNER_BY_ID = """
        @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Invalid data")
    })
    @PostMapping
    public Task createTask(@Parameter(description = "Task data") @RequestBody @Valid TaskDto taskDto) {
        return taskService.createNewTask(taskDto);
    }

    @Operation(summary = "Get task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping(ID)
    public Task getTaskStatusById(@Parameter(description = "Task id") @PathVariable(name = "id") long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Task"));
    }

    @Operation(summary = "Get filtrated list of all task")
    @ApiResponse(responseCode = "200", description = "Received successfully")
    @GetMapping
    public Iterable<Task> getFiltratedTask(@Parameter(description = "Query predicate for filtration")
                                               @QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

    @Operation(summary = "Change task data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "500", description = "Invalid data")
    })
    @PutMapping(path = ID)
    public Task updateTask(@Parameter(description = "Task id") @PathVariable(name = "id") long id,
                           @Parameter(description = "Task data") @RequestBody @Valid TaskDto taskDto) {
        return taskService.updateExistingTask(id, taskDto);
    }

    @Operation(summary = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping(path = ID)
    @PreAuthorize(ONLY_TASK_OWNER_BY_ID)
    public void deleteTask(@Parameter(description = "Task id") @PathVariable(name = "id") long id) {
        taskRepository.deleteById(id);
    }
}
