package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.exception.UserNotFoundException;
import hexlet.code.model.User;

public interface UserService {
    User createUser(UserDto userDto);
    User findUserById(long id) throws UserNotFoundException;
}
