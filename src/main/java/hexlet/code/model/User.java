package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Email(message = "Email format is incorrect")
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 1, message = "First name must contain at least 1 character")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "Last name must contain at least 1 characters")
    private String lastName;

    @NotNull
    @JsonIgnore
    @Size(min = 3, message = "Password must contain at least 3 characters")
    private String password;

    @CreationTimestamp
    private Date createdAt;

    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}

