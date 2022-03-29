package hexlet.code.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.config.security.component.TokenGenerator;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static hexlet.code.controller.UserController.USERS_CONTROLLER_PATH;
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@DBRider
@DataSet("dataset.yml")
@Component
public class TestUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String FIXTURES_PATH = "src/test/resources/fixtures/";
    public static final String BASE_URL = "/api";
    public static final String ID = "/{id}";
    public static final String DEFAULT_USER_DATA = "defaultUserData.json";
    public static final String UPDATE_USER_DATA = "updateUserData.json";
    public static final String DEFAULT_TASK_STATUS = "defaultTaskStatus.json";
    public static final String DEFAULT_TASK_DATA = "defaultTaskData.json";
    public static final String UPDATE_TASK_DATA = "updateTaskData.json";
    private static final String DEFAULT_LABEL_DATA = "{\n  \"name\": \"New label\"\n}";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public String readFixturesAsString(String path) throws IOException {
        return Files.readString(Path.of(path).toAbsolutePath().normalize());
    }

    public UserDto getTestRegistrationDto() throws IOException {
        String json = readFixturesAsString(FIXTURES_PATH + DEFAULT_USER_DATA);
        return MAPPER.readValue(json, UserDto.class);
    }

    public void regDefaultLabel() throws Exception {
        final User user = userRepository.findAll().get(0);
        MockHttpServletResponse response = perform(
                post(BASE_URL + LABEL_CONTROLLER_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(DEFAULT_LABEL_DATA),
                user.getEmail()
        ).andReturn().getResponse();
    }

    public void regDefaultTask() throws Exception {
        String json = readFixturesAsString(FIXTURES_PATH + DEFAULT_TASK_DATA);
        final User user = userRepository.findAll().get(0);
        MockHttpServletResponse response = perform(
                post(BASE_URL + TASK_CONTROLLER_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(json),
                user.getEmail()
        ).andReturn().getResponse();
    }

    public void regDefaultTaskStatus() throws Exception {
        String json = readFixturesAsString(FIXTURES_PATH + DEFAULT_TASK_STATUS);
        final User user = userRepository.findAll().get(0);
        MockHttpServletResponse response =  perform(
                post(BASE_URL + TASK_STATUS_CONTROLLER_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(json),
                user.getEmail()
        ).andReturn().getResponse();
    }

    public MockHttpServletResponse regDefaultUser() throws Exception {
        String json = readFixturesAsString(FIXTURES_PATH + DEFAULT_USER_DATA);
        return perform(
                post(BASE_URL + USERS_CONTROLLER_PATH)
                        .content(json)
                        .contentType(APPLICATION_JSON)
        ).andReturn().getResponse();
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = tokenGenerator.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);
        return perform(request);
    }
}
