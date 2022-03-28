package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import org.webjars.NotFoundException;

public interface UserService {
    User createNewUser(UserDto userDto);
    User updateExistingUser(long id, UserDto dto);
    User getCurrentUser() throws NotFoundException;
}
