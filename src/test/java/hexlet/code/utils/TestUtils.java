package hexlet.code.utils;


import hexlet.code.config.security.component.TokenGenerator;
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

    public static final String FIXTURES_PATH = "src/test/resources/fixtures/";
    public static final String BASE_URL = "/api";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    public MockHttpServletResponse regDefaultUser() throws Exception {
        String json = readFile(FIXTURES_PATH + "defaultUserData.json");
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

    public String readFile(String path) throws IOException {
        return Files.readString(Path.of(path).toAbsolutePath().normalize());
    }
}
