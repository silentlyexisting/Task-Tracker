package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    private final TaskStatusService taskStatusService;
    private final TaskStatusRepository taskStatusRepository;

    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    private static final String ID = "/{id}";

    @Operation(summary = "Create new task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Invalid data")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TaskStatus createTaskStatus(@Parameter(description = "Task status data")
                                           @RequestBody @Valid TaskStatusDto taskStatusDto) {
        return taskStatusService.createNewTaskStatus(taskStatusDto);
    }

    @Operation(summary = "Get task status by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status found"),
            @ApiResponse(responseCode = "404", description = "Task status not found")
    })
    @GetMapping(path = ID)
    public TaskStatus getTaskStatus(@Parameter(description = "Task status id") @PathVariable(name = "id") long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Task Status"));
    }

    @Operation(summary = "Get list of all task statuses")
    @ApiResponse(responseCode = "200", description = "Received successfully")
    @GetMapping
    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @Operation(summary = "Change task status data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Task status not found"),
            @ApiResponse(responseCode = "500", description = "Invalid data")
    })
    @PutMapping(path = ID)
    public TaskStatus updateTaskStatus(@Parameter(description = "Task status id") @PathVariable(name = "id") long id,
                                       @Parameter(description = "Task status data")
                                       @RequestBody @Valid TaskStatusDto taskStatusDto) {
        return taskStatusService.updateExistingTaskStatus(id, taskStatusDto);
    }

    @Operation(summary = "Delete task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Task status not found")
    })
    @DeleteMapping(path = ID)
    public void deleteTaskStatus(@Parameter(description = "Task status id") @PathVariable(name = "id") long id) {
        taskStatusRepository.deleteById(id);
    }

}
