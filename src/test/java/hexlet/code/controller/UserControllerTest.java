package hexlet.code.controller;

import hexlet.code.dto.LoginDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import static hexlet.code.config.security.SecurityConfig.LOGIN;
import static hexlet.code.utils.TestUtils.UPDATE_USER_DATA;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static hexlet.code.controller.UserController.USERS_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.ID;

import javax.transaction.Transactional;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils utils;

    private static String updateUserJson;

    @BeforeAll
    void initialization() throws IOException {
        updateUserJson = utils.readFileContent(TestUtils.FIXTURES_PATH + UPDATE_USER_DATA);
    }

    @Test
    public void registrationUserTest() throws Exception {
        assertThat(userRepository.count()).isEqualTo(0);
        final var request = utils.regDefaultUser();
        assertThat(request.getStatus()).isEqualTo(200);
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void loginTest() throws Exception {
        utils.regDefaultUser();

        final LoginDto loginDto = new LoginDto(
                utils.getTestRegistrationDto().getEmail(),
                utils.getTestRegistrationDto().getPassword()
        );

        String json = utils.MAPPER.writeValueAsString(loginDto);

        MockHttpServletResponse postRequest = utils.perform(
                post(utils.BASE_URL + LOGIN)
                        .contentType(APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();
        assertThat(postRequest.getStatus()).isEqualTo(200);
    }

    @Test
    public void getUserByIdTest() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + USERS_CONTROLLER_PATH + ID, expectedUser.getId()),
                expectedUser.getEmail()
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("example@gmail.com");
        assertThat(userRepository.findByEmail("example@gmail.com")).isNotNull();
        assertThat(body).contains("Ivan");
        assertThat(body).contains("Pavlov");
    }

    @Test
    public void getAllUsersTest() throws Exception {
        utils.regDefaultUser();

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + USERS_CONTROLLER_PATH)
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("example@gmail.com");
    }

    @Test
    public void duplicateUserRegistrationTest() throws Exception {
        final var user = utils.regDefaultUser();
        assertThat(user.getStatus()).isEqualTo(200);
        final var duplicateUser = utils.regDefaultUser();
        assertThat(duplicateUser.getStatus()).isEqualTo(500);
    }

    @Test
    public void testUpdateUserData() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);


        MockHttpServletResponse putResponse = utils.perform(
                put(utils.BASE_URL + USERS_CONTROLLER_PATH + ID, expectedUser.getId())
                        .contentType(APPLICATION_JSON)
                        .content(updateUserJson),
                expectedUser.getEmail()
        ).andReturn().getResponse();

        String body = putResponse.getContentAsString();

        assertThat(putResponse.getStatus()).isEqualTo(200);
        assertThat(body).contains("updated@gmail.com");
        assertThat(body).contains("newFirstName");
        assertThat(body).contains("newLastName");
    }

    @Test
    public void deleteUserTest() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);
        MockHttpServletResponse response = utils.perform(
                delete(utils.BASE_URL + USERS_CONTROLLER_PATH + ID, expectedUser.getId()),
                expectedUser.getEmail()
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(userRepository.count()).isEqualTo(0);
    }

}
