package com.timesheet.management.usecase.impl;

import com.timesheet.management.dto.AddProjectActivityRequest;
import com.timesheet.management.entity.Activity;
import com.timesheet.management.entity.Project;
import com.timesheet.management.entity.ProjectActivity;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.repository.ProjectActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProjectActivityUseCaseImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ProjectActivityRepository projectActivityRepository;

    @InjectMocks
    private AddProjectActivityUseCaseImpl impl;

    @Test
    void execute_shouldReturnError_whenMissingFields() {
        AddProjectActivityRequest req = AddProjectActivityRequest.builder().projectId(null).activityCode(null).build();
        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void execute_shouldReturnError_whenProjectOrActivityNotFound() {
        AddProjectActivityRequest req = AddProjectActivityRequest.builder().projectId(45).activityCode(10).build();
        when(projectRepository.findById(45)).thenReturn(Optional.empty());
        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void execute_shouldCreateMapping_whenValid() {
        AddProjectActivityRequest req = AddProjectActivityRequest.builder().projectId(45).activityCode(10).build();
        Project p = new Project(); p.setId(45);
        Activity a = new Activity(); a.setCode(10);
        when(projectRepository.findById(45)).thenReturn(Optional.of(p));
        when(activityRepository.findById(10)).thenReturn(Optional.of(a));
        when(projectActivityRepository.existsByProjectAndActivity(p, a)).thenReturn(false);
        ProjectActivity saved = new ProjectActivity();
        saved.setId(1);
        saved.setProject(p);
        saved.setActivity(a);
        when(projectActivityRepository.save(any(ProjectActivity.class))).thenReturn(saved);

        var resp = impl.execute(req);
        assertThat(resp.isStatus()).isTrue();
        verify(projectActivityRepository).save(any(ProjectActivity.class));
    }
}

