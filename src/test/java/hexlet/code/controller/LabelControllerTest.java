package hexlet.code.controller;


import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.assertj.core.api.Assertions.assertThat;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@TestInstance(Lifecycle.PER_CLASS)
@DBRider
@DataSet("dataset.yml")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class LabelControllerTest {

    @Autowired
    private TestUtils utils;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;

    private static final String UPDATE_LABEL_DATA = "{\n  \"name\": \"Updated label\"\n}";

    @Test
    public void createLabelTest() throws Exception {
        assertThat(labelRepository.count()).isEqualTo(3);
        utils.regDefaultLabel();
        assertThat(labelRepository.count()).isEqualTo(4);
    }

    @Test
    public void getLabelByIdTest() throws Exception {
        utils.regDefaultLabel();
        final Label label = labelRepository.findAll().get(3);
        final User user = userRepository.findAll().get(1);

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + LABEL_CONTROLLER_PATH + utils.ID, label.getId()),
                user.getEmail()
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("New label");
    }

    @Test
    public void getAllLabelsTest() throws Exception {
        final User user = userRepository.findAll().get(1);

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + LABEL_CONTROLLER_PATH),
                user.getEmail()
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(body).contains("Test label").contains("Bug").contains("Documentation");
    }

    @Test
    public void updateLabelByIdTest() throws Exception {
        final User user = userRepository.findAll().get(1);
        final Label label = labelRepository.findAll().get(0);
        MockHttpServletResponse response = utils.perform(
                put(utils.BASE_URL + LABEL_CONTROLLER_PATH + utils.ID, label.getId())
                        .contentType(APPLICATION_JSON)
                        .content(UPDATE_LABEL_DATA),
                user.getEmail()
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("Updated label");
    }

    @Test
    public void deleteLabelTest() throws Exception {
        final User user = userRepository.findAll().get(1);
        final Label label = labelRepository.findAll().get(0);

        MockHttpServletResponse response = utils.perform(
                delete(utils.BASE_URL + LABEL_CONTROLLER_PATH + utils.ID, label.getId()),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(labelRepository.count()).isEqualTo(2);
    }
}
