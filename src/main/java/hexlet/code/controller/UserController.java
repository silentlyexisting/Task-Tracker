package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.UserController.USERS_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + USERS_CONTROLLER_PATH)
public class UserController {

    public static final String USERS_CONTROLLER_PATH = "/users";
    private static final String ID = "/{id}";


    private final UserService userService;
    private final UserRepository userRepository;

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    @PostMapping
    public User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.createNewUser(userDto);
    }

    @GetMapping(path = ID)
    public User getUser(@PathVariable(name = "id") long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("User"));
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PutMapping(path = ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public User updateUser(@PathVariable long id,
                           @RequestBody @Valid UserDto userDto) {
        return userService.updateExistingUser(id, userDto);
    }

    @DeleteMapping(path = ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
    }
}
