package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.Activity;
import com.timesheet.management.entity.Project;
import com.timesheet.management.entity.ProjectActivity;
import com.timesheet.management.repository.ProjectActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectActivityQueryUseCaseImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectActivityRepository projectActivityRepository;

    @InjectMocks
    private ProjectActivityQueryUseCaseImpl impl;

    @Test
    void listByProject_shouldReturnError_whenProjectNotFound() {
        when(projectRepository.findById(999)).thenReturn(Optional.empty());
        var resp = impl.listByProject(999);
        assertThat(resp.isStatus()).isFalse();
    }

    @Test
    void listByProject_shouldReturnActivities_whenFound() {
        Project p = new Project(); p.setId(45);
        Activity a1 = new Activity(); a1.setCode(10); a1.setName("Dev");
        ProjectActivity pa = new ProjectActivity(); pa.setProject(p); pa.setActivity(a1);
        when(projectRepository.findById(45)).thenReturn(Optional.of(p));
        when(projectActivityRepository.findAllByProject(p)).thenReturn(java.util.List.of(pa));
        var resp = impl.listByProject(45);
        assertThat(resp.isStatus()).isTrue();
        assertThat(resp.getActivities()).hasSize(1);
        assertThat(resp.getActivities().get(0).getCode()).isEqualTo(10);
    }
}
