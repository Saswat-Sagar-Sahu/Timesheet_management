package com.timesheet.management.usecase.impl;

import com.timesheet.management.entity.Activity;
import com.timesheet.management.entity.Project;
import com.timesheet.management.repository.ActivityRepository;
import com.timesheet.management.repository.ProjectActivityRepository;
import com.timesheet.management.repository.ProjectRepository;
import com.timesheet.management.usecase.ProjectActivityManageUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j(topic = "ProjectActivityManageUseCaseImpl")
public class ProjectActivityManageUseCaseImpl implements ProjectActivityManageUseCase {

    private ProjectRepository projectRepository;
    private ActivityRepository activityRepository;
    private ProjectActivityRepository projectActivityRepository;

    @Override
    public RemoveProjectActivityResponse remove(RemoveProjectActivityRequest request) {
        if (request == null || request.getProjectId() == null || request.getActivityCode() == null) {
            return RemoveProjectActivityResponse.builder().status(false).message("projectId and activityCode required").errorCode(ErrorCode.E_002).build();
        }

        try {
            Optional<Project> pOpt = projectRepository.findById(request.getProjectId());
            if (pOpt.isEmpty()) {
                return RemoveProjectActivityResponse.builder().status(false).message("Project not found").errorCode(ErrorCode.E_002).build();
            }
            Optional<Activity> aOpt = activityRepository.findById(request.getActivityCode());
            if (aOpt.isEmpty()) {
                return RemoveProjectActivityResponse.builder().status(false).message("Activity not found").errorCode(ErrorCode.E_002).build();
            }

            projectActivityRepository.deleteByProjectAndActivity(pOpt.get(), aOpt.get());
            return RemoveProjectActivityResponse.builder().status(true).message("Mapping removed").errorCode(ErrorCode.S_001).build();
        } catch (Exception e) {
            log.error("Error removing project activity mapping", e);
            return RemoveProjectActivityResponse.builder().status(false).message("An error occurred").errorCode(ErrorCode.E_001).build();
        }
    }
}

