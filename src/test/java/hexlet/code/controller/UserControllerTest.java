package hexlet.code.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
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

import javax.transaction.Transactional;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@TestInstance(Lifecycle.PER_CLASS)
@DBRider
@DataSet("dataset.yml")
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
        updateUserJson = utils.readFixturesAsString(utils.FIXTURES_PATH + UPDATE_USER_DATA);
    }

    @Test
    public void registrationUserTest() throws Exception {
        assertThat(userRepository.count()).isEqualTo(3);
        utils.regDefaultUser();
        assertThat(userRepository.count()).isEqualTo(4);
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
        final User expectedUser = userRepository.findAll().get(3);

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + USERS_CONTROLLER_PATH + utils.ID, expectedUser.getId()),
                expectedUser.getEmail()
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("test@gmail.com");
        assertThat(userRepository.findByEmail("test@gmail.com")).isNotNull();
        assertThat(body).contains("Ivan");
        assertThat(body).contains("Pavlov");
    }

    @Test
    public void getAllUsersTest() throws Exception {
        utils.regDefaultUser();

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + USERS_CONTROLLER_PATH)
        ).andReturn().getResponse();

        final String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("test@gmail.com");
    }

    @Test
    public void duplicateUserRegistrationTest() throws Exception {
        final var user = utils.regDefaultUser();
        assertThat(user.getStatus()).isEqualTo(201);
        final var duplicateUser = utils.regDefaultUser();
        assertThat(duplicateUser.getStatus()).isEqualTo(500);
    }

    @Test
    public void testUpdateUserData() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        MockHttpServletResponse putResponse = utils.perform(
                put(utils.BASE_URL + USERS_CONTROLLER_PATH + utils.ID, expectedUser.getId())
                        .contentType(APPLICATION_JSON)
                        .content(updateUserJson),
                expectedUser.getEmail()
        ).andReturn().getResponse();

        final String body = putResponse.getContentAsString();

        assertThat(putResponse.getStatus()).isEqualTo(200);
        assertThat(body).contains("updated@gmail.com");
        assertThat(body).contains("NewFirstName");
        assertThat(body).contains("NewLastName");
    }

    @Test
    public void deleteUserTest() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        MockHttpServletResponse response = utils.perform(
                delete(utils.BASE_URL + USERS_CONTROLLER_PATH + utils.ID, expectedUser.getId()),
                expectedUser.getEmail()
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(userRepository.count()).isEqualTo(2);
    }
}
