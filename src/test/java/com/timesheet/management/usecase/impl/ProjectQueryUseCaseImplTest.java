package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.Project;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.usecase.ProjectQueryUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectQueryUseCaseImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectQueryUseCaseImpl impl;

    @Test
    void getById_shouldReturnError_whenNotFound() {
        when(projectRepository.findById(999)).thenReturn(Optional.empty());
        ProjectQueryUseCase.GetProjectResponse resp = impl.getById(999);
        assertThat(resp.isStatus()).isFalse();
        assertThat(resp.getErrorCode()).isEqualTo(ProjectQueryUseCase.ErrorCode.E_002);
    }

    @Test
    void getById_shouldReturnProject_whenFound() {
        Project p = new Project(); p.setId(45); p.setName("Website");
        when(projectRepository.findById(45)).thenReturn(Optional.of(p));
        ProjectQueryUseCase.GetProjectResponse resp = impl.getById(45);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getProject()).isNotNull();
        assertThat(resp.getProject().getId()).isEqualTo(45);
    }

    @Test
    void listAll_shouldReturnProjects() {
        Project p1 = new Project(); p1.setId(1); p1.setName("A");
        Project p2 = new Project(); p2.setId(2); p2.setName("B");
        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1,p2));
        ProjectQueryUseCase.ListProjectsResponse resp = impl.listAll();
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getProjects()).hasSize(2);
    }
}

