package hexlet.code.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.config.security.component.TokenGenerator;
import hexlet.code.dto.UserDto;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Component
public class TestUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String FIXTURES_PATH = "src/test/resources/fixtures/";
    public static final String BASE_URL = "/api";
    public static final String DEFAULT_USER_DATA = "defaultUserData.json";
    public static final String UPDATE_USER_DATA = "updateUserData.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    public String readFileContent(String path) throws IOException {
        return Files.readString(Path.of(path).toAbsolutePath().normalize());
    }

    public UserDto getTestRegistrationDto() throws IOException {
        String json = readFileContent(FIXTURES_PATH + DEFAULT_USER_DATA);
        return MAPPER.readValue(json, UserDto.class);
    }

    public MockHttpServletResponse regDefaultUser() throws Exception {
        String json = readFileContent(FIXTURES_PATH + DEFAULT_USER_DATA);
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
