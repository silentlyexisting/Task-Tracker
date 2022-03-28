package hexlet.code.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TaskDto {

    @NotNull
    @Size(min = 1, message = "Task name must contain at least 1 character")
    private String name;

    private String description;

    @NotNull
    private long taskStatusId;

    private long executorId;

    private List<Long> labelIds;
}
