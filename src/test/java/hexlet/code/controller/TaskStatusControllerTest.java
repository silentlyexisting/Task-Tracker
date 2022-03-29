package hexlet.code.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@TestInstance(Lifecycle.PER_CLASS)
@DBRider
@DataSet("dataset.yml")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class TaskStatusControllerTest {

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TestUtils utils;
    @Autowired
    private UserRepository userRepository;

    private static final String UPDATE_STATUS_DATA = "{\n  \"name\": \"Updated\"\n}";;

    @Test
    public void createTaskStatusTest() throws Exception {
        assertThat(taskStatusRepository.count()).isEqualTo(3);
        utils.regDefaultTaskStatus();
        assertThat(taskStatusRepository.count()).isEqualTo(4);
    }

    @Test
    public void getTaskStatusById() throws Exception {
        utils.regDefaultTaskStatus();
        final TaskStatus expectedTaskStatus = taskStatusRepository.findAll().get(3);

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + TASK_STATUS_CONTROLLER_PATH + utils.ID, expectedTaskStatus.getId())
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("Created");
    }

    @Test
    public void getAllTaskStatuses() throws Exception {
        utils.regDefaultTaskStatus();

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + TASK_STATUS_CONTROLLER_PATH)
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("Created");
        assertThat(body).contains("New");
        assertThat(body).contains("Important");
    }

    @Test
    public void updateTaskStatusTest() throws Exception {
        final TaskStatus expectedTaskStatus = taskStatusRepository.findAll().get(0);

        final User user = userRepository.findAll().get(0);

        MockHttpServletResponse response = utils.perform(
                put(utils.BASE_URL + TASK_STATUS_CONTROLLER_PATH + utils.ID, expectedTaskStatus.getId())
                        .contentType(APPLICATION_JSON)
                        .content(UPDATE_STATUS_DATA),
                user.getEmail()
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(body).contains("Updated");
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
    }

    @Test
    public void deleteTaskStatusTest() throws Exception {
        utils.regDefaultTaskStatus();
        final TaskStatus expectedTaskStatus = taskStatusRepository.findAll().get(3);

        final User user = userRepository.findAll().get(0);

        MockHttpServletResponse response = utils.perform(
                delete(utils.BASE_URL + TASK_STATUS_CONTROLLER_PATH + utils.ID, expectedTaskStatus.getId()),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(taskStatusRepository.count()).isEqualTo(3);

    }
}
