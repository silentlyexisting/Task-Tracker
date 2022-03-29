package hexlet.code.controller;


import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.transaction.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.assertj.core.api.Assertions.assertThat;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

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
class TaskControllerTest {

    @Autowired
    private TestUtils utils;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    private static String updateTaskJson;

    @BeforeAll
    void initialization() throws IOException {
        updateTaskJson = utils.readFixturesAsString(utils.FIXTURES_PATH + utils.UPDATE_TASK_DATA);
    }

    @Test
    public void createTaskTest() throws Exception {
        assertThat(taskRepository.count()).isEqualTo(1);
        utils.regDefaultTask();
        assertThat(taskRepository.count()).isEqualTo(2);
    }

    @Test
    public void getTaskById() throws Exception {
        utils.regDefaultTask();
        final Task expectedTask = taskRepository.findAll().get(1);

        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + TASK_CONTROLLER_PATH + utils.ID, expectedTask.getId())
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("Test Task");
        assertThat(body).contains("Testing task handler");
        assertThat(body).contains("Important"); // task status
        assertThat(body).contains("example@gmail.com"); // executor email
        assertThat(body).contains("Documentation");
    }

    @Test
    public void getAllTasks() throws Exception {
        utils.regDefaultTask();
        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + TASK_CONTROLLER_PATH)
                        .contentType(APPLICATION_JSON)
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(body).contains("example@gmail.com");
        assertThat(body).contains("charles.mason@gmail.com");
    }

    @Test
    public void getAllTasksWithFiltration() throws Exception {
        utils.regDefaultTask();
        MockHttpServletResponse response = utils.perform(
                get(utils.BASE_URL + TASK_CONTROLLER_PATH + "?taskStatusId=3&executorId=10&labelIds=3")
                        .contentType(APPLICATION_JSON)
        ).andReturn().getResponse();

        final String body = response.getContentAsString();

        assertThat(body).contains("Test Task").contains("Testing task handler");
        assertThat(body).contains("Important").contains("example@gmail.com").contains("Documentation");
    }

    @Test
    public void updateTaskTest() throws Exception {
        final Task task = taskRepository.findAll().get(0);
        final User user = userRepository.findAll().get(1);
        MockHttpServletResponse response = utils.perform(
                put(utils.BASE_URL + TASK_CONTROLLER_PATH + utils.ID, task.getId())
                        .contentType(APPLICATION_JSON)
                        .content(updateTaskJson),
                user.getEmail()
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("New name").contains("New description")
                .contains("owen.grey@gmail.com").contains("Documentation");
    }

    @Test
    public void deleteTaskTest() throws Exception {
        utils.regDefaultTask();
        final Task task = taskRepository.findAll().get(1);

        final User user = userRepository.findAll().get(0);

        MockHttpServletResponse response = utils.perform(
                delete(utils.BASE_URL + TASK_CONTROLLER_PATH + utils.ID, task.getId()),
                user.getEmail()
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(taskRepository.count()).isEqualTo(1);
    }
}
