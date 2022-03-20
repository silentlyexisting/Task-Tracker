package hexlet.code.controller;


import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.assertj.core.api.Assertions.assertThat;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.ID;

import static org.springframework.http.MediaType.*;
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
                get(utils.BASE_URL + TASK_CONTROLLER_PATH + ID, expectedTask.getId())
        ).andReturn().getResponse();

        String body = response.getContentAsString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(APPLICATION_JSON.toString());
        assertThat(body).contains("Test Task");
        assertThat(body).contains("Testing task handler");
        assertThat(body).contains("Closed"); // task status
        assertThat(body).contains("example@gmail.com"); // executor email
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
        assertThat(body).contains("example@gmail.com"); // executor email
        assertThat(body).contains("testemail@gmail.com"); // executor email
    }
}