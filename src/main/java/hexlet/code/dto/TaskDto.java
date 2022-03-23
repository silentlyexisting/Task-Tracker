package hexlet.code.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

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

    @Nullable
    private String description;

    @NotNull
    private long taskStatusId;

    @Nullable
    private long executorId;

    @Nullable
    private List<Long> labels;
}
