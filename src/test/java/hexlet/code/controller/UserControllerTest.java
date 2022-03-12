package hexlet.code.controller;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static hexlet.code.controller.UserController.USERS_CONTROLLER_PATH;

import javax.transaction.Transactional;


import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils utils;

    @Test
    public void createUserTest() throws Exception {
        assertThat(userRepository.count()).isEqualTo(0);
        final var request = utils.regDefaultUser();
        assertThat(request.getStatus()).isEqualTo(200);
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    public void getUserTest() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + USERS_CONTROLLER_PATH + "/{id}", expectedUser.getId()),
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

//    @Test
//    public void getUnauthenticatedUserTest() throws Exception {
//        final User expectedUser = userRepository.findAll().get(0);
//
//        MockHttpServletResponse response = utils.perform(
//                get(utils.BASE_URL + USERS_CONTROLLER_PATH + "/{id}", expectedUser.getId())
//        ).andReturn().getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(401);
//    }

    @Test
    public void getAllUsersTest() throws Exception {
        utils.regDefaultUser();

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + USERS_CONTROLLER_PATH)
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("Ivan");
    }

}
