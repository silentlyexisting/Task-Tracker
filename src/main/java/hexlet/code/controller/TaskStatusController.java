package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import lombok.AllArgsConstructor;
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

import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + TASK_STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    public static final String TASK_STATUS_CONTROLLER_PATH = "/statuses";
    private static final String ID = "/{id}";

    private final TaskStatusService taskStatusService;
    private final TaskStatusRepository taskStatusRepository;

//    private static final String ONLY_OWNER_BY_ID = """
//            @userRepository.findById(#id).get().getEmail() == authentication.getName()
//        """;

    @PostMapping
    public TaskStatus createTaskStatus(@RequestBody @Valid TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @GetMapping(path = ID)
    public TaskStatus getTaskStatus(@PathVariable(name = "id") long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(id));
    }

    @GetMapping
    public List<TaskStatus> getTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @PutMapping(path = ID)
//    @PreAuthorize(ONLY_OWNER_BY_ID)
    public TaskStatus updateTaskStatus(@PathVariable(name = "id") long id,
                                       @RequestBody @Valid TaskStatusDto taskStatusDto) {
        return taskStatusService.updateTaskStatus(id, taskStatusDto);
    }

    @DeleteMapping(path = ID)
//    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteTaskStatus(@PathVariable(name = "id") long id) {
        taskStatusRepository.deleteById(id);
    }

}
