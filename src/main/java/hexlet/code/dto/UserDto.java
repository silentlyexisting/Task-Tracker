package hexlet.code.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Email(message = "Email format is incorrect")
    private String email;

    @NotNull
    @Size(min = 1, message = "First name must contain at least 1 character")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "Last name must contain at least 1 characters")
    private String lastName;

    @NotNull
    @Size(min = 3, message = "Password must contain at least 3 characters")
    private String password;
}
