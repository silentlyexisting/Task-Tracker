package hexlet.code.controller;

import com.rollbar.notifier.Rollbar;
import hexlet.code.dto.UserDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.UserController.USERS_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + USERS_CONTROLLER_PATH)
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final Rollbar rollbar;

    public static final String USERS_CONTROLLER_PATH = "/users";
    private static final String ID = "/{id}";
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created successfully"),
            @ApiResponse(responseCode = "500", description = "Invalid data")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createUser(@Parameter(description = "User data") @RequestBody @Valid UserDto userDto) {
        rollbar.debug("Here is some debug registration message ");
        return userService.createNewUser(userDto);
    }

    @Operation(summary = "Get user by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(path = ID)
    public User getUser(@Parameter(description = "User id") @PathVariable(name = "id") long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("User"));
    }

    @Operation(summary = "Get list of all users")
    @ApiResponse(responseCode = "200", description = "Received successfully")
    @GetMapping
    public List<User> getUsers() {
        rollbar.debug("Here is some debug all users message");
        return userRepository.findAll();
    }

    @Operation(summary = "Change user data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changer successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Invalid data")
    })
    @PutMapping(path = ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public User updateUser(@Parameter(description = "User id") @PathVariable long id,
                           @Parameter(description = "User data") @RequestBody @Valid UserDto userDto) {
        return userService.updateExistingUser(id, userDto);
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping(path = ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void deleteUser(@Parameter(description = "User id") @PathVariable long id) {
        userRepository.deleteById(id);
    }
}
