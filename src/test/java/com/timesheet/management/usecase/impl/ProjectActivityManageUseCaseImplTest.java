package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.Activity;
import com.timesheet.management.entity.Project;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.repository.ProjectActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.usecase.ProjectActivityManageUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectActivityManageUseCaseImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ProjectActivityRepository projectActivityRepository;

    @InjectMocks
    private ProjectActivityManageUseCaseImpl impl;

    @Test
    void remove_shouldReturnError_whenMissing() {
        var resp = impl.remove(null);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void remove_shouldReturnError_whenProjectNotFound() {
        var req = ProjectActivityManageUseCase.RemoveProjectActivityRequest.builder().projectId(1).activityCode(10).build();
        when(projectRepository.findById(1)).thenReturn(Optional.empty());
        var resp = impl.remove(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void remove_shouldReturnError_whenActivityNotFound() {
        var req = ProjectActivityManageUseCase.RemoveProjectActivityRequest.builder().projectId(1).activityCode(10).build();
        when(projectRepository.findById(1)).thenReturn(Optional.of(new Project()));
        when(activityRepository.findById(10)).thenReturn(Optional.empty());
        var resp = impl.remove(req);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void remove_shouldSucceed_whenMappingExistsOrNot() {
        var req = ProjectActivityManageUseCase.RemoveProjectActivityRequest.builder().projectId(1).activityCode(10).build();
        Project p = new Project(); p.setId(1);
        Activity a = new Activity(); a.setCode(10);
        when(projectRepository.findById(1)).thenReturn(Optional.of(p));
        when(activityRepository.findById(10)).thenReturn(Optional.of(a));
        doNothing().when(projectActivityRepository).deleteByProjectAndActivity(p, a);
        var resp = impl.remove(req);
        assertThat(resp.isStatus()).isTrue();
        verify(projectActivityRepository).deleteByProjectAndActivity(p,a);
    }
}

