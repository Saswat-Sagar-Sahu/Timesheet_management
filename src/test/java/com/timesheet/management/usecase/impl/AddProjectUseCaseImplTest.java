package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.AddProjectRequest;
import com.timesheet.management.entity.Project;
import com.timesheet.management.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProjectUseCaseImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private AddProjectUseCaseImpl addProjectUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute_shouldReturnError_whenProjectCodeExists() {
        AddProjectRequest request = AddProjectRequest.builder().projectCode("PRJ-001").name("Test").client("C").build();

        when(projectRepository.existsByProjectCode("PRJ-001")).thenReturn(true);

        var resp = addProjectUseCase.execute(request);

        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(com.timesheet.management.usecase.AddProjectUseCase.ErrorCode.E_003);
        verify(projectRepository, times(1)).existsByProjectCode("PRJ-001");
        verify(projectRepository, never()).save(any());
    }

    @Test
    void execute_shouldCreateProject_whenValidRequest() {
        AddProjectRequest request = AddProjectRequest.builder().projectCode("PRJ-002").name("Website").client("Acme").build();

        when(projectRepository.existsByProjectCode("PRJ-002")).thenReturn(false);

        Project saved = new Project();
        saved.setId(45);
        saved.setProjectCode("PRJ-002");
        saved.setName("Website");
        saved.setClient("Acme");
        saved.setStatus(true);

        when(projectRepository.save(any(Project.class))).thenReturn(saved);

        var resp = addProjectUseCase.execute(request);

        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getProject()).isNotNull();
        assertThat(resp.getProject().getId()).isEqualTo(45);
        assertThat(resp.getProject().getProjectCode()).isEqualTo("PRJ-002");

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(captor.capture());
        Project toSave = captor.getValue();
        assertThat(toSave.getProjectCode()).isEqualTo("PRJ-002");
        assertThat(toSave.getName()).isEqualTo("Website");
    }
}

