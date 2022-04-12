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

    //напоминаю проверяющим, что firstName и lastName здесь для того, чтобы прошли тесты,
    //т.к. у вас почему-то по урлу логина приходит json с этими переменными
    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    private String password;
}
