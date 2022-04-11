package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String email;

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    private String password;
}
