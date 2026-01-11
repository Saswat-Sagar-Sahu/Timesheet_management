package com.timesheet.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheet.management.dto.AddProjectRequest;
import com.timesheet.management.entity.Project;
import com.timesheet.management.usecase.AddProjectUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private AddProjectUseCase addProjectUseCase;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    void addProjectEndpoint_shouldReturnCreated_whenValid() throws Exception {
        AddProjectRequest request = AddProjectRequest.builder().projectCode("PRJ-100").name("App").client("Client").build();

        Project saved = new Project();
        saved.setId(100);
        saved.setProjectCode("PRJ-100");
        saved.setName("App");
        saved.setClient("Client");
        saved.setStatus(true);

        // mock usecase response
        com.timesheet.management.usecase.AddProjectUseCase.AddProjectResponse resp = com.timesheet.management.usecase.AddProjectUseCase.AddProjectResponse.builder()
                .status(true)
                .message("Project created")
                .errorCode(com.timesheet.management.usecase.AddProjectUseCase.ErrorCode.S_001)
                .project(saved)
                .build();

        when(addProjectUseCase.execute(any(AddProjectRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectCode").value("PRJ-100"));
    }

    @Test
    void addProjectEndpoint_shouldReturnBadRequest_whenDuplicate() throws Exception {
        AddProjectRequest request = AddProjectRequest.builder().projectCode("PRJ-100").name("App").client("Client").build();

        com.timesheet.management.usecase.AddProjectUseCase.AddProjectResponse resp = com.timesheet.management.usecase.AddProjectUseCase.AddProjectResponse.builder()
                .status(false)
                .message("Project with same projectCode already exists")
                .errorCode(com.timesheet.management.usecase.AddProjectUseCase.ErrorCode.E_003)
                .build();

        when(addProjectUseCase.execute(any(AddProjectRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
