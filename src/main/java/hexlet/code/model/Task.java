package hexlet.code.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 1, message = "Task name must contain at least 1 character")
    private String name;

    @Nullable
    private String description;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private TaskStatus taskStatus;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authorId")
    private User author;

    @Nullable
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "executorId")
    private User executor;

    @Nullable
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Label> labels;

    @CreationTimestamp
    private Date createdAt;

    public Task(String name,
                @Nullable String description,
                TaskStatus taskStatus,
                User author,
                @Nullable User executor) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.author = author;
        this.executor = executor;
    }
}
